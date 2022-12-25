package net.minecraftforge.client.event;

import net.minecraftforge.fml.common.eventhandler.*;

public class GuiOpenEvent extends Event
{
    private static ListenerList listenerList;
    
    @Override
    public ListenerList getListenerList() {
        return GuiOpenEvent.listenerList;
    }
    
    @Override
    public boolean isCancelable() {
        return true;
    }
    
    static {
        GuiOpenEvent.listenerList = new ListenerList();
    }
}
