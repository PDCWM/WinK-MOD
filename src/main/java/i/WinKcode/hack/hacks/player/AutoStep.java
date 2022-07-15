package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.DoubleValue;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoStep extends Hack{
	
	public ModeValue mode;
	public DoubleValue height;
	public float tempHeight;
	public int ticks = 0;
	
	public AutoStep() {
		super("AutoStep", HackCategory.PLAYER);
		this.GUIName = "跳跃台阶";
		
		this.mode = new ModeValue("类型", new Mode("简易", true), new Mode("AAC", false));
		height = new DoubleValue("高度", 0.5D, 0D, 10D);
		
		this.addValue(mode, height);
	}
	
	@Override
    public String getDescription() {
        return "遇到台阶后直接上去.";
    }
	
	@Override
	public void onEnable() {
		ticks = 0;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Wrapper.INSTANCE.player().stepHeight = 0.5f;
		super.onDisable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(mode.getMode("AAC").isToggled()) {
			EntityPlayerSP player = Wrapper.INSTANCE.player();
			if(player.collidedHorizontally) {
				switch(ticks) {
					case 0:
					if(player.onGround)
						player.jump();
						break;
					case 7:
						player.motionY = 0;
						break;
					case 8:
					if(!player.onGround)
						player.setPosition(player.posX, player.posY + 1, player.posZ);
						break;
				}
				ticks++;
			} else {
				ticks = 0;
			}
		} else if(mode.getMode("简易").isToggled()) {
			Wrapper.INSTANCE.player().stepHeight = height.getValue().floatValue();
		}
		
		super.onClientTick(event);
	}
	
}
