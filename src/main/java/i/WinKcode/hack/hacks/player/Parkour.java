package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Utils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Parkour extends Hack{
	
	public Parkour() {
		super("Parkour", HackCategory.PLAYER);
		this.GUIName = "跑酷";
	}
	
	@Override
	public String getDescription() {
		return "到达方块边缘时跳跃.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(Utils.isBlockEdge(Wrapper.INSTANCE.player()) 
				&& !Wrapper.INSTANCE.player().isSneaking()) 
			Wrapper.INSTANCE.player().jump();
		super.onClientTick(event);
	}
	
}
