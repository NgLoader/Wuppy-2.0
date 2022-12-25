package de.ngloader.wuppy.modmanager.modul.triggers;

import java.util.LinkedList;
import java.util.List;

import de.ngloader.wuppy.modmanager.mods.Mod;
import de.ngloader.wuppy.modmanager.modul.Trigger;
import net.minecraft.scoreboard.ScoreObjective;

public class TriggerScoreboard extends Trigger {

	public static List<String> currentObjectiveNames = new LinkedList<>();
	public static long lastUpdate = 0;

	private final Mod mod;

	private final String objectiveName;
	private final Boolean ignoreCase;

	public TriggerScoreboard(Mod mod, String objectiveName, boolean ignoreCase) {
		this.mod = mod;
		this.objectiveName = objectiveName;
		this.ignoreCase = ignoreCase;
	}

	@Override
	public boolean isTriggered() {
		if (mc == null || mc.world == null || mc.world.getScoreboard() == null)
			return false;
		for(ScoreObjective score : mc.world.getScoreboard().getScoreObjectives()) {
			if(score.getName() != null && ignoreCase ?
					score.getName().equalsIgnoreCase(this.objectiveName) :
					score.getName().equals(this.objectiveName))
				return true;
		}
		return false;
	}

	public Mod getMod() {
		return mod;
	}
}