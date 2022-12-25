package net.minecraftforge.common;

import net.minecraftforge.fml.common.eventhandler.*;

public class MinecraftForge
{
    public static final EventBus EVENT_BUS;
    
    static {
        EVENT_BUS = new EventBus();
    }
}
