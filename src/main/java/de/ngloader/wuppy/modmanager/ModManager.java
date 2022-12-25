package de.ngloader.wuppy.modmanager;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.Sys;

import de.ngloader.wuppy.modmanager.mods.Mod;
import de.ngloader.wuppy.modmanager.modul.Trigger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public class ModManager {
	
	public static ModManager INSTANCE;
	
	public final Minecraft mc;
	
	public final List<Trigger> triggers;

	private Long lastUpdate = 0L;

	public ModManager(Minecraft mc) {
		INSTANCE = this;
		
		this.mc = mc;
		triggers = new LinkedList<>();
		
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
		
		ModList.registerAllMods();
	}
	
	@SubscribeEvent
	public void onTick(TickEvent event) {
		if(event.type != Type.CLIENT || triggers.isEmpty() || lastUpdate > System.currentTimeMillis())
			return;

		lastUpdate = System.currentTimeMillis() + 1000;

		for(Trigger trigger : triggers)
			trigger.getMod().onTrigger(trigger, false);
	}
	
	public void enableMod(Mod mod) {
		if(mod.activ)
			return;
		MinecraftForge.EVENT_BUS.register(mod);
		FMLCommonHandler.instance().bus().register(mod);
		
		mod.onEnable();
		mod.activ = true;
	}
	
	public void disableMod(Mod mod) {
		if(!mod.activ)
			return;
		mod.activ = false;
		mod.onDisable();
		
		MinecraftForge.EVENT_BUS.unregister(mod);
		FMLCommonHandler.instance().bus().unregister(mod);
	}
}