package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;

import i.WinKcode.value.Mode;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoSwim extends Hack{

	public ModeValue mode;
	public AutoSwim() {
		super("AutoSwim", HackCategory.PLAYER);
		this.GUIName = "游泳加速";
		
		this.mode = new ModeValue("类型", new Mode("跳跃", true), new Mode("海豚", false), new Mode("鱼", false));
		this.addValue(mode);
	}
	
	@Override
    public String getDescription() {
        return "入水时自动跳跃.";
    }
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(!Wrapper.INSTANCE.player().isInWater() && !Wrapper.INSTANCE.player().isInLava()) {
			return;
		}
		if(Wrapper.INSTANCE.player().isSneaking() || Wrapper.INSTANCE.mcSettings().keyBindJump.isKeyDown()) {
			return;
		}
		if(mode.getMode("跳跃").isToggled()) {
			Wrapper.INSTANCE.player().jump();
		} 
		else if(mode.getMode("海豚").isToggled()) {
			Wrapper.INSTANCE.player().motionY += 0.04f;
		} 
		else if(mode.getMode("鱼").isToggled()) {
			Wrapper.INSTANCE.player().motionY += 0.02f;
		}
		super.onClientTick(event);
	}
}
