package de.ngloader.wuppy.modmanager.addons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import me.ichun.mods.pictureinpicture.common.PiP;

public class AddonPictureInPicture extends Addon {

	public static final AddonPictureInPicture ADDON_PICTURE_IN_PICTURE = new AddonPictureInPicture();

	public boolean isAvavible() {
		try {
			return Class.forName("me.ichun.mods.pictureinpicture.common.PiP") != null;
		} catch (ClassNotFoundException ex) {
		}
		return false;
	}

	public void addPlayer(String name) {
		if (!isAvavible() || !activ)
			return;

		List<String> players = new ArrayList<>();
		players.addAll(Arrays.asList(PiP.config.playerCam.split(",")));

		if (players.contains(name))
			return;
		players.add(name);

		PiP.config.playerCam = players.stream().filter(player -> player.length() > 2).collect(Collectors.joining(","));
//		PiP.config.save();
		PiP.eventHandlerClient.repopulatePiPs = true;
	}

	public void removePlayer(String name) {
		if (!isAvavible() || !activ)
			return;

		List<String> players = new ArrayList<>();
		players.addAll(Arrays.asList(PiP.config.playerCam.split(",")));

		if(!players.contains(name))
			return;
		players.remove(name);

		PiP.config.playerCam = players.stream().filter(player -> player.length() > 2).collect(Collectors.joining(","));
//		PiP.config.save();
		PiP.eventHandlerClient.repopulatePiPs = true;
	}

	public void resetPlayers() {
		if (!isAvavible() || !activ)
			return;

		PiP.config.playerCam = "";
//		PiP.config.save();
		PiP.eventHandlerClient.repopulatePiPs = true;
	}
}