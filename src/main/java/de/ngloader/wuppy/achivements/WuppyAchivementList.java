package de.ngloader.wuppy.achivements;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.IStatType;
import net.minecraft.stats.StatBasic;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.TextComponentString;

public class WuppyAchivementList {
	
	public static StatBasic test = new StatBasic("achivement.test", new TextComponentString("TEST"));
	
	public static void registerAllAchievements() {
		test.registerStat();
	}
	
	public static void displayAchievement() {
		Minecraft.getMinecraft().player.addStat(test);
	}
}