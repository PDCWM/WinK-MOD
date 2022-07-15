package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiRain extends Hack{

	public AntiRain() {
		super("AntiRain", HackCategory.VISUAL);
		this.GUIName = "防雨";
	}
	
	@Override
	public String getDescription() {
		return "停止下雨.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
        Wrapper.INSTANCE.world().setRainStrength(0.0f);
		super.onClientTick(event);
	}

}
