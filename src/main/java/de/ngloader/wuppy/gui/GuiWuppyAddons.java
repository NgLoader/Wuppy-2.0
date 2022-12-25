package de.ngloader.wuppy.gui;

import java.io.IOException;

import de.ngloader.wuppy.gui.elements.WuppyButton;
import de.ngloader.wuppy.gui.elements.WuppyScreen;
import de.ngloader.wuppy.modmanager.addons.AddonPictureInPicture;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiWuppyAddons extends WuppyScreen {

	private final GuiScreen lastScreen;

	public GuiWuppyAddons(GuiScreen lastScreen, boolean fancyBackground) {
		this.lastScreen = lastScreen;
		this.drawFancyBackground = fancyBackground;
	}

	public void initGui() {
		setTitle("Wuppy - Settings - Addons");

		int j = this.height / 6;

        this.buttonList.add(new WuppyButton(0, this.width / 24, j, 100, 20, "PiP", AddonPictureInPicture.ADDON_PICTURE_IN_PICTURE));

		this.buttonList.add(new WuppyButton(1, this.width - 105, this.height - 25, 100, 20, "Back"));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0:
			if(AddonPictureInPicture.ADDON_PICTURE_IN_PICTURE.isAvavible())
				AddonPictureInPicture.ADDON_PICTURE_IN_PICTURE.activ = !AddonPictureInPicture.ADDON_PICTURE_IN_PICTURE.activ;
			break;

		case 1:
			this.mc.displayGuiScreen(lastScreen);
			break;

		default:
			break;
		}
	}
}