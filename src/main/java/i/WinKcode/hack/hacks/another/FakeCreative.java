package i.WinKcode.hack.hacks.another;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.world.GameType;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FakeCreative extends Hack {
	
	public GameType gameType;
	public BooleanValue showItemsId;
	
	public FakeCreative() {
		super("FakeCreative", HackCategory.ANOTHER);
		this.GUIName = "假创造模式";
		
		showItemsId = new BooleanValue("显示项目ID", true);
		this.addValue(showItemsId);
	}
	
	@Override
	public void onGuiOpen(GuiOpenEvent event) {
		if(event.getGui() instanceof GuiContainerCreative)
			event.setGui(new i.WinKcode.gui.GuiContainerCreative(Wrapper.INSTANCE.player()));
		super.onGuiOpen(event);
	}
	
	@Override
	public void onDisable() {
		if(gameType == null) return;
		Wrapper.INSTANCE.controller().setGameType(gameType);
		gameType = null;
		super.onDisable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(Wrapper.INSTANCE.controller().getCurrentGameType() == GameType.CREATIVE) return;
		gameType = Wrapper.INSTANCE.controller().getCurrentGameType();
		Wrapper.INSTANCE.controller().setGameType(GameType.CREATIVE);
		super.onClientTick(event);
	}
}
