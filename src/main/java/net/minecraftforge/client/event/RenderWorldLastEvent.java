package net.minecraftforge.client.event;

import net.minecraftforge.fml.common.eventhandler.*;

public class RenderWorldLastEvent extends Event
{
    private static final ListenerList listenerList;
    public final float partialTicks;
    
    public RenderWorldLastEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    @Override
    public ListenerList getListenerList() {
        return RenderWorldLastEvent.listenerList;
    }
    
    static {
        listenerList = new ListenerList();
    }
}
