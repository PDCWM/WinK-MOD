package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Entity301;
import i.WinKcode.utils.system.Connection;
import i.WinKcode.utils.system.Connection.Side;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.network.play.client.CPacketPlayer;

public class FreeCam extends Hack{

	public Entity301 entity301 = null;
	
	public FreeCam() {
		super("FreeCam", HackCategory.VISUAL);
		this.GUIName = "灵魂出窍";
	}
	
	@Override
	public String getDescription() {
		return "允许您在不移动角色的情况下移动相机.";
	}
	
	@Override
	public boolean onPacket(Object packet, Side side) {
		return !(side == Connection.Side.OUT && (packet instanceof CPacketPlayer
                || packet instanceof CPacketPlayer.Position
                || packet instanceof CPacketPlayer.Rotation
                || packet instanceof CPacketPlayer.PositionRotation));
	}
	
	@Override
	public void onEnable() {
		if (Wrapper.INSTANCE.player() != null && Wrapper.INSTANCE.world() != null) {
            this.entity301 = new Entity301(Wrapper.INSTANCE.world(), Wrapper.INSTANCE.player().getGameProfile());
            this.entity301.setPosition(Wrapper.INSTANCE.player().posX, Wrapper.INSTANCE.player().posY, Wrapper.INSTANCE.player().posZ);
            this.entity301.inventory = Wrapper.INSTANCE.inventory();
            //this.entity301.yOffset = Wrapper.INSTANCE.player().yOffset;
            //this.entity301.ySize = Wrapper.INSTANCE.player().ySize;
            this.entity301.rotationPitch = Wrapper.INSTANCE.player().rotationPitch;
            this.entity301.rotationYaw = Wrapper.INSTANCE.player().rotationYaw;
            this.entity301.rotationYawHead = Wrapper.INSTANCE.player().rotationYawHead;
            Wrapper.INSTANCE.world().spawnEntity(this.entity301);
        }
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if (this.entity301 != null && Wrapper.INSTANCE.world() != null) {
            Wrapper.INSTANCE.player().setPosition(this.entity301.posX, this.entity301.posY, this.entity301.posZ);
            //Wrapper.INSTANCE.player().yOffset = this.entity301.yOffset;
            //Wrapper.INSTANCE.player().ySize = this.entity301.ySize;
            Wrapper.INSTANCE.player().rotationPitch = this.entity301.rotationPitch;
            Wrapper.INSTANCE.player().rotationYaw = this.entity301.rotationYaw;
            Wrapper.INSTANCE.player().rotationYawHead = this.entity301.rotationYawHead;
            Wrapper.INSTANCE.world().removeEntity(this.entity301);
            this.entity301 = null;
        }
		super.onDisable();
	}

}
