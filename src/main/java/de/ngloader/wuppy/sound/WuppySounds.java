package de.ngloader.wuppy.sound;

import java.io.File;

import de.ngloader.wuppy.Wuppy;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class WuppySounds {
	
	private static int size = 0;
	
	public static SoundEvent TEST;
	public static SoundEvent BUTTON_CLICK;
	
	public static void reigsterAllSounds() {
		size = SoundEvent.REGISTRY.getKeys().size();
		
		TEST = registerSound("test");
		BUTTON_CLICK = registerSound("button_click");
	}
	
	public static SoundEvent registerSound(String name) {
		ResourceLocation resourceLocation = new ResourceLocation(Wuppy.ASSETS_LOCATION + "" + name);
		SoundEvent soundEvent = new SoundEvent(resourceLocation);
		
		if(soundEvent != null && resourceLocation != null && new File(soundEvent.getSoundName().getResourcePath()).exists()) {
			ForgeRegistries.SOUND_EVENTS.register(soundEvent);
			Wuppy.LOGGER.info("Registered sound: " + name);
		} else
			Wuppy.LOGGER.info("Registred sound was null");
		
		return soundEvent;
	}
}