package net.minecraftforge.fml.common.eventhandler;

public enum EventPriority implements IEventListener
{
    HIGHEST, 
    HIGH, 
    NORMAL, 
    LOW, 
    LOWEST;
    
    @Override
    public void invoke(final Event event) {
        event.setPhase(this);
    }
}
