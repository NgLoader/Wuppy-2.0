package net.minecraftforge.fml.common.gameevent;

import net.minecraftforge.fml.common.eventhandler.*;

public abstract class InputEvent extends Event
{
    public static class KeyInputEvent extends InputEvent
    {
        private static ListenerList listenerList;
        
        @Override
        public ListenerList getListenerList() {
            return KeyInputEvent.listenerList;
        }
        
        static {
            KeyInputEvent.listenerList = new ListenerList();
        }
    }
    
    public static class MouseInputEvent extends InputEvent
    {
        private static ListenerList listenerList;
        
        @Override
        public ListenerList getListenerList() {
            return MouseInputEvent.listenerList;
        }
        
        static {
            MouseInputEvent.listenerList = new ListenerList();
        }
    }
}
