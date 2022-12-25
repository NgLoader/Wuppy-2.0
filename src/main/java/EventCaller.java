import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import net.minecraftforge.client.event.*;

public class EventCaller
{
    private EventBus bus;
    
    public EventCaller() {
        this.bus = MinecraftForge.EVENT_BUS;
    }
    
    public void callTick() {
        this.bus.post(new TickEvent.ClientTickEvent());
    }
    
    public void callRenderTick(final float renderTickTime) {
        this.bus.post(new TickEvent.RenderTickEvent(renderTickTime));
    }
    
    public void callKeyInput() {
        this.bus.post(new InputEvent.KeyInputEvent());
    }
    
    public void callMouseInput() {
        this.bus.post(new InputEvent.MouseInputEvent());
    }
    
    public static boolean mouseNext() {
        final boolean next = Mouse.next();
        if (next) {
            instance().callMouseInput();
        }
        return next;
    }
    
    public static float getFovModifier(final Object entityPlayer, final float fov) {
        final FOVUpdateEvent event = new FOVUpdateEvent(fov);
        instance().bus.post(event);
        return event.newfov;
    }
    
    public static EventCaller instance() {
        return LabyMod.instance().getEventCaller();
    }
}
