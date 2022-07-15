package i.WinKcode.hack.hacks.visual;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.managers.SkinChangerManager;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;

public class SkinChanger extends Hack { // TODO Add cape
	
	//public BooleanValue skin;
	//public BooleanValue cape;
	
	public ResourceLocation defaultSkin;
	//public ResourceLocation defaultCape;
	
	public SkinChanger() {
		super("SkinChanger", HackCategory.VISUAL);
		this.GUIName = "换肤器";
		
		//skin = new BooleanValue("Skin", true);
		//cape = new BooleanValue("Cape", false);
		
		//this.addValue(skin, cape);
	}
	
	@Override
	public String getDescription() {
		return "改变你的皮肤/斗篷. （测试版）";
	}
	
	@Override
	public void onEnable() {
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		
		//if(skin.getValue()) {
			if(defaultSkin == null) defaultSkin = player.getLocationSkin();
			ResourceLocation location = SkinChangerManager.playerTextures.get(Type.SKIN);
			if(location != null && !SkinChangerManager.setTexture(Type.SKIN, player, location)) failed();
		//}
		
//		if(cape.getValue()) {
//			if(defaultCape == null) defaultCape = player.getLocationSkin();
//			ResourceLocation location = SkinChangerManager.playerTextures.get(Type.CAPE);
//			if(location != null && !SkinChangerManager.setTexture(Type.CAPE, player, location)) failed();
//		}
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if(defaultSkin != null 
				&& !SkinChangerManager.setTexture(Type.SKIN, Wrapper.INSTANCE.player(),
						defaultSkin)) failed();
//		if((defaultSkin != null && !SkinChangerManager.setTexture(Type.SKIN, Wrapper.INSTANCE.player(),
//				defaultSkin)) || (defaultCape != null && !SkinChangerManager.setTexture(Type.CAPE, Wrapper.INSTANCE.player(),
//						defaultCape))) failed();
		defaultSkin = null; 
		//defaultCape = null;
		super.onDisable();
	}
	void failed() { ChatUtils.error("SkinChanger：设置纹理失败！"); }
	
}
