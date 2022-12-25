package de.ngloader.wuppy.gui.elements;

import de.ngloader.wuppy.gui.panorama.PanoramaGlobal;
import de.ngloader.wuppy.gui.panorama.PanoramaRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class WuppyScreen extends GuiScreen {
	
	private PanoramaRenderer panoramaRenderer;
	
	public String title = "Wuppy Alpha";

	public boolean drawFancyBackground = true;
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDefaultTitle() {
		this.title = "Wuppy Alpha";
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(drawFancyBackground) {
			if(panoramaRenderer != null)
				panoramaRenderer.renderSkybox(partialTicks);
			else
				panoramaRenderer = PanoramaGlobal.getPanoramaRenderer(width, height);
			
			this.drawCenteredString(this.fontRenderer, title, this.width  / 2, 15, 16777215);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void updateScreen() {
		if(drawFancyBackground) {
			if(panoramaRenderer != null)
				panoramaRenderer.panoramaTick();
			else
				panoramaRenderer = PanoramaGlobal.getPanoramaRenderer(width, height);
		}
		super.updateScreen();
	}
	
	@Override
	public void onResize(Minecraft mcIn, int w, int h) {
		if(drawFancyBackground) {
			if(panoramaRenderer != null)
				panoramaRenderer.updateSize(w, h);
			else
				panoramaRenderer = PanoramaGlobal.getPanoramaRenderer(w, h);
		}
		super.onResize(mcIn, w, h);
	}
}