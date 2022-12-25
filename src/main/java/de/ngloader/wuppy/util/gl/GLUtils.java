package de.ngloader.wuppy.util.gl;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glRotated;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import net.minecraft.client.renderer.GlStateManager;
import pw.knx.feather.tessellate.GrowingTess;
import pw.knx.feather.tessellate.OffsetTess;

/**
 * Utility class to assist in rendering.
 * */
public final class GLUtils {

	private static final Random random = new Random();

	private static final OffsetTess tess = new OffsetTess(new GrowingTess(4));
	
	/**
	 * Renders a simple lined border around the given x, y, x1, and y1 coordinates. 
	 * */
	public static void drawBorder(float size, float x, float y, float x1, float y1) {
		glEnableClientState(GL_VERTEX_ARRAY);
		glLineWidth(size);
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	tess.reset().vertex(x, y, 0F).vertex(x, y1, 0F).vertex(x1, y1, 0F).vertex(x1, y, 0F).bind().pass(GL_LINE_LOOP);
		GlStateManager.enableTexture2D();
		glDisableClientState(GL_VERTEX_ARRAY);
	}

	/**
	 * Renders a line from the given x, y positions to the second x1, y1 positions.
	 * */
	public static void drawLine(float size, float x, float y, float x1, float y1) {
		glLineWidth(size);
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		glEnableClientState(GL_VERTEX_ARRAY);
		tess.reset().vertex(x, y, 0F).vertex(x1, y1, 0F).bind().pass(GL_LINES);
		glDisableClientState(GL_VERTEX_ARRAY);
    	GlStateManager.enableTexture2D();
	}

	/**
	 * Renders a simple rectangle around the given x, y, width, and height values within the rect array.
	 * */
	public static void drawRect(int[] rect) {
		drawRect(rect[0], rect[1], rect[0] + rect[2], rect[1] + rect[3]);
	}

	/**
	 * Renders a simple rectangle around the given x, y, width, and height values within the rect array.
	 * */
	public static void drawRect(float[] rect) {
		drawRect(rect[0], rect[1], rect[0] + rect[2], rect[1] + rect[3]);
	}

	/**
	 * Renders a simple rectangle around the given x, y, x1, and y1 coordinates.
	 * */
	public static void drawRect(float x, float y, float x1, float y1) {
		glEnableClientState(GL_VERTEX_ARRAY);
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	tess.reset().vertex(x, y1, 0F).vertex(x1, y1, 0F).vertex(x1, y, 0F).vertex(x, y, 0F).bind().pass(GL_QUADS);
    	GlStateManager.enableTexture2D();
		glDisableClientState(GL_VERTEX_ARRAY);
	}

	/**
	 * Renders a simple rectangle and line border around the given x, y, x1, and y1 coordinates.
	 * */
	public static void drawBorderRect(float x, float y, float x1, float y1, float borderSize) {
		drawBorder(borderSize, x, y, x1, y1);
		drawRect(x, y, x1, y1);
	}
	
	/**
	 * @return A {@link Color} object with effects applied to it based on the boolean values given.
	 * */
	public static Color getColorWithAffects(Color color, boolean mouseOver, boolean mouseDown) {
    	return mouseOver ? (mouseDown ? color.darker() : color.brighter()) : color;
    }

	/**
	 * @return A hexadecimal color with effects applied to it based on the boolean values given.
	 * */
	public static int getColorWithAffects(int color, boolean mouseOver, boolean mouseDown) {
    	return mouseOver ? (mouseDown ? darker(color, 0.2F) : brighter(color, 0.2F)) : color;
    }

    /**
     * @return A hexadecimal color that is darkened by the scale amount provided.
     */
	public static int darker(int color, float scale) {
		int red = (color >> 16 & 255), green =  (color >> 8 & 255), blue = (color & 255), alpha = (color >> 24 & 0xff);
		red = (int) (red - red * scale);
		red = Math.min(red, 255);
		green = (int) (green - green * scale);
		green = Math.min(green, 255);
		blue = (int) (blue - blue * scale);
		blue = Math.min(blue, 255);
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}

	/**
     * @return A hexadecimal color that is brightened by the scale amount provided.
     * */
	public static int brighter(int color, float scale) {
		int red = (color >> 16 & 255), green =  (color >> 8 & 255), blue = (color & 255), alpha = (color >> 24 & 0xff);
		red = (int) (red + red * scale);
		red = Math.min(red, 255);
		green = (int) (green + green * scale);
		green = Math.min(green, 255);
		blue = (int) (blue + blue * scale);
		blue = Math.min(blue, 255);
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}

	/**
     * @return A hexadecimal representation of the rgba color values specified (between 0 - 1)
     * */
	public static int toColor(float r, float g, float b, float a) {
		return ((int) (a * 255F) << 24) | ((int) (r * 255F) << 16) | ((int) (g * 255F) << 8) | (int) (b * 255F);
	}

	public static void glColor(float red, float green, float blue, float alpha) {
		GlStateManager.color(red, green, blue, alpha);
	}

	public static void glColor(Color color) {
		GlStateManager.color((float) color.getRed() / 255F, (float) color.getGreen() / 255F, (float) color.getBlue() / 255F, (float) color.getAlpha() / 255F);
	}

	public static void glColor(Color color, float alpha) {
		GlStateManager.color((float) color.getRed() / 255F, (float) color.getGreen() / 255F, (float) color.getBlue() / 255F, alpha);
	}

	public static void glColor(int color) {
		GlStateManager.color((float) (color >> 16 & 255) / 255F, (float) (color >> 8 & 255) / 255F, (float) (color & 255) / 255F, (color >> 24 & 0xff) / 255F);
	}

	public static void glColor(int color, float alpha) {
		GlStateManager.color((float) (color >> 16 & 255) / 255F, (float) (color >> 8 & 255) / 255F, (float) (color & 255) / 255F, alpha);
	}

	/**
	 * This isn't mine, but it's the most beautiful random color generator I've seen. Reminds me of easter.
	 * */
	public static Color getRandomColor(int saturationRandom, float luminance) {
		final float hue = random.nextFloat();
		final float saturation = (random.nextInt(saturationRandom) + (float) saturationRandom) / (float) saturationRandom + (float) saturationRandom;
		return Color.getHSBColor(hue, saturation, luminance);
	}

	public static Color getHSBColor(float hue, float saturation, float luminance) {
		return Color.getHSBColor(hue, saturation, luminance);
	}

	public static Color getRandomColor() {
		return getRandomColor(1000, 0.6F);
	}

	/**
	 * @return Newly generated lwjgl vbo id.
	 * */
	public static int genVbo() {
		int id = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
		return id;
	}

	/**
	 * @return Newly generated lwjgl texture id.
	 * */
	public static int genTexture() {
		int textureId = GL11.glGenTextures();
		return textureId;
	}

	/**
	 * @param filter determines how the texture will interpolate when scaling up / down. <br>
	 * GL_LINEAR - smoothest <br> GL_NEAREST - most accurate <br>
	 * @param wrap determines how the UV coordinates outside of the 0.0F ~ 1.0F range will be handled. <br>
	 * GL_CLAMP_TO_EDGE - samples edge color <br> GL_REPEAT - repeats the texture <br>
	 * */
	public static int applyTexture(int texId, File file, int filter, int wrap) throws IOException {
		applyTexture(texId, ImageIO.read(file), filter, wrap);
		return texId;
	}

	/**
	 * @param filter determines how the texture will interpolate when scaling up / down. <br>
	 * GL_LINEAR - smoothest <br> GL_NEAREST - most accurate <br>
	 * @param wrap determines how the UV coordinates outside of the 0.0F ~ 1.0F range will be handled. <br>
	 * GL_CLAMP_TO_EDGE - samples edge color <br> GL_REPEAT - repeats the texture <br>
	 * */
	public static int applyTexture(int texId, BufferedImage image, int filter, int wrap) {
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

		for(int y = 0; y < image.getHeight(); y++){
			for(int x = 0; x < image.getWidth(); x++){
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}

		buffer.flip();
		applyTexture(texId, image.getWidth(), image.getHeight(), buffer, filter, wrap);
		return texId;
	}

	/**
	 * @param filter determines how the texture will interpolate when scaling up / down. <br>
	 * GL_LINEAR - smoothest <br> GL_NEAREST - most accurate <br>
	 * @param wrap determines how the UV coordinates outside of the 0.0F ~ 1.0F range will be handled. <br>
	 * GL_CLAMP_TO_EDGE - samples edge color <br> GL_REPEAT - repeats the texture <br>
	 * */
	public static int applyTexture(int texId, int width, int height, ByteBuffer pixels, int filter, int wrap) {
		glBindTexture(GL_TEXTURE_2D, texId);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, filter);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrap);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrap);
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
		glBindTexture(GL_TEXTURE_2D, 0);
		return texId;
	}

}
