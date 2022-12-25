package de.ngloader.wuppy.modmanager.mods;

import java.util.LinkedList;
import java.util.List;

import de.ngloader.wuppy.Wuppy;
import de.ngloader.wuppy.modmanager.ModManager;
import de.ngloader.wuppy.modmanager.enums.DectectType;
import de.ngloader.wuppy.modmanager.enums.ModCategory;
import de.ngloader.wuppy.modmanager.modul.Trigger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public abstract class Mod {

	public final ModManager modManager;
	public final Minecraft mc;

	public final String name;
	public final String description;

	public final ModCategory category;
	public final DectectType dectectType;

	public final List<Trigger> privateTriggers;

	public boolean activ;

	private long lastUpdate;

	public Mod(ModManager modManager, String name, String description, ModCategory category, DectectType dectectType) {
		this.modManager = modManager;
		this.mc = modManager.mc;
		this.name = name;
		this.description = description;
		this.category = category;
		this.dectectType = dectectType;
		this.privateTriggers = new LinkedList<>();

		onInit();
	}

	@SubscribeEvent
	public void onClientTick(TickEvent event) {
		if(event.type != Type.CLIENT || privateTriggers.isEmpty() || lastUpdate > System.currentTimeMillis())
			return;
		lastUpdate = System.currentTimeMillis() + 1000;

		for(Trigger trigger : privateTriggers)
			if(activ)
				onTrigger(trigger, true);
	}
	
	public void sendChatMessage(String message, boolean small) {
		if(small) {
			mc.player.sendMessage(new TextComponentString(Wuppy.PREFIX + TextFormatting.GREEN + message));
			return;
		}
		
		mc.player.sendMessage(new TextComponentString(Wuppy.PREFIX + TextFormatting.GRAY + "----------------------------------"));
		mc.player.sendMessage(new TextComponentString(Wuppy.PREFIX + TextFormatting.GRAY));

		mc.player.sendMessage(new TextComponentString(Wuppy.PREFIX + TextFormatting.GREEN + message));

		mc.player.sendMessage(new TextComponentString(Wuppy.PREFIX + TextFormatting.GRAY));
		mc.player.sendMessage(new TextComponentString(Wuppy.PREFIX + TextFormatting.GRAY + "----------------------------------"));
	}
	
	public void addPrivateTrigger(Trigger trigger) {
		if(!privateTriggers.contains(trigger))
			privateTriggers.add(trigger);
	}
	
	public void removePrivateTrigger(Trigger trigger) {
		if(privateTriggers.contains(trigger))
			privateTriggers.remove(trigger);
	}
	
	public void addTrigger(Trigger trigger) {
		if(!modManager.triggers.contains(trigger))
			modManager.triggers.add(trigger);
	}
	
	public void removeTrigger(Trigger trigger) {
		if(modManager.triggers.contains(trigger))
			modManager.triggers.remove(trigger);
	}
	
	public abstract void onInit();
	public abstract void onEnable();
	public abstract void onDisable();
	public abstract void onTrigger(Trigger trigger, boolean privateTrigger);
}