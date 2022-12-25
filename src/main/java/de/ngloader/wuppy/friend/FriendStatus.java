package de.ngloader.wuppy.friend;

public enum FriendStatus {
	
	ONLINE("Online"),
	AWAY("Abwesend"),
	DONOTDISTORB("Geh mir nicht auf die nerven!");
	
	private String name;
	
	private FriendStatus(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}