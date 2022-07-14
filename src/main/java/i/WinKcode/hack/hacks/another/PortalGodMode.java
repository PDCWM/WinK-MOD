package i.WinKcode.hack.hacks.another;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;
import i.WinKcode.utils.system.Connection.Side;
import net.minecraft.network.play.client.CPacketConfirmTeleport;

public class PortalGodMode extends Hack{
	
	public PortalGodMode() {
		super("PortalGodMode", HackCategory.ANOTHER);
		this.GUIName = "传送门模块屏蔽";
	}
	
	@Override
	public String getDescription() {
		return "传送门模块屏蔽, 取消 CPacketConfirmTeleport 包.";
	}
	
	@Override
	public boolean onPacket(Object packet, Side side) {
		if(packet instanceof CPacketConfirmTeleport)
			return false;
		return true;
	}
}
