package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;

import i.WinKcode.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FastLadder extends Hack{
    
	public FastLadder() {
		super("FastLadder", HackCategory.PLAYER);
		this.GUIName = "快速爬梯子";
	}
	
	@Override
	public String getDescription() {
		return "可以让你更快地爬上梯子.";
	}
    
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(!Wrapper.INSTANCE.player().isOnLadder() || Wrapper.INSTANCE.player().moveForward == 0 && Wrapper.INSTANCE.player().moveStrafing == 0) return;
		Wrapper.INSTANCE.player().motionY = 0.169;
		super.onClientTick(event);
	}
	
}
