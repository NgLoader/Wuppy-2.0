package de.ngloader.wuppy.util;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;

public class ResourcePath {
	
	private final String domain;
	private final String path;
	
	public ResourcePath(String domain, String path) {
		this.domain = domain;
		this.path = path;
	}
	
	public ResourcePath(String path) {
		this("minecraft", path);
	}
	
	public String getPath() {
		return path;
	}
	
	public String getDomain() {
		return domain;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		else if(!(obj instanceof ResourcePath))
			return false;
		else {
			ResourcePath assetLocation = (ResourcePath)obj;
			return this.getPath().equals(assetLocation.getPath()) && this.getDomain().equals(assetLocation.getDomain());
		}
	}
	
	@Override
	public String toString() {
		return domain + ":" + path;
	}
}