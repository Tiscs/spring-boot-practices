package io.github.tiscs.sbp.handlers

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipelineException
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.mqtt.MqttConnectMessage
import io.netty.handler.codec.mqtt.MqttMessage
import org.slf4j.LoggerFactory

class MqttLoggingHandler : ChannelDuplexHandler() {
    companion object {
        internal val logger = LoggerFactory.getLogger(MqttLoggingHandler::class.java)
    }

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any?) {
        if (msg is MqttMessage) {
            if (msg is MqttConnectMessage) {
                ctx.channel().attr(AttributeKeys.CLIENT_ID_KEY).set(msg.payload().clientIdentifier())
            }
            if (!ctx.channel().hasAttr(AttributeKeys.CLIENT_ID_KEY)) {
                ctx.close(ChannelPipelineException("Invalid packet from unconnected client"))
                return
            } else if (logger.isDebugEnabled) {
                logger.debug("Received from [${ctx.channel().attr(AttributeKeys.CLIENT_ID_KEY).get()}]: ${msg.toLog()}")
            }
        }
        super.channelRead(ctx, msg)
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any?, promise: ChannelPromise?) {
        if (msg is MqttMessage && logger.isDebugEnabled) {
            logger.debug(" Writing into [${ctx.channel().attr(AttributeKeys.CLIENT_ID_KEY).get()}]: ${msg.toLog()}")
        }
        super.write(ctx, msg, promise)
    }

    @Suppress("OverridingDeprecatedMember")
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        ctx.close(cause)
    }
}
