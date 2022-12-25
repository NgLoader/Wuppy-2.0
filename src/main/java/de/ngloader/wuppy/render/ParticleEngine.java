package de.ngloader.wuppy.render;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.ngloader.wuppy.Wuppy;
import de.ngloader.wuppy.util.gl.GLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ParticleEngine {
	
	private static final ResourceLocation RESOURCE_LOCATION_PARTICLES = new ResourceLocation(Wuppy.ASSETS_LOCATION + "textures/gui/particles.png");
	
	private final Random random = Wuppy.RANDOM;
	private final List<Particle> particles = new ArrayList<>();
	private final boolean randomDespawn;
	
	public ParticleEngine() {
		this(false);
	}
	
	public ParticleEngine(boolean randomDespawn) {
		this.randomDespawn = randomDespawn;
	}
	
	public void render() {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.enableTexture2D();
		
		for(Particle particle : particles) {
			particle.applyPhysics();
			
			GlStateManager.pushMatrix();
			
			Minecraft.getMinecraft().getTextureManager().bindTexture(RESOURCE_LOCATION_PARTICLES);
			
			float scale = ((particle.life) / (getMaxLive())) / 2;
			
			glScalef(scale, scale, scale);
			
			GLUtils.glColor(0.4F, 0.4F, 0.9F, ((particle.life) / (getMaxLive())) / 2);
			renderTexture(320, 32, particle.x * (1F / scale), particle.y * (1F / scale), 32, 32, 0, 0, 32, 32);
			
			GlStateManager.popMatrix();
		}
	}
	
	private void renderTexture(int textureWidth, int textureHeight, float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
		float renderSRCX = srcX / textureWidth;
		float renderSRCY = srcY / textureHeight;
		float renderSRCWidth = srcWidth / textureWidth;
		float renderSRCHeight = srcHeight /  textureHeight;
		
		glBegin(GL_TRIANGLES);
		
		glTexCoord2f(renderSRCY + renderSRCWidth, renderSRCY);
		glVertex2d(x + width, y);
		
		glTexCoord2f(renderSRCX, renderSRCY);
		glVertex2d(x, y);
		
		glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		glVertex2d(x, y + height);
		
		glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
		glVertex2d(x, y + height);
		
		glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
		glVertex2d(x + width, y + height);
		
		glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
		glVertex2d(x + width, y);
		glEnd();
	}
	
	public void updateParticles() {
		for(int i = 0; i < particles.size(); i++) {
			Particle particle = particles.get(i);
			particle.update();
			if(particle.life < 0)
				particles.remove(particle);
		}
	}
	
	public void spawnParticles(int spawnX, int spawnY, int dispurseX, int dispurseY, float velocity, float gravity) {
		int startX = spawnX + random.nextInt(dispurseX);
		int startY = spawnY + random.nextInt(dispurseY);
		
		Particle particle = new Particle(startX, startY, velocity, gravity);
		particles.add(particle);
	}
	
	private class Particle {
		private float life;
		private float x;
		private float y;
		private float montionX;
		private float montionY;
		private float gravity;
		
		public Particle(float x, float y, float velocity, float gravity) {
			this.x = x;
			this.y = y;
			this.montionX = random.nextFloat() * velocity;
			this.montionY = random.nextFloat() * velocity;
			
			if(random.nextBoolean())
				montionX = -montionX;
			if(random.nextBoolean())
				montionY = -montionY;
			
			this.life = getMaxLive();
			this.gravity = gravity;
		}
		
		private void applyPhysics() {
			x += montionX * 0.1F;
			y += montionY * 0.01F;
			montionX *= 0.99F;
			montionY *= 0.99F;
			y += gravity * 0.1F;
		}
		
		private void update() {
			if(randomDespawn) {
				if(random.nextBoolean())
					life -= random.nextFloat() * 2;
			} else
				life -= random.nextFloat() * 2;
		}
	}
	
	public int getMaxLive() {
		return 50;
	}
}