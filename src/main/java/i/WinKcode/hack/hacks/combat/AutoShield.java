package i.WinKcode.hack.hacks.combat;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;
import i.WinKcode.managers.HackManager;
import i.WinKcode.utils.RobotUtils;
import i.WinKcode.utils.Utils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AutoShield extends Hack{
	
	public AutoShield() {
		super("AutoShield", HackCategory.COMBAT);
		this.GUIName = "自动盾牌";
	}
	
	@Override
	public String getDescription() {
		return "自动管理你的盾牌.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(!Utils.screenCheck()) { this.blockByShield(false); }
		super.onClientTick(event);
	}
	
	@Override
	public void onDisable() {
		this.blockByShield(false);
		super.onDisable();
	}
	
	 public void blockByShield(boolean state) {
		if(Wrapper.INSTANCE.player().getHeldItemOffhand().getItem() != Items.SHIELD) return;
			RobotUtils.setMouse(1, state);
	}
	 
	 public static void block(boolean state) {
		AutoShield hack = (AutoShield)HackManager.getHack("AutoShield");
		if(hack.isToggled()) hack.blockByShield(state);
	}
}
