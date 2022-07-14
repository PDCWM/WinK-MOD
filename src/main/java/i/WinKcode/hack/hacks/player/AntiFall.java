package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;

import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiFall extends Hack{
	public BooleanValue goGround;

	public AntiFall() {
		super("AntiFall", HackCategory.PLAYER);
		this.GUIName = "防坠落";

		goGround = new BooleanValue("强制落地", false);
		this.addValue(goGround);
	}
	
	@Override
	public String getDescription() {
		return "坠落伤害为零.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(Wrapper.INSTANCE.player().fallDistance > 2)
			if (goGround.getValue() || Wrapper.INSTANCE.player().motionY < -2) {
				Wrapper.INSTANCE.player().motionY = -2;
			}
			Wrapper.INSTANCE.sendPacket(new CPacketPlayer(true));

		super.onClientTick(event); 
	}
}
