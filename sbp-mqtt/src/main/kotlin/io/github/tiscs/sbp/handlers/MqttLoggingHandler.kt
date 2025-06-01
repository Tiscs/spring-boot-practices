package io.github.tiscs.sbp.handlers

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPipelineException
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.mqtt.MqttConnectMessage
import io.netty.handler.codec.mqtt.MqttMessage
import io.netty.util.AttributeKey
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger(MqttLoggingHandler::class.java)
private val CLIENT_ID_KEY = AttributeKey.valueOf<String>("CLIENT_IDENTIFIER")!!

class MqttLoggingHandler : ChannelDuplexHandler() {
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is MqttMessage) {
            if (msg is MqttConnectMessage) {
                ctx.channel().attr(CLIENT_ID_KEY).set(msg.payload().clientIdentifier())
            }
            if (!ctx.channel().hasAttr(CLIENT_ID_KEY)) {
                ctx.close(ChannelPipelineException("Invalid packet from unconnected client"))
                return
            } else if (LOGGER.isDebugEnabled) {
                LOGGER.debug("Received from [${ctx.channel().attr(CLIENT_ID_KEY).get()}]: ${msg.toLog()}")
            }
        }
        super.channelRead(ctx, msg)
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any?, promise: ChannelPromise?) {
        if (msg is MqttMessage && LOGGER.isDebugEnabled) {
            LOGGER.debug(" Writing into [${ctx.channel().attr(CLIENT_ID_KEY).get()}]: ${msg.toLog()}")
        }
        super.write(ctx, msg, promise)
    }
}

internal fun ChannelHandlerContext.close(cause: Throwable): ChannelFuture {
    LOGGER.warn("Channel closed: $cause")
    return this.close()
}

internal fun MqttMessage.toLog(): String {
    return "[fixedHeader: ${fixedHeader()}" +
        ", variableHeader: ${variableHeader()}" +
        ", payload: ${
            if (payload() is ByteBuf)
                ByteBufUtil.hexDump(payload() as ByteBuf)
            else
                payload()
        }]"
}
