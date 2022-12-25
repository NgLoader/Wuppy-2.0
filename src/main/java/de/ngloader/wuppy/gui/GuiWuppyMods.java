package de.ngloader.wuppy.gui;

import java.io.IOException;

import de.ngloader.wuppy.gui.elements.WuppyButton;
import de.ngloader.wuppy.gui.elements.WuppyScreen;
import de.ngloader.wuppy.modmanager.ModList;
import de.ngloader.wuppy.modmanager.ModManager;
import de.ngloader.wuppy.modmanager.mods.Mod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiWuppyMods extends WuppyScreen {

	private final GuiScreen lastScreen;

	public GuiWuppyMods(GuiScreen lastScreen, boolean fancyBackground) {
		this.lastScreen = lastScreen;
		this.drawFancyBackground = fancyBackground;
	}

	public void initGui() {
		setTitle("Wuppy - Settings - Mods");

		int j = this.height / 6;

		int i = 0;
		ModList.getMods().forEach(mod -> this.buttonList.add(new WuppyButton(i + 1, this.width / 24, j + (24 * i), 100, 20, mod.name, mod)));

		this.buttonList.add(new WuppyButton(0, this.width - 105, this.height - 25, 100, 20, "Back"));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0:
			this.mc.displayGuiScreen(lastScreen);
			break;

		default:
			Mod mod = ((WuppyButton)button).mod;

			if(mod == null)
				break;

			if (mod.activ)
				ModManager.INSTANCE.disableMod(mod);
			else
				ModManager.INSTANCE.enableMod(mod);
			break;
		}
	}

	public WuppyButton createModButton(WuppyButton button, Mod mod) {
		button.customR = mod.activ ? 1 : 0;
		button.customG = mod.activ ? 0 : 1;
		button.customB = mod.activ ? 0 : 0;
		button.customA = 1;
		button.customColor = true;
		return button;
	}
}