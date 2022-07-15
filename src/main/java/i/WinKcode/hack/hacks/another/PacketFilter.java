package i.WinKcode.hack.hacks.another;

import i.WinKcode.gui.PacketWindows;
import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.system.Connection;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.value.types.ModeValue;
import net.minecraft.network.play.client.*;

public class PacketFilter extends Hack {
	
	public ModeValue mode;
	
	public BooleanValue cCPacketPlayer;
	public BooleanValue cCPacketCloseWindow;
	public BooleanValue cCPacketRotation;
	public BooleanValue cCPacketPosition;
	public BooleanValue cCPacketPositionRotation;
	public BooleanValue cCPacketClientStatus;
	public BooleanValue cCPacketInput;
	public BooleanValue cCPacketPlayerAbilities;
	public BooleanValue cCPacketPlayerDigging;
	public BooleanValue cCPacketUseEntity;
	public BooleanValue cCPacketVehicleMove;
	public BooleanValue cCPacketEntityAction;
	public BooleanValue cCPacketClickWindow;
	public BooleanValue cCPacketCollect;

	public PacketWindows pw;
	
	public PacketFilter() {
		super("PacketFilter", HackCategory.ANOTHER);
		this.GUIName = "包过滤器";
		
		this.mode = new ModeValue("模式", new Mode("Output", true), new Mode("Input", false), new Mode("AllSides", false));
		
		cCPacketPlayer = new BooleanValue("Player", false);
		cCPacketEntityAction = new BooleanValue("EntityAction", false);
		cCPacketCloseWindow = new BooleanValue("CloseWindow", false);
		cCPacketRotation = new BooleanValue("Rotation", false);
		cCPacketPosition = new BooleanValue("Position", false);
		cCPacketPositionRotation = new BooleanValue("PositionRotation", false);
		cCPacketClientStatus = new BooleanValue("ClientStatus", false);
		cCPacketInput = new BooleanValue("Input", false);
		cCPacketPlayerAbilities = new BooleanValue("PlayerAbilities", false);
		cCPacketPlayerDigging = new BooleanValue("PlayerDigging", false);
		cCPacketUseEntity = new BooleanValue("UseEntity", false);
		cCPacketVehicleMove = new BooleanValue("VehicleMove", false);
		cCPacketEntityAction = new BooleanValue("EntityAction", false);
		cCPacketClickWindow = new BooleanValue("ClickWindow", false);

		cCPacketCollect = new BooleanValue("收集包", false);
		
		this.addValue(
				this.mode,
				cCPacketPlayer,
				cCPacketEntityAction,
				cCPacketCloseWindow,
				cCPacketRotation,
				cCPacketPosition,
				cCPacketPositionRotation,
				cCPacketClientStatus,
				cCPacketInput,
				cCPacketPlayerAbilities,
				cCPacketPlayerDigging,
				cCPacketUseEntity,
				cCPacketVehicleMove,
				cCPacketEntityAction,
				cCPacketClickWindow,
				cCPacketCollect
				);
	}
	
	@Override
	public String getDescription() {
		return "数据包过滤器.";
	}

	@Override
	public void onEnable() {
		if (cCPacketCollect.getValue())
		{
			pw = new PacketWindows();
		}
		super.onEnable();
	}
	
	@Override
	public boolean onPacket(Object packet, Connection.Side side) {
		if (cCPacketCollect.getValue() && pw != null)
		{
			pw.onPacket(packet,side);
		}
		if((this.mode.getMode("Output").isToggled() && side == Connection.Side.OUT)
				|| (this.mode.getMode("Input").isToggled() && side == Connection.Side.IN)
				|| (this.mode.getMode("AllSides").isToggled()))
			return checkPacket(packet);
		return true;
	}
	
	public boolean checkPacket(Object packet) {
		if((cCPacketPlayer.getValue() &&  packet instanceof CPacketPlayer)
				|| (cCPacketEntityAction.getValue() &&  packet instanceof CPacketEntityAction)
				|| (cCPacketCloseWindow.getValue() &&  packet instanceof CPacketCloseWindow)
				|| (cCPacketRotation.getValue() &&  packet instanceof CPacketPlayer.Rotation)
				|| (cCPacketPosition.getValue() &&  packet instanceof CPacketPlayer.Position)
				|| (cCPacketPositionRotation.getValue() &&  packet instanceof CPacketPlayer.PositionRotation)
				|| (cCPacketClientStatus.getValue() &&  packet instanceof CPacketClientStatus)
				|| (cCPacketInput.getValue() &&  packet instanceof CPacketInput)
				|| (cCPacketPlayerAbilities.getValue() &&  packet instanceof CPacketPlayerAbilities)
				|| (cCPacketPlayerDigging.getValue() &&  packet instanceof CPacketPlayerDigging)
				|| (cCPacketUseEntity.getValue() &&  packet instanceof CPacketUseEntity)
				|| (cCPacketVehicleMove.getValue() &&  packet instanceof CPacketVehicleMove)
				|| (cCPacketEntityAction.getValue() &&  packet instanceof CPacketEntityAction)
				|| (cCPacketClickWindow.getValue() &&  packet instanceof CPacketClickWindow))
			return false;
		return true;
	}
}
