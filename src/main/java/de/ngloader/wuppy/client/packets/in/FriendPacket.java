package de.ngloader.wuppy.client.packets.in;

import java.io.IOException;
import java.util.UUID;

import de.ngloader.wuppy.client.PacketSerializer;
import de.ngloader.wuppy.client.packets.Packet;
import de.ngloader.wuppy.friend.FriendStatus;

public class FriendPacket implements Packet {
	
	public Usage usage;
	
	public UUID uuid;
	public String token;
	
	public UUID friend;
	public String name;
	public String message;
	public FriendStatus status;
	
	public String[] messageLog;
	
	public FriendPacket() { }
	
	public FriendPacket(UUID uuid, String token, Usage usage, UUID friend, String name, String message, FriendStatus status) {
		this.uuid = uuid;
		this.token = token;
		this.usage = usage;
		this.friend = friend;
		this.name = name;
		this.message = message;
		this.status = status;
	}
	
	@Override
	public void read(PacketSerializer packetSerializer) throws IOException {
		this.usage = packetSerializer.readEnum(Usage.class);
		this.friend = packetSerializer.readUUID();
		
		switch (usage) {
		case MESSAGE:
			this.message = packetSerializer.readString();
			break;
		case STATUS:
			this.status = packetSerializer.readEnum(FriendStatus.class);
			break;
		case INFO:
			this.name = packetSerializer.readString();
			this.status = packetSerializer.readEnum(FriendStatus.class);
			break;
		case MESSAGELOG:
			this.messageLog = new String[packetSerializer.readInt()];
			
			for(int i = 0; i < messageLog.length; i++)
				messageLog[i] = packetSerializer.readString();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void write(PacketSerializer packetSerializer) throws IOException {
		packetSerializer.writeEnum(usage);
		packetSerializer.writeUUID(uuid);
		packetSerializer.writeString(token);
		
		packetSerializer.writeUUID(friend);
		
		switch (usage) {
		case MESSAGE:
			packetSerializer.writeString(message);
			break;
		case STATUS:
			packetSerializer.writeEnum(status);
			break;
		case INFO:
			packetSerializer.writeString(name);
			packetSerializer.writeEnum(status);
			break;
		default:
			break;
		}
	}
	
	public enum Usage {
		REQUEST,
		REMOVE,
		MESSAGE,
		STATUS,
		INFO,
		MESSAGELOG
	}
}