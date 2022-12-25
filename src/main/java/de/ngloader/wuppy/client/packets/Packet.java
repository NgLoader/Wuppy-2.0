package de.ngloader.wuppy.client.packets;

import java.io.IOException;

import de.ngloader.wuppy.client.PacketSerializer;

public interface Packet {
	
	public void read(PacketSerializer packetSerializer) throws IOException;
	public void write(PacketSerializer packetSerializer) throws IOException;
	
}