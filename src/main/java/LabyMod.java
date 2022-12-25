public class LabyMod
{
    private static LabyMod instance;
    private EventCaller eventCaller;
    
    public void init() {
        LabyMod.instance = this;
        this.eventCaller = new EventCaller();
    }
    
    public EventCaller getEventCaller() {
        return this.eventCaller;
    }
    
    public static LabyMod instance() {
        return LabyMod.instance;
    }
}
