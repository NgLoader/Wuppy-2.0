package de.ngloader.wuppy.client.handler;

import de.ngloader.wuppy.client.Client;
import de.ngloader.wuppy.client.PacketSerializer;
import de.ngloader.wuppy.client.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf output) throws Exception {
		int id = Client.OUT_PACKETS.indexOf(packet.getClass());
		if(id == -1)
			throw new NullPointerException("Couldn't find id of packet " + packet.getClass().getSimpleName());
		PacketSerializer packetSerializer = new PacketSerializer(output);
		packetSerializer.writeVarInt(id);
		packet.write(packetSerializer);
	}
}