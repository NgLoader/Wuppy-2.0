package de.ngloader.wuppy.modmanager;

import java.util.LinkedList;
import java.util.List;

import de.ngloader.wuppy.modmanager.mods.Mod;
import de.ngloader.wuppy.modmanager.mods.hypixel.MurderMystery;

public class ModList {
	
	public static Mod MURDERMYSTERY;

	private static final List<Mod> mods = new LinkedList<>();

	public static void registerAllMods() {
		ModManager modManager = ModManager.INSTANCE;
		
		mods.add(MURDERMYSTERY = new MurderMystery(modManager));
	}

	public static List<Mod> getMods() {
		return mods;
	}
}