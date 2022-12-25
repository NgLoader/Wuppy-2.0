package de.ngloader.wuppy.gui;

import java.io.IOException;

import de.ngloader.wuppy.gui.elements.WuppyButton;
import de.ngloader.wuppy.gui.elements.WuppyScreen;
import de.ngloader.wuppy.handler.GlobalEventHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.realms.RealmsBridge;
import net.minecraftforge.fml.client.GuiModList;

public class GuiWuppy extends WuppyScreen {
	public void initGui() {
		setDefaultTitle();

		int j = this.height / 6;

		this.buttonList.add(new WuppyButton(0, this.width / 24, j, "Minechat"));

		this.buttonList.add(new WuppyButton(1, this.width / 24, j + 24 * 2, 98, 20, "Singleplayer"));
		this.buttonList.add(new WuppyButton(2, this.width / 24 + 102, j + 24 * 2, 98, 20, "Multiplayer"));

		this.buttonList.add(new WuppyButton(3, this.width / 24, j + 24 * 3, 200, 20, "Realms"));

		this.buttonList.add(new WuppyButton(4, this.width / 24, j + 24 * 5, 98, 20, "Mods"));
		this.buttonList.add(new WuppyButton(5, this.width / 24 + 102, j + 24 * 5, 98, 20, "Forge Mods"));

		this.buttonList.add(new WuppyButton(6, this.width / 24, j + 24 * 6, 200, 20, "Settings"));

		this.buttonList.add(new WuppyButton(7, this.width - 105, this.height - 25, 100, 20, "Beenden"));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0: // Minechat
			GlobalEventHandler.guiWuppy = new GuiWuppy();
			this.mc.displayGuiScreen(GlobalEventHandler.guiWuppy);
			break;

		case 1: // Singleplayer
			this.mc.displayGuiScreen(new GuiWorldSelection(this));
			break;

		case 2: // Multiplayer
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
			break;

		case 3: // Realms
			this.switchToRealms();
			break;

		case 4: // Mods
			this.mc.displayGuiScreen(new GuiWuppyMultiplayer(this, true));
			// this.mc.displayGuiScreen(new GuiWuppy()); //TESTING
			break;

		case 5: // Forge mods
			this.mc.displayGuiScreen(new GuiModList(this));
			break;

		case 6: // Settings
			this.mc.displayGuiScreen(new GuiWuppySettings(this, true));
			break;

		case 7: // Shutdown
			this.mc.shutdown();
			break;

		default:
			break;
		}
	}

	private void switchToRealms() {
		RealmsBridge realmsbridge = new RealmsBridge();
		realmsbridge.switchToRealms(this);
	}
}