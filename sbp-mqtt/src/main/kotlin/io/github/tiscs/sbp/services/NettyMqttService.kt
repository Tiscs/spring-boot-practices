package io.github.tiscs.sbp.services

import io.github.tiscs.sbp.handlers.MqttLoggingHandler
import io.github.tiscs.sbp.handlers.MqttServiceHandler
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.mqtt.MqttDecoder
import io.netty.handler.codec.mqtt.MqttEncoder
import io.netty.handler.timeout.ReadTimeoutHandler
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

private val LOGGER = LoggerFactory.getLogger(NettyMqttService::class.java)

@Component
class NettyMqttService {
    private val parentGroup: NioEventLoopGroup = NioEventLoopGroup(1)
    private val workerGroup: NioEventLoopGroup = NioEventLoopGroup()

    @PostConstruct
    fun start() {
        val bootstrap = ServerBootstrap()
        bootstrap.group(parentGroup, workerGroup)
            .channel(NioServerSocketChannel::class.java)
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(channel: SocketChannel) {
                    channel.pipeline()
                        .addLast("timeout", ReadTimeoutHandler(30))
                        .addLast("decoder", MqttDecoder(4 * 1024))
                        .addLast("encoder", MqttEncoder.INSTANCE)
                        .addLast("logging", MqttLoggingHandler())
                        .addLast("service", MqttServiceHandler())
                }
            })
        bootstrap.bind(1883).sync()
        LOGGER.info("MQTT server listening on 1883")
    }

    @PreDestroy
    fun close() {
        LOGGER.info("MQTT server shutting down")
        workerGroup.shutdownGracefully().sync()
        parentGroup.shutdownGracefully().sync()
    }
}
