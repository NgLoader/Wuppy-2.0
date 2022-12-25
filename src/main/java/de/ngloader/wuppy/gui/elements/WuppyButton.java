/** <a href="http://www.cpupk.com/decompiler">Eclipse Class Decompiler</a> plugin, Copyright (c) 2017 Chen Chao. */
package de.ngloader.wuppy.gui.elements;

import org.lwjgl.opengl.ARBShadow;

import de.ngloader.wuppy.Wuppy;
import de.ngloader.wuppy.modmanager.addons.Addon;
import de.ngloader.wuppy.modmanager.mods.Mod;
import de.ngloader.wuppy.sound.WuppySounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WuppyButton extends GuiButton {

	public static ResourceLocation BUTTON_TEXTURES = new ResourceLocation(
			Wuppy.ASSETS_LOCATION + "textures/gui/widgets.png");

	public int width;

	public int height;

	public int x;

	public int y;

	public String displayString;
	public int id;

	public boolean enabled;

	public boolean customColor = false;
	public float customR = 1F;
	public float customG = 1F;
	public float customB = 1F;
	public float customA = 1F;

	public Mod mod;
	public Addon addon;

	public boolean visible;
	protected boolean hovered;
	public int packedFGColour; // FML

	public WuppyButton(int buttonId, int x, int y, String buttonText) {
		this(buttonId, x, y, 200, 20, buttonText);
	}

	public WuppyButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		this.width = 200;
		this.height = 20;
		this.enabled = true;
		this.visible = true;
		this.id = buttonId;
		this.x = x;
		this.y = y;
		this.width = widthIn;
		this.height = heightIn;
		this.displayString = buttonText;
		this.mod = null;
		this.addon = null;
	}

	public WuppyButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, Mod mod) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		this.width = 200;
		this.height = 20;
		this.enabled = true;
		this.visible = true;
		this.id = buttonId;
		this.x = x;
		this.y = y;
		this.width = widthIn;
		this.height = heightIn;
		this.displayString = buttonText;
		this.mod = mod;
		this.addon = null;
	}

	public WuppyButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, Addon addon) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
		this.width = 200;
		this.height = 20;
		this.enabled = true;
		this.visible = true;
		this.id = buttonId;
		this.x = x;
		this.y = y;
		this.width = widthIn;
		this.height = heightIn;
		this.displayString = buttonText;
		this.addon = addon;
		this.mod = null;
	}

	@Override
	protected int getHoverState(boolean mouseOver) {
		int i = 1;

		if (!this.enabled)
			i = 0;
		else if (mouseOver)
			i = 2;
		return i;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			FontRenderer fontrenderer = mc.fontRenderer;
			mc.getTextureManager().bindTexture(BUTTON_TEXTURES);

			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
					&& mouseY < this.y + this.height;
			int i = this.getHoverState(this.hovered);
			int hight = 0;

			if (!this.enabled)
				hight = 20;
			else if (this.hovered)
				hight = 40;

			boolean enabled = (mod != null || addon != null);
			boolean activ = enabled ? mod != null ? mod.activ : addon != null ? addon.activ : false : false;

			GlStateManager.color(
					customColor ? customR : enabled ? activ ? 0F : 1F : 1F,
					customColor ? customG : enabled ? activ ? 1F : 0F : 1F,
					customColor ? customB : enabled ? activ ? 0F : 0F : 1F,
					customColor ? customA : 1F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
					GlStateManager.DestFactor.ZERO);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
					GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.x, this.y, 0, hight, this.width / 2, this.height);
			this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, hight, this.width / 2,
					this.height);

			this.mouseDragged(mc, mouseX, mouseY);

			int color = RGBToInt(255, 255, 255);
			if (packedFGColour != 0) {
				color = packedFGColour;
			} else if (!this.enabled) {
				color = 10526880;
			} else if (this.hovered) {
				color = RGBToInt(255, 255, 200);
			}

			this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2,
					this.y + (this.height - 8) / 2, color);
		}
	}

	public int RGBToInt(int red, int green, int blue) {
		return (red * 65536) + (green * 256) + blue;
	}

	@Override
	protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY) {
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
		return this.enabled && this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
				&& mouseY < this.y + this.height;
	}

	@Override
	public boolean isMouseOver() {
		return this.hovered;
	}

	@Override
	public void playPressSound(SoundHandler soundHandlerIn) {
		soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(WuppySounds.BUTTON_CLICK, 1.0F));
	}

	@Override
	public int getButtonWidth() {
		return this.width;
	}

	@Override
	public void setWidth(int width) {
		this.width = width;
	}
}