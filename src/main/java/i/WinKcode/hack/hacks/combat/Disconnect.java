package i.WinKcode.hack.hacks.combat;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.value.types.DoubleValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Disconnect extends Hack{

	public DoubleValue leaveHealth;
	
	public Disconnect() {
		super("Disconnect", HackCategory.COMBAT);
		this.GUIName = "狗头保命";
		
		leaveHealth = new DoubleValue("退出生命值", 4.0D, 0D, 20D);
		
		this.addValue(leaveHealth);
	}
	
	@Override
	public String getDescription() {
		return "当你的生命值不佳时自动退出服务器.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(Wrapper.INSTANCE.player().getHealth() <= leaveHealth.getValue().floatValue()) {
			
			boolean flag = Wrapper.INSTANCE.mc().isIntegratedServerRunning();
			Wrapper.INSTANCE.world().sendQuittingDisconnectingPacket();
			Wrapper.INSTANCE.mc().loadWorld((WorldClient)null);
			
            if (flag)
            	Wrapper.INSTANCE.mc().displayGuiScreen(new GuiMainMenu()); else
            	Wrapper.INSTANCE.mc().displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
            this.setToggled(false);
		}
		super.onClientTick(event);
	}
}
