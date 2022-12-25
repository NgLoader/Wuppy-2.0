package de.ngloader.wuppy.friend;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.ngloader.wuppy.client.Client;
import de.ngloader.wuppy.client.packets.in.FriendPacket;
import net.minecraft.client.Minecraft;

public class FriendManager {
	
	private final Map<UUID, String> friends = new HashMap<>();
	
	public boolean requestFriend(UUID uuid) {
		if(friends.containsKey(uuid))
			return false;
		Client.sendPacket(new FriendPacket(Minecraft.getMinecraft().getSession().getProfile().getId(), null, FriendPacket.Usage.REQUEST	, uuid, null, null, null));
		return true;
	}
	
	public void addFriend(UUID uuid, String username) {
		friends.put(uuid, username);
	}
	
	public void removeFriend(UUID uuid) {
		friends.remove(uuid);
		Client.sendPacket(new FriendPacket(Minecraft.getMinecraft().getSession().getProfile().getId(), null, FriendPacket.Usage.REMOVE	, uuid, null, null, null));
	}
	
	public boolean isFriend(UUID uuid) {
		try {
			return friends.containsKey(uuid);
		}catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	public String getUsername(UUID uuid) {
		return friends.get(uuid);
	}
}