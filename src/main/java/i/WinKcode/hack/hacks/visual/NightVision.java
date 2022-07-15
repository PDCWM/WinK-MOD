package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Utils;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class NightVision extends Hack{

	public ModeValue mode;
	
	public NightVision() {
		super("NightVision", HackCategory.VISUAL);
		this.GUIName = "夜视";
		
		this.mode = new ModeValue("类型", new Mode("亮度", true), new Mode("能力", false));
		this.addValue(mode);
	}
	
	@Override
	public String getDescription() {
		return "拥有夜视能力.";
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	@Override
	public void onDisable() {
		if(this.mode.getMode("亮度").isToggled())
			Wrapper.INSTANCE.mcSettings().gammaSetting = 1;
		else
			Utils.removeEffect(16);
		super.onDisable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(this.mode.getMode("亮度").isToggled())
			Wrapper.INSTANCE.mcSettings().gammaSetting = 10;
		else
			Utils.addEffect(16, 1000, 3);
		super.onClientTick(event);
	}
}
