package i.WinKcode.hack.hacks.another;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;
import i.WinKcode.utils.TimerUtils;
import i.WinKcode.value.types.DoubleValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiAfk extends Hack{
	
	public DoubleValue delay;
	public TimerUtils timer;
	
	public AntiAfk() {
		super("AntiAfk", HackCategory.ANOTHER);
		this.GUIName = "反Afk";
		
		this.timer = new TimerUtils();
		this.delay = new DoubleValue("延迟秒", 10.0D, 1.0D, 100.0D);
		
		this.addValue(delay);
	}
	
	@Override
	public String getDescription() {
		return "防止被AFK踢.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) { 
		if(timer.isDelay((long)(1000 * delay.getValue().longValue()))) {
			Wrapper.INSTANCE.player().jump();
			timer.setLastMS();
		}
		super.onClientTick(event); 
	}
}
