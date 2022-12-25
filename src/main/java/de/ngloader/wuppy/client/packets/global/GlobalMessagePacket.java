package de.ngloader.wuppy.client.packets.global;

import java.io.IOException;

import de.ngloader.wuppy.client.PacketSerializer;
import de.ngloader.wuppy.client.packets.Packet;

public class GlobalMessagePacket implements Packet {
	
	public String message;
	
	public GlobalMessagePacket() { }
	
	public GlobalMessagePacket(String message) {
		this.message = message;
	}
	
	@Override
	public void read(PacketSerializer packetSerializer) throws IOException {
		this.message = packetSerializer.readString();
	}
	
	@Override
	public void write(PacketSerializer packetSerializer) throws IOException {
		packetSerializer.writeString(message);
	}
}