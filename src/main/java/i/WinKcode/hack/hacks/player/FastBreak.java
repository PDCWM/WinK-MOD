package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;
import i.WinKcode.utils.BlockUtils;
import i.WinKcode.utils.PlayerControllerUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FastBreak extends Hack{
	
	public FastBreak() {
		super("FastBreak", HackCategory.PLAYER);
		this.GUIName = "快速破坏方块";
	}
	
	@Override
	public String getDescription() {
		return "允许你更快地打破方块.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		PlayerControllerUtils.setBlockHitDelay(0);
		super.onClientTick(event);
	}
	
    @Override
    public void onLeftClickBlock(LeftClickBlock event) {
    	float progress = PlayerControllerUtils.getCurBlockDamageMP() + BlockUtils.getHardness(event.getPos());
    	if(progress >= 1) return;
    	Wrapper.INSTANCE.sendPacket(new CPacketPlayerDigging(
    			CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(),
    			Wrapper.INSTANCE.mc().objectMouseOver.sideHit));
    	super.onLeftClickBlock(event);
    }
	
}
