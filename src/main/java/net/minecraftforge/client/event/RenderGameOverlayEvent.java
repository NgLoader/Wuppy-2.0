package net.minecraftforge.client.event;

import net.minecraftforge.fml.common.eventhandler.*;

public class RenderGameOverlayEvent extends Event
{
    private static ListenerList listenerList;
    public final float partialTicks;
    
    public RenderGameOverlayEvent(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    @Override
    public ListenerList getListenerList() {
        return RenderGameOverlayEvent.listenerList;
    }
    
    static {
        RenderGameOverlayEvent.listenerList = new ListenerList();
    }
}
