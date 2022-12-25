package de.ngloader.wuppy.friend;

import java.util.UUID;

public class Friend {
	
	private UUID uuid;
	private String username;
	
	private FriendStatus status;
	private FriendType type;
	
	public Friend(UUID uuid, String username, FriendStatus status, FriendType type) {
		this.uuid = uuid;
		this.username = username;
		this.status = status;
		this.type = type;
	}
	
	public void acceptFriendRequest() {
		
	}
	
	public void removeFriend() {
		
	}
	
	public FriendStatus getStatus() {
		return status;
	}
	
	public FriendType getType() {
		return type;
	}
	
	public String getUsername() {
		return username;
	}
	
	public UUID getUUID() {
		return uuid;
	}
}