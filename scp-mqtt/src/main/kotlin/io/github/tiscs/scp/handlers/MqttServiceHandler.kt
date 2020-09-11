package io.github.tiscs.scp.handlers

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelPipelineException
import io.netty.handler.codec.mqtt.*
import io.netty.handler.timeout.ReadTimeoutHandler
import org.slf4j.LoggerFactory

class MqttServiceHandler : ChannelInboundHandlerAdapter() {
    companion object {
        private val logger = LoggerFactory.getLogger(MqttServiceHandler::class.java)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is MqttMessage) {
            when (msg.fixedHeader().messageType()) {
                MqttMessageType.CONNECT -> onConnect(ctx, msg as MqttConnectMessage)
                MqttMessageType.DISCONNECT -> onDisconnect(ctx, msg)
                MqttMessageType.PINGREQ -> onPing(ctx, msg)
                MqttMessageType.SUBSCRIBE -> onSubscribe(ctx, msg as MqttSubscribeMessage)
                MqttMessageType.UNSUBSCRIBE -> onUnsubscribe(ctx, msg as MqttUnsubscribeMessage)
                MqttMessageType.PUBLISH -> onPublish(ctx, msg as MqttPublishMessage)
                MqttMessageType.PUBACK -> onPubAck(ctx, msg as MqttPubAckMessage)
                MqttMessageType.PUBREC -> onPubRec(ctx, msg)
                MqttMessageType.PUBREL -> onPubRel(ctx, msg)
                MqttMessageType.PUBCOMP -> onPubComp(ctx, msg)
                else -> run {
                    ctx.close(ChannelPipelineException("Unknown MQTT packet type: ${msg.fixedHeader().messageType()}, channel closed"))
                }
            }
        } else {
            ctx.close(ChannelPipelineException("Invalid message type: ${msg::class.simpleName ?: "UNKNOWN"}, channel closed"))
        }
    }

    private fun onConnect(ctx: ChannelHandlerContext, msg: MqttConnectMessage) {
        val returnCode = if ("MQTT" == msg.variableHeader().name() && 4 == msg.variableHeader().version()) {
            if (!msg.variableHeader().hasUserName() || !msg.variableHeader().hasPassword()) { // TODO: Authorize Device
                MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED
            } else if (msg.payload().clientIdentifier().length < 32) { // TODO: Verify Identifier
                MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED
            } else {
                MqttConnectReturnCode.CONNECTION_ACCEPTED
            }
        } else {
            MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION
        }
        val fixedHeader = MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0)
        val variableHeader = MqttConnAckVariableHeader(returnCode, false)
        ctx.writeAndFlush(MqttConnAckMessage(fixedHeader, variableHeader))
        ctx.pipeline().replace("timeout", "timeout", ReadTimeoutHandler(msg.variableHeader().keepAliveTimeSeconds() * 2))
    }

    private fun onDisconnect(ctx: ChannelHandlerContext, @Suppress("UNUSED_PARAMETER") msg: MqttMessage) {
        ctx.close()
    }

    private fun onPing(ctx: ChannelHandlerContext, @Suppress("UNUSED_PARAMETER") msg: MqttMessage) {
        val fixedHeader = MqttFixedHeader(MqttMessageType.PINGRESP, false, MqttQoS.AT_MOST_ONCE, false, 0)
        ctx.writeAndFlush(MqttMessage(fixedHeader))
    }

    private fun onSubscribe(ctx: ChannelHandlerContext, msg: MqttSubscribeMessage) {
        val fixedHeader = MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0)
        val variableHeader = MqttMessageIdVariableHeader.from(msg.variableHeader().messageId())
        val payload = MqttSubAckPayload(msg.payload().topicSubscriptions().map { r -> r.qualityOfService().value() }) // TODO: Verify subscribe topic name
        ctx.writeAndFlush(MqttSubAckMessage(fixedHeader, variableHeader, payload))
    }

    private fun onUnsubscribe(ctx: ChannelHandlerContext, msg: MqttUnsubscribeMessage) {
        val fixedHeader = MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0)
        val variableHeader = MqttMessageIdVariableHeader.from(msg.variableHeader().messageId())
        ctx.writeAndFlush(MqttUnsubAckMessage(fixedHeader, variableHeader))
    }

    private fun onPublish(ctx: ChannelHandlerContext, msg: MqttPublishMessage) {
        if (msg.variableHeader().topicName().length < 32) { // TODO: Verify publish topic name
            logger.warn("Invalid publish topic name: ${msg.variableHeader().topicName()}, channel closed")
            ctx.close()
        }
        val messageType = when (msg.fixedHeader().qosLevel()) {
            MqttQoS.AT_MOST_ONCE -> return
            MqttQoS.AT_LEAST_ONCE -> MqttMessageType.PUBACK
            MqttQoS.EXACTLY_ONCE -> run {
                // TODO: Store message identifier
                MqttMessageType.PUBREC
            }
            else -> run {
                ctx.close()
                logger.warn("Invalid MQTT QoS level: ${msg.fixedHeader().qosLevel()}, channel closed")
                return
            }
        }
        // TODO: Distribute message
        val fixedHeader = MqttFixedHeader(messageType, false, MqttQoS.AT_MOST_ONCE, false, 0)
        val variableHeader = MqttMessageIdVariableHeader.from(msg.variableHeader().packetId())
        ctx.writeAndFlush(MqttMessage(fixedHeader, variableHeader))
    }

    private fun onPubAck(@Suppress("UNUSED_PARAMETER") ctx: ChannelHandlerContext, @Suppress("UNUSED_PARAMETER") msg: MqttPubAckMessage) {
        // TODO: Delete message
    }

    private fun onPubRec(ctx: ChannelHandlerContext, msg: MqttMessage) {
        // TODO: Delete message
        val fixedHeader = MqttFixedHeader(MqttMessageType.PUBREL, false, MqttQoS.AT_LEAST_ONCE, false, 0)
        val variableHeader = MqttMessageIdVariableHeader.from((msg.variableHeader() as MqttMessageIdVariableHeader).messageId())
        ctx.writeAndFlush(MqttMessage(fixedHeader, variableHeader))
    }

    private fun onPubRel(ctx: ChannelHandlerContext, msg: MqttMessage) {
        // TODO: Delete message identifier
        val fixedHeader = MqttFixedHeader(MqttMessageType.PUBCOMP, false, MqttQoS.AT_MOST_ONCE, false, 0)
        val variableHeader = MqttMessageIdVariableHeader.from((msg.variableHeader() as MqttMessageIdVariableHeader).messageId())
        ctx.writeAndFlush(MqttMessage(fixedHeader, variableHeader))
    }

    private fun onPubComp(@Suppress("UNUSED_PARAMETER") ctx: ChannelHandlerContext, @Suppress("UNUSED_PARAMETER") msg: MqttMessage) {
        // TODO: Delete message
    }
}
