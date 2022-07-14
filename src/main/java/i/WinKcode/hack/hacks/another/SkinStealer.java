package i.WinKcode.hack.hacks.another;

import i.WinKcode.hack.HackCategory;
import org.lwjgl.input.Mouse;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import i.WinKcode.hack.Hack;
import i.WinKcode.managers.SkinChangerManager;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class SkinStealer extends Hack{
	
	public EntityPlayer currentPlayer;
	
	public SkinStealer() {
		super("SkinStealer", HackCategory.ANOTHER);
		this.GUIName = "皮肤窃取者";
	}
	
	@Override
	public String getDescription() {
		return "左键点击玩家 - 偷皮肤.";
	}
	
	@Override
	public void onDisable() {
		currentPlayer = null;
		super.onDisable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
		if(object == null) return;
		if(object.typeOfHit == RayTraceResult.Type.ENTITY) {
			Entity entity = object.entityHit;
			if(entity instanceof EntityPlayer 
					&& !(entity instanceof EntityArmorStand) 
					&& !Wrapper.INSTANCE.player().isDead 
					&& Wrapper.INSTANCE.player().canEntityBeSeen(entity))
			{
				EntityPlayer player = (EntityPlayer)entity;
				if(Mouse.isButtonDown(0) 
						&& Wrapper.INSTANCE.mc().currentScreen == null 
						&& player != currentPlayer 
						&& player.getGameProfile() != null) 
				{
					SkinChangerManager.addTexture(Type.SKIN, player.getGameProfile().getName());
					currentPlayer = player;
				}

			}
    	}
		super.onClientTick(event);
	}
}
