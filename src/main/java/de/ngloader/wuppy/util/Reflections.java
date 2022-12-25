package de.ngloader.wuppy.util;

import java.lang.reflect.Field;

import ibxm.Player;
import net.minecraft.network.Packet;

public class Reflections {

	public static void setValue(Object obj, String name, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
		}
	}

	public static <T> T getValue(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return (T) field.get(obj);
		} catch (Exception e) { }
		return null;
	}
}