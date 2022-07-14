package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.TimerUtils;
import i.WinKcode.utils.Utils;
import i.WinKcode.value.types.IntegerValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Rage extends Hack{
	
	public TimerUtils timer;
	
	public IntegerValue delay;
	
	public Rage() {
		super("Rage", HackCategory.PLAYER);
		this.GUIName = "狂暴";
		
		this.timer = new TimerUtils();
		delay = new IntegerValue("延迟", 0, 0, 1000);
		
		this.addValue(delay);
	}

	@Override
	public String getDescription() {
		return "其他玩家会看见你疯狂摇头.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(timer.isDelay(delay.getValue().longValue())) {
			Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Rotation(Utils.random(-160, 160), Utils.random(-160, 160), true));
			timer.setLastMS();
		}
		super.onClientTick(event);
	}
	
//	@Override // TODO Added camera fix
//	public void onCameraSetup(CameraSetup event) {
//		// TODO Auto-generated method stub
//		super.onCameraSetup(event);
//	}
}
