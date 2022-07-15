package i.WinKcode.hack.hacks.auto;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.system.Connection;
import i.WinKcode.value.types.IntegerValue;
import i.WinKcode.wrappers.Wrapper;
import i.WinKcode.hack.Hack;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class ChestStealer extends Hack{

	public IntegerValue delay;
	
	public SPacketWindowItems packet;
	public int ticks;
	
	public ChestStealer() {
		super("ChestStealer", HackCategory.AUTO);
		this.GUIName = "宝箱窃取者";
		
		delay = new IntegerValue("延迟", 4, 0, 20);
		
		this.addValue(delay);
		this.ticks = 0;
	}
	
	@Override
    public String getDescription() {
        return "从宝箱偷走所有东西.";
    }
	
	@Override
	public boolean onPacket(Object packet, Connection.Side side) {
		if(side == Connection.Side.IN && packet instanceof SPacketWindowItems) {
			this.packet = (SPacketWindowItems)packet;
		}
		return true;
	}
	
	boolean isContainerEmpty(Container container) {
		boolean temp = true;
	    int i = 0;
	    for(int slotAmount = container.inventorySlots.size() == 90 ? 54 : 35; i < slotAmount; i++) {
	    	if (container.getSlot(i).getHasStack()) {
	    		temp = false;
	    	}
	    }
	    return temp;
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(event.phase != Phase.START) return;
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		if ((!Wrapper.INSTANCE.mc().inGameHasFocus) 
        		&& (this.packet != null) 
        		&& (player.openContainer.windowId == this.packet.getWindowId()) 
        		&& ((Wrapper.INSTANCE.mc().currentScreen instanceof GuiChest))) {
			if (!isContainerEmpty(player.openContainer)) {
				for (int i = 0; i < player.openContainer.inventorySlots.size() - 36; ++i) {
                    Slot slot = player.openContainer.getSlot(i);
                    if (slot.getHasStack() && slot.getStack() != null) {
                    	if (this.ticks >= this.delay.getValue()) {
        	            	Wrapper.INSTANCE.controller().windowClick(player.openContainer.windowId, i, 1, ClickType.QUICK_MOVE, player);
        	            	this.ticks = 0;
        	            }
                    }
                }
				this.ticks += 1;
			} 
			else 
			{
            	player.closeScreen();
            	this.packet = null;
            }
		}
	}
}
