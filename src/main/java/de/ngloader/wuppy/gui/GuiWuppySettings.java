package de.ngloader.wuppy.gui;

import java.io.IOException;

import de.ngloader.wuppy.gui.elements.WuppyButton;
import de.ngloader.wuppy.gui.elements.WuppyScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;

public class GuiWuppySettings extends WuppyScreen {
	
	private final GuiScreen lastScreen;
	
	public GuiWuppySettings(GuiScreen lastScreen, boolean fancyBackground) {
		this.lastScreen = lastScreen;
		this.drawFancyBackground = fancyBackground;
	}
	
	public void initGui() {
        setTitle("Wuppy - Settings");
        
        int j = this.height / 6;

        this.buttonList.add(new WuppyButton(0, this.width / 24, j, 100, 20, "Mods"));
        this.buttonList.add(new WuppyButton(1, this.width / 24, j += 24, 100, 20, "Addons"));
        this.buttonList.add(new WuppyButton(2, this.width / 24, j += 48, 100, 20, "Game Settings"));
        this.buttonList.add(new WuppyButton(3, this.width - 105, this.height - 25, 100, 20, "Back"));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		switch(button.id) {
		case 0:
			this.mc.displayGuiScreen(new GuiWuppyMods(this, this.drawFancyBackground));
			break;

		case 1:
			this.mc.displayGuiScreen(new GuiWuppyAddons(this, this.drawFancyBackground));
			break;

		case 2:
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
			break;

		case 3:
			this.mc.displayGuiScreen(lastScreen);
			break;

		default:
			break;
		}
	}
}