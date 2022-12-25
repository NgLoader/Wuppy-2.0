package de.ngloader.wuppy.util;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Loader;

public class StringReplacer {
	
	private static final String TIME_FORMAT = "HH:mm:ss";
	private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(TIME_FORMAT);
	private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(2, Locale.getDefault());
	
	private static Field mcpVersionField;
	private static Field mcVersionField;
	
	static String mcpVersion;
	static String mcVersion;
	
	static {
		try {
			mcpVersionField = Loader.class.getDeclaredField("mcpversion");
			mcpVersionField.setAccessible(true);
			mcpVersion = (String)mcpVersionField.get(null);
			
			mcVersionField = ForgeVersion.class.getDeclaredField("mcVersion");
			mcVersion = (String)mcVersionField.get(null);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static String replacePlaceHolders(String source) {
		int modCount = Loader.instance().getModList().size();
		int activModCount = Loader.instance().getActiveModList().size();
		
		Calendar calendar = Calendar.getInstance();
		
		String clock = SIMPLE_DATE_FORMAT.format(calendar.getTime());
		String date = DATE_FORMAT.format(new Date());
		
		return source.replace("#date#", date).replace("#time#", clock).replace("#mcversion#", mcVersion).replace("#fmlversion#", Loader.instance().getFMLVersionString()).replace("#mcpversion#", mcpVersion).replace("#modsloaded#", modCount + "").replace("#modsactive#", activModCount + "").replace("#forgeversion#", ForgeVersion.getVersion()).replace("#username#", Minecraft.getMinecraft().getSession().getUsername());
	}
}