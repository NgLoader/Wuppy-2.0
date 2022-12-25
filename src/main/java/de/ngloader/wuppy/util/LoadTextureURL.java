package de.ngloader.wuppy.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.ngloader.wuppy.texture.TextureURL;

public class LoadTextureURL extends Thread {
	
	TextureURL textureURL;
	
	public LoadTextureURL(TextureURL textureURL) {
		this.textureURL = textureURL;
		
		setDaemon(true);
	}
	
	@Override
	public void run() {
		BufferedImage bufferedImage = null;
		
		try {
			bufferedImage = ImageIO.read(this.textureURL.getUrl());
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		if(bufferedImage != null)
			this.textureURL.finishLoading(bufferedImage);
	}
}