package i.WinKcode.hack.hacks.auto;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoWalk extends Hack{
	
	public AutoWalk() {
		super("AutoWalk", HackCategory.AUTO);
		this.GUIName = "自动行走";
	}
	
	@Override
	public String getDescription() {
		return "自动行走.";
	}
	
	@Override
	public void onDisable() {
		KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode(), false);
		super.onDisable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		KeyBinding.setKeyBindState(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode(), true);
		super.onClientTick(event);
	}
	
}
