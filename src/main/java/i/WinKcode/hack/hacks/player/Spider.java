package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Spider extends Hack{
	
	public Spider() {
		super("Spider", HackCategory.PLAYER);
		this.GUIName = "爬墙";
	}
	
	@Override
	public String getDescription() {
		return "允许你像蜘蛛一样爬墙.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
        if(!Wrapper.INSTANCE.player().isOnLadder() 
        		&& Wrapper.INSTANCE.player().collidedHorizontally 
        		&& Wrapper.INSTANCE.player().motionY < 0.2) {
        	Wrapper.INSTANCE.player().motionY = 0.2;
        }
		super.onClientTick(event);
	}
	
}
