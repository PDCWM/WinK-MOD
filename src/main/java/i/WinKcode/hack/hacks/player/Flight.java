package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Flight extends Hack{

	public ModeValue mode;
	int ticks = 0;
	public Flight() {
		super("Flight", HackCategory.PLAYER);
		this.GUIName = "飞行";

		this.mode = new ModeValue("模式", new Mode("简易", true), new Mode("动态", false), new Mode("Hypixel", false));

		this.addValue(mode);
	}

	@Override
	public String getDescription() {
		return "允许你飞行.";
	}

	@Override
	public void onEnable() {
		ticks = 0;
		super.onEnable();
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		if(mode.getMode("Hypixel").isToggled()) {
			player.motionY = 0.0;
			player.setSprinting(true);
			player.onGround = true;
		    ticks++;
		    if(ticks == 2 || ticks == 4 || ticks == 6 || ticks == 8 || ticks == 10 || ticks == 12 || ticks == 14 || ticks == 16 || ticks == 18 || ticks == 20) {
		    player.setPosition(player.posX, player.posY + 0.00000000128, player.posZ);
		    } if(ticks == 20) {
		    	ticks = 0;
		    }
		}
		else if(mode.getMode("简易").isToggled())
		{
			player.capabilities.isFlying = true;
		}
		else if(mode.getMode("动态").isToggled())
		{
			float flyspeed = 1.0f;
			player.jumpMovementFactor = 0.4f;
			player.motionX = 0.0;
			player.motionY = 0.0;
			player.motionZ = 0.0;
			player.jumpMovementFactor *= (float) flyspeed * 3f;
	        if (Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
	        	player.motionY += flyspeed;
	        }
	        if (Wrapper.INSTANCE.mcSettings().keyBindSneak.isKeyDown()) {
	        	player.motionY -= flyspeed;
	        }
		}


		super.onClientTick(event);
	}

	@Override
	public void onKeyPressed(int key){

	}

	@Override
	public void onDisable() {
		if(mode.getMode("简易").isToggled()) {
			Wrapper.INSTANCE.player().capabilities.isFlying = false;
		}
		super.onDisable();
	}
}

