package de.ngloader.wuppy.gui;

import java.io.IOException;

import de.ngloader.wuppy.gui.elements.WuppyButton;
import de.ngloader.wuppy.gui.elements.WuppyScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiWuppyMultiplayer extends WuppyScreen {
	
	private final GuiScreen lastScreen;
	
	public GuiWuppyMultiplayer(GuiScreen lastScreen, boolean fancyBackground) {
		this.lastScreen = lastScreen;
		this.drawFancyBackground = fancyBackground;
	}
	
	public void initGui() {
        setTitle("Wuppy - Multiplayer");
        
		int j = this.height / 6;
        
        this.buttonList.add(new WuppyButton(1, this.width - 105, this.height - 25, 100, 20, "Back"));
	}
	
	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id) {
		case 0:
			break;
		case 1:
			this.mc.displayGuiScreen(lastScreen);
			break;
		default:
			break;
		}
	}
}