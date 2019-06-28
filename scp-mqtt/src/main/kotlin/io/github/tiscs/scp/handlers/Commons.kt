package io.github.tiscs.scp.handlers

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.mqtt.MqttMessage
import io.netty.util.AttributeKey

internal object AttributeKeys {
    val CLIENT_ID_KEY = AttributeKey.valueOf<String>("CLIENT_IDENTIFIER")!!
}

internal fun ChannelHandlerContext.close(cause: Throwable): ChannelFuture {
    MqttLoggingHandler.logger.warn("Channel closed: $cause")
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
