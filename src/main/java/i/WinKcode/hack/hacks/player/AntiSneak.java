package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;

import i.WinKcode.utils.Utils;
import i.WinKcode.utils.system.Connection.Side;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiSneak extends Hack{
	
	public BooleanValue fullSprint;
	public AntiSneak() {
		super("AntiSneak", HackCategory.PLAYER);
		this.GUIName = "潜行加速";
		fullSprint = new BooleanValue("全速冲刺", true);
		this.addValue(fullSprint);
	}
	
	@Override
	public String getDescription() {
		return "潜行时不改变步行速度.";
	}
	
	@Override
	public boolean onPacket(Object packet, Side side) {
		if(side == Side.OUT && packet instanceof CPacketEntityAction) {
			CPacketEntityAction p = (CPacketEntityAction) packet;
			if(p.getAction() == CPacketEntityAction.Action.START_SNEAKING) {
				return false;
			}
		}
		return true;
	}
	@Override
	public void onClientTick(ClientTickEvent event) {
		EntityPlayerSP player = Wrapper.INSTANCE.player();
		GameSettings settings = Wrapper.INSTANCE.mcSettings();
		if(player.onGround && settings.keyBindSneak.isKeyDown()) {
			if(!fullSprint.getValue() && settings.keyBindForward.isKeyDown()) {
				player.setSprinting(Utils.isMoving(player));
			} 
			else if(fullSprint.getValue()) {
				player.setSprinting(Utils.isMoving(player));
			}
			if(settings.keyBindRight.isKeyDown() 
					|| settings.keyBindLeft.isKeyDown()
					|| settings.keyBindBack.isKeyDown()) {
				if(settings.keyBindBack.isKeyDown()) {
					player.motionX *= 1.268;
					player.motionZ *= 1.268;
				} else {
					player.motionX *= 1.252;
					player.motionZ *= 1.252;
				}
			} else {
				player.motionX *= 1.2848;
				player.motionZ *= 1.2848;
			}
		}
		super.onClientTick(event);
	}
}
