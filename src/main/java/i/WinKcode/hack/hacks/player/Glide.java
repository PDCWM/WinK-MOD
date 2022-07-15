package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.managers.HackManager;
import i.WinKcode.utils.TimerUtils;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Glide extends Hack{

	public BooleanValue damage;
	public ModeValue mode;
	
    static int tick;
    static boolean fall;
    static int times;
	
	TimerUtils timer;
	
	public Glide() {
		super("Glide", HackCategory.PLAYER);
		this.GUIName = "滑翔";
		
		damage = new BooleanValue("自我伤害", false);
		this.mode = new ModeValue("类型", new Mode("坠落", true), new Mode("平坦", false));
		
		this.addValue(mode, damage);
				
		timer = new TimerUtils();
	}
	
	@Override
	public String getDescription() {
		return "让你在坠落时缓慢滑落.";
	}
	
	@Override
	public void onEnable() {
		if(damage.getValue())
			HackManager.getHack("自我伤害").toggle();
		super.onEnable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		if(mode.getMode("平坦").isToggled()) {
			if (!player.capabilities.isFlying && player.fallDistance > 0.0f && !player.isSneaking()) {
				player.motionY = 0.0;
        	}
        	if (Wrapper.INSTANCE.mcSettings().keyBindSneak.isKeyDown()) {
        		player.motionY = -0.11;
        	}
        	if (Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
        		player.motionY = 0.11;
        	}
        	if (timer.delay(50)) {
        		player.onGround = false;
        		timer.setLastMS();
        	}
		}
		else if(mode.getMode("坠落").isToggled())
		{
			if (player.onGround) {
                times = 0;
            }
            if (player.fallDistance > 0.0f && times <= 1) {
                if (tick > 0 && fall) {
                	player.motionY = 0.0;
                    tick = 0;
                } else {
                    ++tick;
                }
                if (player.fallDistance >= 0.1) {
                    fall = false;
                }
                if (player.fallDistance >= 0.4) {
                    fall = true;
                    player.fallDistance = 0.0f;
                    
                }
            }
		}
		super.onClientTick(event);
	}

}
