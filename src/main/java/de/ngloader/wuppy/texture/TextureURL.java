package de.ngloader.wuppy.texture;

import java.awt.image.BufferedImage;
import java.io.File;

import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

import de.ngloader.wuppy.Wuppy;
import de.ngloader.wuppy.util.LoadTextureURL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;

public class TextureURL implements ITexture {
	
	static final ResourceLocation transparentTexture = new ResourceLocation(Wuppy.ASSETS_LOCATION + "textures/gui/transparent.png");
	
	public File url;
	public int textureId;
	
	private BufferedImage bufferedImage;
	
	public TextureURL(String url) {
		this.textureId = -1;
		
		try {
			this.url = new File(url);
		} catch(Exception ex) {
			ex.printStackTrace();
			Wuppy.LOGGER.log(Level.ERROR, "Invalid URL: " + url);
		}
		new LoadTextureURL(this).start();
	}
	
	@Override
	public void bind() {
		if(this.textureId != -1)
			GlStateManager.bindTexture(textureId);
		else {
			if(this.bufferedImage != null) {
				this.textureId = TextureUtil.uploadTextureImageAllocate(GL11.glGenTextures(), this.bufferedImage, false, false);
				bind();
				return;
			}
			Minecraft.getMinecraft().renderEngine.bindTexture(transparentTexture);
		}
	}
	
	public void finishLoading(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}
	
	public void setTextureID(int textureId) {
		this.textureId = textureId;
	}
	
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}
	
	public int getTextureId() {
		return textureId;
	}
	
	public File getUrl() {
		return url;
	}
}