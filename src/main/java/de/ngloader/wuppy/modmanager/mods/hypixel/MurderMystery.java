package de.ngloader.wuppy.modmanager.mods.hypixel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import de.ngloader.wuppy.Wuppy;
import de.ngloader.wuppy.modmanager.ModManager;
import de.ngloader.wuppy.modmanager.addons.AddonPictureInPicture;
import de.ngloader.wuppy.modmanager.enums.DectectType;
import de.ngloader.wuppy.modmanager.enums.ModCategory;
import de.ngloader.wuppy.modmanager.mods.Mod;
import de.ngloader.wuppy.modmanager.mods.hypixel.MurderMystery.ClassType;
import de.ngloader.wuppy.modmanager.mods.hypixel.MurderMystery.CustomPlayer;
import de.ngloader.wuppy.modmanager.modul.Trigger;
import de.ngloader.wuppy.modmanager.modul.triggers.TriggerScoreboard;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team.EnumVisible;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import scala.swing.TextComponent;

public class MurderMystery extends Mod {
	
	private boolean enabled;
	
	private HashMap<UUID, CustomPlayer> players;
	private List<String> goldDrops, detectivBows;
	
	private List<ScorePlayerTeam> createdTeams;
	
	private Item[] murderItems;
	
	private long startTime;
	
	public MurderMystery(ModManager modManager) {
		super(modManager, "MurderMystery", "murdermystery.description", ModCategory.HYPIXEL, DectectType.UNDETECTABLE);
	}
	
	@Override
	public void onInit() {
		enabled = false;

		players = new HashMap<>();
		goldDrops = new LinkedList<>();
		detectivBows = new LinkedList<>();
		createdTeams = new LinkedList<>();

		murderItems = new Item[] {
				Items.IRON_SWORD,		Items.STONE_SWORD,			Items.IRON_SHOVEL,		Items.STICK,
				Items.STONE_SHOVEL,		Items.BLAZE_ROD,			Items.DIAMOND_SHOVEL,	Items.CARROT_ON_A_STICK,
				Items.BONE,				Items.GOLDEN_SWORD,			Items.CARROT,			Items.GOLDEN_SWORD,
				Items.DIAMOND_SWORD,	Items.DIAMOND_HOE
			};

		addPrivateTrigger(new TriggerScoreboard(this, "MurderMystery", false));
	}

	@Override
	public void onEnable() {
		reset();
	}

	@Override
	public void onDisable() {
		reset();
	}

	@Override
	public void onTrigger(Trigger trigger, boolean privateTrigger) {
		if(trigger.isTriggered() && !enabled) {
			enabled = true;
			reset();
			
			startTime = System.currentTimeMillis() + 35000;
		} else if(!trigger.isTriggered() && enabled) {
			enabled = false;
			
			reset();
			
			startTime = -1;
		}
	}
	
	public void reset() {
		players.clear();
		goldDrops.clear();
		detectivBows.clear();
		
		AddonPictureInPicture.ADDON_PICTURE_IN_PICTURE.resetPlayers();
		
		try {
			createdTeams.forEach(team -> {

				if(mc.world == null || mc.world.loadedEntityList == null)
					return;

				mc.world.loadedEntityList.forEach(entity -> {
					if(team.getMembershipCollection().contains(entity instanceof EntityPlayer ? entity.getName() : entity.getCachedUniqueIdString()))
						entity.setGlowing(false);
				});

				mc.world.getScoreboard().removeTeam(team);
			});
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		createdTeams.clear();
	}
	
	@SubscribeEvent
	public void onTick(TickEvent event) {
		if(event.type != Type.CLIENT || mc.player == null || !enabled || mc.player.isDead)
			return;
		checkWorld();
	}
	
	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent event) { //BEBUG
//		if(mc.isSingleplayer())
//			if(event.getMessage().getFormattedText().contains("add"))
//				mc.world.getScoreboard().addScoreObjective("MurderMystery", IScoreCriteria.DUMMY);
//			else if(event.getMessage().getFormattedText().contains("remove"))
//				mc.world.getScoreboard().removeObjective(mc.world.getScoreboard().getObjective("MurderMystery"));
	}
	
	private void checkWorld() {
		Scoreboard scoreboard = mc.player.getWorldScoreboard();
		if(scoreboard == null)
			return;

		try {
			mc.world.getLoadedEntityList().forEach(entity -> {
				if(entity != null) {
					if(entity instanceof EntityItem) {
						EntityItem entityItem = (EntityItem)entity;

						if(entityItem.getItem().getItem() == Items.GOLD_INGOT)
							addGoldDrop(scoreboard, entityItem);
					} else if(entity instanceof EntityArmorStand) {
						EntityArmorStand entityArmorStand = (EntityArmorStand)entity;
						
						if(entityArmorStand.isInvisible() && (entityArmorStand.getHeldItemOffhand().getItem() == Items.ARROW || entityArmorStand.getHeldItemMainhand().getItem() == Items.ARROW || entityArmorStand.getHeldItemOffhand().getItem() == Items.BOW || entityArmorStand.getHeldItemMainhand().getItem() == Items.BOW) && !detectivBows.contains(entity.getCachedUniqueIdString()))
							addDectivBow(scoreboard, entityArmorStand);
					} else if(entity instanceof EntityPlayer) {
						EntityPlayer entityPlayer = (EntityPlayer)entity;
						UUID uuid = UUID.fromString(entityPlayer.getUUID(entityPlayer.getGameProfile()).toString());
						
						CustomPlayer customPlayer;
						
						if(!players.containsKey(uuid))
							players.put(uuid, customPlayer = new CustomPlayer(uuid, ClassType.INNOCENT));
						else
							customPlayer = players.get(uuid);
						
						if(entityPlayer.isInvisible() && entityPlayer.getActivePotionEffect(MobEffects.INVISIBILITY) != null && entityPlayer.getActivePotionEffect(MobEffects.INVISIBILITY).getDuration() > 8000)
							customPlayer.setType(ClassType.SPECTATOR);
						else if(entityPlayer.isPlayerSleeping())
							customPlayer.setType(ClassType.DEAD);
						else if(hasItemInhand(entityPlayer)) {
							customPlayer.setType(ClassType.MURDER);
						} else if(hasItemInHand(entityPlayer, Items.BOW) || hasItemInHand(entityPlayer, Items.ARROW)) {
							if(!customPlayer.hasBow)
								if(customPlayer.getType() != ClassType.MURDER)
									if(startTime > System.currentTimeMillis())
										customPlayer.setType(ClassType.DETECTIV);
									else
										customPlayer.setType(ClassType.INNOCENTWITHBOW);
							customPlayer.setHasBow(true);
						}
						if(!uuid.equals(mc.player.getUUID(mc.player.getGameProfile())))
							customPlayer.updatePlayer(entityPlayer, scoreboard);
					}
				}
			});
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void addGoldDrop(Scoreboard scoreboard, EntityItem entityItem) {
		ScorePlayerTeam team = scoreboard.getTeam("WUPPY_GOLD");
		
		if(team == null) {
			team = scoreboard.createTeam("WUPPY_GOLD");
			
			createdTeams.add(team);
			
			team.setColor(TextFormatting.GOLD);
			team.setPrefix(TextFormatting.GOLD.toString());
			team.setSuffix(TextFormatting.RESET.toString());
			team.setNameTagVisibility(EnumVisible.NEVER);
		}
		
		if(!goldDrops.contains(entityItem.getCachedUniqueIdString()) || !entityItem.isOnScoreboardTeam(team) || !entityItem.isGlowing()) {
			entityItem.setGlowing(true);
			goldDrops.add(entityItem.getCachedUniqueIdString());
			scoreboard.addPlayerToTeam(entityItem.getCachedUniqueIdString(), "WUPPY_GOLD");
		}
	}
	
	private void addDectivBow(Scoreboard scoreboard, EntityArmorStand entityArmorStand) {
		ScorePlayerTeam team = scoreboard.getTeam("WUPPY_BOW");
		
		if(team == null) {
			team = scoreboard.createTeam("WUPPY_BOW");
			
			createdTeams.add(team);
			
			team.setColor(TextFormatting.AQUA);
			team.setPrefix(TextFormatting.AQUA.toString());
			team.setSuffix(TextFormatting.RESET.toString());
			team.setNameTagVisibility(EnumVisible.NEVER);
		}
		
		if(!detectivBows.contains(entityArmorStand.getCachedUniqueIdString())) {
			entityArmorStand.setGlowing(true);
			detectivBows.add(entityArmorStand.getCachedUniqueIdString());
			scoreboard.addPlayerToTeam(entityArmorStand.getCachedUniqueIdString(), "WUPPY_BOW");
		}
	}
	
	enum ClassType {
		MURDER,
		DETECTIV,
		INNOCENTWITHBOW,
		INNOCENT,
		SPECTATOR,
		DEAD;
	}
	
	class CustomPlayer {
		
		private UUID uuid;
		private ClassType type;
		private ScorePlayerTeam team;
		
		private boolean hasBow;
		private boolean hasNewType, hasNewBow;
		
		public CustomPlayer(UUID uuid, ClassType type) {
			this.uuid = uuid;
			this.type = type;
			this.hasBow = false;
			this.hasNewType = hasNewBow = false;
		}
		
		public void updatePlayer(EntityPlayer entityPlayer, Scoreboard scoreboard) {
			if(team == null || team.getMembershipCollection().size() > 1) {
				team = mc.world.getScoreboard().getTeam("WUPPY_" + entityPlayer.getEntityId());
				if(team != null)
					mc.world.getScoreboard().removeTeam(team);
				team = scoreboard.createTeam("WUPPY_" + entityPlayer.getEntityId());
				
				createdTeams.add(team);
				
				team.setSuffix(TextFormatting.RESET.toString());
			}
			
			switch (type) {
			case MURDER:
				if(!hasNewType) {
					hasNewType = true;
					hasNewBow = hasBow;
					
					sendChatMessage(TextFormatting.RED + "Es wurde ein Murderer gefunden " + TextFormatting.DARK_RED + entityPlayer.getDisplayNameString() + (hasBow ? (TextFormatting.GRAY + ".\n" + TextFormatting.RED + Wuppy.PREFIX + TextFormatting.RED + "Er hat auch ein " + TextFormatting.DARK_RED + "Bogen" + TextFormatting.GRAY + ".") : (TextFormatting.GRAY + ".")), false);
				} else if(hasBow && !hasNewBow) {
					hasNewBow = true;
					sendChatMessage(TextFormatting.RED + "Der Murderer " + TextFormatting.DARK_RED + entityPlayer.getDisplayNameString() + TextFormatting.RED + " hat nun ein " + TextFormatting.DARK_RED + "Bogen" + TextFormatting.GRAY + ".", false);
				}
				
				AddonPictureInPicture.ADDON_PICTURE_IN_PICTURE.addPlayer(entityPlayer.getName());
				team.setColor(hasBow ? TextFormatting.DARK_RED : TextFormatting.RED);
				team.setPrefix(TextFormatting.DARK_RED + "[" + TextFormatting.RED + "M" + TextFormatting.DARK_RED + "] " + (hasBow ? (TextFormatting.RED + "[" + TextFormatting.DARK_RED + "B" + TextFormatting.RED + "] " + TextFormatting.DARK_RED) : TextFormatting.RED));
				team.setNameTagVisibility(EnumVisible.ALWAYS);
				break;
			case DETECTIV:
				AddonPictureInPicture.ADDON_PICTURE_IN_PICTURE.removePlayer(entityPlayer.getName());
				team.setColor(TextFormatting.BLUE);
				team.setPrefix(TextFormatting.DARK_BLUE + "[" + TextFormatting.BLUE + "D" + TextFormatting.DARK_BLUE + "] " + TextFormatting.BLUE);
				team.setNameTagVisibility(EnumVisible.ALWAYS);
				break;
			case INNOCENTWITHBOW:
				AddonPictureInPicture.ADDON_PICTURE_IN_PICTURE.removePlayer(entityPlayer.getName());
				team.setColor(TextFormatting.LIGHT_PURPLE);
				team.setPrefix(TextFormatting.DARK_PURPLE + "[" + TextFormatting.LIGHT_PURPLE + "B" + TextFormatting.DARK_PURPLE + "] " + TextFormatting.LIGHT_PURPLE);
				team.setNameTagVisibility(EnumVisible.ALWAYS);
				break;
			case INNOCENT:
				AddonPictureInPicture.ADDON_PICTURE_IN_PICTURE.removePlayer(entityPlayer.getName());
				team.setColor(TextFormatting.WHITE);
				team.setPrefix(TextFormatting.WHITE.toString());
				team.setNameTagVisibility(EnumVisible.ALWAYS);
				break;
			case DEAD:
			case SPECTATOR:
				AddonPictureInPicture.ADDON_PICTURE_IN_PICTURE.removePlayer(entityPlayer.getName());
				team.setColor(TextFormatting.RESET);
				team.setPrefix(TextFormatting.RESET.toString());
				team.setNameTagVisibility(EnumVisible.NEVER);
				entityPlayer.setGlowing(false);
				break;
			default:
				break;
			}
			
			if(!entityPlayer.isGlowing() && !(type == ClassType.DEAD || type == ClassType.SPECTATOR))
				entityPlayer.setGlowing(true);
			
			if(!team.getMembershipCollection().contains(entityPlayer.getName()))
				scoreboard.addPlayerToTeam(entityPlayer.getName(), "WUPPY_" + entityPlayer.getEntityId());
		}
		
		public UUID getUUID() {
			return uuid;
		}
		
		public ClassType getType() {
			return type;
		}
		
		public boolean isHasBow() {
			return hasBow;
		}
		
		public void setUUID(UUID uuid) {
			this.uuid = uuid;
		}
		
		public void setType(ClassType type) {
			this.type = type;
		}
		
		public void setHasBow(boolean hasBow) {
			this.hasBow = hasBow;
		}
	}

	public boolean hasItemInHand(EntityPlayer entityPlayer, Item item) {
		return entityPlayer.getHeldItemMainhand().getItem().equals(item) || entityPlayer.getHeldItemOffhand().getItem().equals(item);
	}
	
	public boolean hasItemInhand(EntityPlayer entityPlayer) {
		for(Item item : murderItems)
			if(entityPlayer.getHeldItemMainhand().getItem().equals(item) || entityPlayer.getHeldItemOffhand().getItem().equals(item))
				return true;
		return false;
	}
}