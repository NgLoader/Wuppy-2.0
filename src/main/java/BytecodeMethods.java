import net.labymod.main.*;
import net.labymod.api.permissions.*;
import net.labymod.core.*;
import net.labymod.user.cosmetic.util.*;
import net.labymod.user.emote.*;
import net.labymod.settings.*;
import net.labymod.settings.elements.*;
import net.labymod.gui.elements.*;
import net.labymod.ingamegui.modules.*;
import org.lwjgl.opengl.*;
import org.lwjgl.*;
import java.io.*;
import net.labymod.support.report.*;

public class BytecodeMethods
{
    private static int lastItemSlot;
    
    public static boolean isBlockBuildEnabled(final boolean isHittingBlock) {
        return (!LabyMod.getSettings().oldBlockbuild || !Permissions.isAllowed(Permissions.Permission.BLOCKBUILD)) && isHittingBlock;
    }
    
    public static boolean isGuiBackground() {
        return LabyMod.getSettings().guiBackground;
    }
    
    public static boolean shouldCancelAnimation() {
        final boolean cancel = LabyMod.getInstance().getClientTickListener().isCancelSwingAnimation();
        if (cancel) {
            LabyMod.getInstance().getClientTickListener().setCancelSwingAnimation(false);
        }
        return cancel;
    }
    
    public static void onUpdateBlockBuild() {
        LabyModCore.getCoreAdapter().getMinecraftImplementation().handleBlockBuild();
    }
    
    public static boolean shouldRenderMultiplayerBackground() {
        return LabyMod.getInstance() == null || !LabyMod.getInstance().isInGame() || !(bib.z().m instanceof bnf);
    }
    
    public static void translateEyeHeight(final vg entity, final boolean invert) {
        if (LabyModCore.getMinecraft().getPlayer() != entity) {
            return;
        }
        if (LabyMod.getSettings().oldSneaking) {
            final SneakingAnimationThread animationThread = LabyMod.getInstance().getSneakingAnimationThread();
            final float ySize = (animationThread == null) ? 0.0f : animationThread.getYSize();
            float value = ySize - (entity.aU() ? 0.08f : 0.0f);
            if (invert) {
                value *= -1.0f;
            }
            bus.c(0.0f, value, 0.0f);
        }
        if (bib.z().t.aw == 0) {
            final EmoteRenderer emoteRenderer = LabyMod.getInstance().getEmoteRegistry().getEmoteRendererFor((bua)entity);
            if (emoteRenderer != null) {
                bus.b(-entity.v, 0.0f, 1.0f, 0.0f);
                emoteRenderer.transformEntity(entity, true, 0.0f, 0.0f);
                bus.b(entity.v, 0.0f, 1.0f, 0.0f);
            }
        }
    }
    
    public static double getCustomScale(final int scale, final int factor, final boolean isWidth) {
        final blk currentScreen = bib.z().m;
        if (currentScreen == null) {
            if (LabyMod.getInstance() != null) {
                LabyMod.getInstance().getEmoteRegistry().getEmoteSelectorGui().lockMouseMovementInCircle();
            }
        }
        else {
            boolean useCustomScaling = currentScreen instanceof LabyModModuleEditorGui;
            if (currentScreen instanceof StringElement.ExpandedStringElementGui && ((StringElement.ExpandedStringElementGui)currentScreen).getBackgroundScreen() instanceof LabyModModuleEditorGui) {
                useCustomScaling = true;
            }
            if (currentScreen instanceof ColorPicker.AdvancedColorSelectorGui && ((ColorPicker.AdvancedColorSelectorGui)currentScreen).getBackgroundScreen() instanceof LabyModModuleEditorGui) {
                useCustomScaling = true;
            }
            if (useCustomScaling) {
                return scale / LabyMod.getInstance().getDrawUtils().getCustomScaling();
            }
        }
        return scale / factor;
    }
    
    public static boolean shouldKeepServerData() {
        return false;
    }
    
    public static void onReceivePluginMessage(final String channelName, final gy packetBuffer) {
        LabyMod.getInstance().getEventManager().callAllPluginMessage(channelName, packetBuffer);
    }
    
    public static void onUpdateEntityCountInfo(final int rendered, final int total) {
        EntityCountModule.renderedEntityCount = rendered;
        EntityCountModule.totalEntityCount = total;
    }
    
    public static void borderlessWindowAtInitialDisplayMode(final boolean fullscreen) throws LWJGLException {
        if (fullscreen) {
            if (LabyMod.getSettings() != null && LabyMod.getSettings().borderlessWindow) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            }
            else {
                Display.setFullscreen(true);
            }
        }
        else {
            System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
        }
    }
    
    public static void borderlessWindowAtToggleFullscreen(final boolean fullscreen, final boolean pre) throws LWJGLException {
        if (LabyMod.getSettings() != null) {
            if (pre) {
                if (LabyMod.getSettings().borderlessWindow) {
                    System.setProperty("org.lwjgl.opengl.Window.undecorated", "" + !fullscreen);
                }
            }
            else {
                Display.setFullscreen(!LabyMod.getSettings().borderlessWindow && fullscreen);
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
            }
        }
    }
    
    public static void transformFirstPersonItem(final aip itemToRender) {
        final int itemId = (itemToRender != null && itemToRender.c() != null) ? ain.a(itemToRender.c()) : 0;
        if (!Permissions.isAllowed(Permissions.Permission.ANIMATIONS)) {
            return;
        }
        boolean transformAllItems = LabyMod.getSettings().oldItemHold;
        if (LabyMod.getSettings().oldBow && itemId == 261) {
            if (LabyModCore.getMinecraft().getPlayer().au) {
                bus.b(-0.10333333, 0.0, 0.0);
                transformAllItems = false;
            }
            else if (LabyModCore.getMinecraft().getPlayer().cK() != 0) {
                bus.c(-0.053333335f, 0.03f, -0.013333334f);
                bus.b(1.7f, 1.0f, 0.0f, 0.0f);
                bus.b(0.9f, 0.0f, 1.0f, 0.0f);
                bus.b(0.0f, 0.0f, 0.0f, 1.0f);
                transformAllItems = false;
            }
        }
        if (LabyMod.getSettings().oldSword && itemToRender != null && itemToRender.c() instanceof ajy) {
            if (LabyModCore.getMinecraft().getPlayer().au && LabyModCore.getMinecraft().getPlayer().cK() != 0) {
                bus.c(-0.07333333f, 0.05f, -0.11333334f);
                transformAllItems = false;
            }
            else if (LabyModCore.getMinecraft().getPlayer().au) {
                bus.b(-0.10333333, 0.0, 0.0);
                transformAllItems = false;
            }
            else if (LabyModCore.getMinecraft().getPlayer().cK() != 0) {
                bus.b(0.0, 0.04, -0.036666665);
                transformAllItems = false;
            }
        }
        if (LabyMod.getSettings().oldFishing && itemId == 346) {
            bus.c(0.0f, 0.0f, -0.3f);
            transformAllItems = false;
        }
        if (LabyMod.getSettings().oldFood && itemToRender != null && (itemToRender.c() instanceof aij || itemToRender.c() instanceof ajd)) {
            if (LabyModCore.getMinecraft().getPlayer().au) {
                bus.b(-0.10333333, 0.0, 0.0);
                transformAllItems = false;
            }
            else if (LabyModCore.getMinecraft().getPlayer().cK() != 0) {
                bus.b(0.0, -0.036666665, 0.07000000029802322);
                transformAllItems = false;
            }
        }
        if (transformAllItems) {
            bus.b(0.08, -0.01, 0.029999999329447746);
            bus.b(6.1f, 0.0f, 1.0f, 0.0f);
        }
    }
    
    public static float renderItemInFirstPerson(final aip itemToRender, final float f) {
        boolean swap = LabyMod.getSettings().leftHand;
        final int itemId = (itemToRender != null && itemToRender.c() != null) ? ain.a(itemToRender.c()) : 0;
        if (LabyMod.getSettings().swapBow && itemId == 261) {
            swap = !swap;
        }
        if (!LabyMod.getInstance().isHasLeftHand() && swap) {
            bus.b(-1.0f, 1.0f, 1.0f);
            bus.r();
        }
        final bud player = LabyModCore.getMinecraft().getPlayer();
        if (player != null) {
            final EmoteRenderer emoteRenderer = LabyMod.getInstance().getEmoteRegistry().getEmoteRendererFor((bua)player);
            if (emoteRenderer != null) {
                emoteRenderer.transformModel(null);
            }
        }
        return (LabyMod.getSettings().oldBlockhit && Permissions.isAllowed(Permissions.Permission.ANIMATIONS)) ? f : 0.0f;
    }
    
    public static boolean allowedToShiftAllItems(final aip itemStack) {
        final boolean enabled = LabyMod.getSettings().refillFix && Permissions.isAllowed(Permissions.Permission.REFILL_FIX);
        final boolean isItem = ain.a(itemStack.c()) == 282 || ain.a(itemStack.c()) == 373;
        return !enabled || !isItem;
    }
    
    public static boolean isCrosshairsyncEnabled() {
        return LabyMod.getSettings().crosshairSync && Permissions.isAllowed(Permissions.Permission.CROSSHAIR_SYNC);
    }
    
    public static void onAttack(final vg entity) {
        if (entity != null) {
            LabyMod.getInstance().getEventManager().callAttackEntity(entity);
        }
    }
    
    public static boolean modifyCriticalHit(final boolean flag) {
        return !LabyMod.getSettings().particleFix && flag;
    }
    
    public static float modifyEnchantmentCritical(final float f) {
        return LabyMod.getSettings().particleFix ? 0.0f : f;
    }
    
    public static void onReceiveChunkData(final Object packetBuffer, final Object packetObject) {
        LabyModCore.getCoreAdapter().getChunkAdapter().onReceiveChunkPacket(packetBuffer, packetObject);
    }
    
    public static boolean useGCOnLoadWorld() {
        return LabyMod.getSettings() == null || !LabyMod.getSettings().fastWorldLoading;
    }
    
    public static void reportCrash(final File file, final Object mcCrashReport) {
        CrashReportHandler.getInstance().report(file, (b)mcCrashReport);
    }
    
    public static void onIncomingPacket(final Object packet) {
        LabyMod.getInstance().getEventManager().callincomingPacket(packet);
    }
    
    public static void transformModelPre(final Object model, final Object entity) {
        if (model instanceof bpx) {
            EmoteRenderer.resetModel((bpx)model);
        }
    }
    
    public static void transformModelPost(final Object model, final Object entity) {
        if (model instanceof bpx && entity instanceof bua) {
            final EmoteRenderer emoteRenderer = LabyMod.getInstance().getEmoteRegistry().getEmoteRendererFor((bua)entity);
            if (emoteRenderer != null) {
                emoteRenderer.transformModel((bpx)model);
            }
        }
    }
    
    public static boolean isItemStackEqual(final boolean vanillaResult, final aip itemStackA, final aip itemStackB) {
        if (LabyMod.getSettings() == null || !LabyMod.getSettings().oldItemSwitch) {
            return vanillaResult;
        }
        final boolean equalsWithoutDamage = LabyModCore.getMinecraft().getStackSize(itemStackA) == LabyModCore.getMinecraft().getStackSize(itemStackB) && itemStackA.c() == itemStackB.c() && (itemStackA.p() != null || itemStackB.p() == null) && (itemStackA.p() == null || itemStackA.p().equals((Object)itemStackB.p()));
        final boolean isBow = itemStackB.c() instanceof ahg || itemStackA.c() instanceof ahg;
        final boolean isFlintAndSteel = itemStackB.c() instanceof aii || itemStackA.c() instanceof aii;
        boolean result = (isBow || isFlintAndSteel) ? (equalsWithoutDamage || vanillaResult) : vanillaResult;
        if (LabyModCore.getMinecraft().getPlayer() != null) {
            final int itemSlot = LabyModCore.getMinecraft().getPlayer().bv.d;
            if (itemSlot != BytecodeMethods.lastItemSlot) {
                result = false;
            }
            if (itemStackA.equals(itemStackB)) {
                BytecodeMethods.lastItemSlot = itemSlot;
            }
        }
        return result;
    }
    
    public static boolean shouldCancelReequipAnimation(final aip itemStackA, final aip itemStackB) {
        return false;
    }
    
    public static void drawMenuOverlay(final int mouseX, final int mouseY, final float partialTicks) {
        LabyMod.getInstance().getRenderTickListener().drawMenuOverlay(mouseX, mouseY, partialTicks);
    }
    
    public static String modifyResourcePackURL(final String url) {
        return url.replaceAll("..", "");
    }
    
    public static float subtractBackwardsWalkingAnimation(final float defaultValue) {
        return (LabyMod.getSettings() != null && LabyMod.getSettings().oldWalking) ? 0.0f : defaultValue;
    }
    
    static {
        BytecodeMethods.lastItemSlot = 0;
    }
}
