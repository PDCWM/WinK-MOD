package i.WinKcode.hack.hacks.visual;

import i.WinKcode.Main;
import i.WinKcode.gui.GuiConsole;
import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.managers.HackManager;
import i.WinKcode.utils.visual.ColorUtils;
import i.WinKcode.utils.visual.RenderUtils;
import i.WinKcode.value.Mode;
import i.WinKcode.value.Value;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HUD extends Hack {
	
	public BooleanValue effects;
	
	public HUD() {
		super("HUD", HackCategory.VISUAL);
		this.GUIName = "HUD";
		this.setToggled(true);
		this.setShow(false);
		
		effects = new BooleanValue("效果", false);
		this.addValue(effects);
	}
	
	@Override
	public String getDescription() {
		return "平视显示器.";
	}
	
	@Override
	public void onRenderGameOverlay(Text event) {
		//if(Wrapper.INSTANCE.mc().getLanguageManager().getCurrentLanguage() == Wrapper.INSTANCE.mc().getLanguageManager().getLanguage("ru_ru")) {}

		double x = Wrapper.INSTANCE.player().posX;
		double y = Wrapper.INSTANCE.player().posY;
		double z = Wrapper.INSTANCE.player().posZ;
		
		ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
		String coords = String.format("\u00a77X: \u00a7f%s \u00a77Y: \u00a7f%s \u00a77Z: \u00a7f%s", RenderUtils.DF((float)x, 1), RenderUtils.DF((float)y, 1), RenderUtils.DF((float)z, 1));
		boolean isChatOpen = Wrapper.INSTANCE.mc().currentScreen instanceof GuiChat || Wrapper.INSTANCE.mc().currentScreen instanceof GuiConsole;
		
		int heightFPS = isChatOpen ? sr.getScaledHeight() - 37 : sr.getScaledHeight() - 24;
		int heightCoords = isChatOpen ? sr.getScaledHeight() - 25 : sr.getScaledHeight() - 12;
		int heightLogo = Wrapper.INSTANCE.player().getActivePotionEffects().size() > 0 ? 24 : 0;

		int colorRect = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F);
		int colorRect2 = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F);

		GL11.glPushMatrix();
		GlStateManager.translate(sr.getScaledWidth() - 63,heightLogo,0);
		GL11.glScalef(2.0f, 2.0f, 2.0f);
		Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(Main.NAMECN,  0, 0, ClickGui.getColor());
		//int lastWidth = Wrapper.INSTANCE.fontRenderer().getStringWidth(Main.NAMECN) * 2;
		//Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(Main.MCVERSION, 84, 4, ClickGui.getColor());
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(Main.VERSION + "  Q:193697851", 0,  15, ClickGui.getColor());
		//Wrapper.INSTANCE.fontRenderer().drawStringWithShadow("193697851", 7 + lastWidth, 10, ClickGui.getColor());
		GL11.glPopMatrix();
		
		RenderUtils.drawStringWithRect(coords, 4, heightCoords, ClickGui.getColor(), 
				colorRect, colorRect2);
		
		RenderUtils.drawStringWithRect("\u00a77FPS: \u00a7f" + Wrapper.INSTANCE.mc().getDebugFPS(), 4, heightFPS, ClickGui.getColor(), 
				colorRect, colorRect2);

		// 显示功能列表
		List<String> nameList = new ArrayList();
		String toggleHack = "";
		for(Hack hack : HackManager.getSortedHacks()) {
			String modeName = "";
			for(Value value : hack.getValues()) {
				if(value instanceof ModeValue) {
					ModeValue modeValue = (ModeValue)value;
					if(!modeValue.getModeName().equals("Priority")) {
						for(Mode mode : modeValue.getModes()) {
							if(mode.isToggled()) {
								modeName = modeName + " \u00a77" + mode.getName();
							}
						}
					}
				}
			}
			String name = hack.GUIName;
			if(!i.WinKcode.hack.hacks.visual.ClickGui.language.getMode("中文").isToggled()) {
				name = hack.getName();
			}
			nameList.add(name + modeName);
			if(HackManager.getToggleHack() == hack){
				toggleHack = name + modeName;
			}
		}

		//冒泡排序
		while (true){
			boolean isT = true;
			for (int i=0;i < nameList.size(); i++){
				if(i + 1 < nameList.size()){
					if(Wrapper.INSTANCE.fontRenderer().getStringWidth(nameList.get(i)) < Wrapper.INSTANCE.fontRenderer().getStringWidth(nameList.get(i + 1))){
						String th = nameList.get(i);
						nameList.set(i,nameList.get(i + 1));
						nameList.set(i + 1,th);
						isT = false;
					}
				}
			}
			if (isT){
				break;
			}
		}

		int yPos = 4;
		int xPos;
		for(String str : nameList) {
			xPos = 0;
			if(HackManager.getHack("FastGUI").isToggled()){
				xPos = 86;
			}
			if(toggleHack.equals(str)){
				xPos = RenderUtils.drawSplashPos(xPos);
			}
			if(effects.getValue()) {
				xPos += 6;
				RenderUtils.drawBorderedRect(xPos - 2, yPos - 2, xPos + Wrapper.INSTANCE.fontRenderer().getStringWidth(str) + 2, yPos + 10, 1, colorRect, ClickGui.getColor());
			} else {
				xPos += 4;
			}
			if(effects.getValue()) {
				RenderUtils.drawBorderedRect(xPos - 2, yPos - 2, xPos - 6, yPos + 10, 1, ClickGui.getColor(), ClickGui.getColor());
			}
			RenderUtils.drawStringWithRect(str, xPos, yPos, ClickGui.getColor(), colorRect, colorRect2);
			yPos += 12;
		}

		GL11.glPushMatrix();
		GlStateManager.translate(sr.getScaledWidth() - 88,heightLogo + 1,0);
		RenderUtils.drawImage(getClass().getClassLoader().getResource("assets/awwgx-yfgux.png"),
				0,22,22,0);
		GL11.glPopMatrix();

		/*Hack toggleHack = HackManager.getToggleHack();
		if(toggleHack != null) {
			RenderUtils.drawSplash(toggleHack.isToggled()  ? 
					toggleHack.GUIName + " - 开启" :
						 "\u00a77" + toggleHack.GUIName + " - 关闭");
		}*/
		super.onRenderGameOverlay(event);
	}
}
