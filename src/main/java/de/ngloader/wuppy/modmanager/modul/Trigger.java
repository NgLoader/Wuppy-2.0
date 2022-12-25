package de.ngloader.wuppy.modmanager.modul;

import de.ngloader.wuppy.modmanager.mods.Mod;
import net.minecraft.client.Minecraft;

public abstract class Trigger {
	
	protected Minecraft mc = Minecraft.getMinecraft();
	
	public abstract boolean isTriggered();
	public abstract Mod getMod();
}