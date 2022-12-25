package de.ngloader.wuppy.client;

import java.util.Arrays;
import java.util.List;

import de.ngloader.wuppy.client.handler.PacketDecoder;
import de.ngloader.wuppy.client.handler.PacketEncoder;
import de.ngloader.wuppy.client.packets.Packet;
import de.ngloader.wuppy.client.packets.PacketExit;
import de.ngloader.wuppy.client.packets.PacketPing;
import de.ngloader.wuppy.client.packets.global.GlobalMessagePacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	
	private static Client instance;
	private static Channel channel;
	
	public static final boolean EPOLL = Epoll.isAvailable();
	
	public static final List<Class<? extends Packet>> OUT_PACKETS = Arrays.asList(PacketExit.class, PacketPing.class, GlobalMessagePacket.class);
	public static final List<Class<? extends Packet>> IN_PACKETS = Arrays.asList(PacketExit.class, PacketPing.class, GlobalMessagePacket.class);
	
	public static Channel getChannel() {
		return channel;
	}
	
	public static Client getInstance() {
		return instance;
	}
	
	public Client() {
		instance = this;
	}
	
	public void connect() throws Exception {
		if(this.channel != null || this.channel.isOpen())
			return;
		
		EventLoopGroup eventLoopGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
		try {
			channel = new Bootstrap()
			.group(eventLoopGroup)
			.channel(EPOLL ? EpollSocketChannel.class : NioSocketChannel.class)
			.handler(new ChannelInitializer<Channel>() {
				@Override
				protected void initChannel(Channel channel) throws Exception {
					channel.pipeline()
					.addLast(new PacketEncoder())
					.addLast(new PacketDecoder())
					.addLast(new NetworkHandler());
				}
			}).connect("localhost", 2000).sync().channel();
			
			while(channel.isOpen());
		} finally {
			eventLoopGroup.shutdownGracefully();
		}
	}
	
	public void disconnect() {
		if(this.channel != null && this.channel.isOpen())
			this.channel.close();
		this.channel = null;
	}
	
	public static void sendPacket(Packet packet) {
		channel.writeAndFlush(packet, channel.voidPromise());
	}
}