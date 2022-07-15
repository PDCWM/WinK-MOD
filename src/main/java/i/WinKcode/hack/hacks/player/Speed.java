package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Utils;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.DoubleValue;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Speed extends Hack{
	public ModeValue mode;
	public DoubleValue speedV;

	public Speed() {
		super("Speed", HackCategory.PLAYER);
		this.GUIName = "速度";

		this.speedV = new DoubleValue("变速度", 1, 0.1, 5);

		this.mode = new ModeValue("模式", new Mode("兔子跳", true), new Mode("变速", false));
		this.addValue(mode, speedV);
	}
	
	@Override
	public String getDescription() {
		return "让你移动得更快.";
	}
    
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(mode.getMode("兔子跳").isToggled()) {
			boolean boost = Math.abs(Wrapper.INSTANCE.player().rotationYawHead - Wrapper.INSTANCE.player().rotationYaw) < 90;

			if (Wrapper.INSTANCE.player().moveForward > 0 && Wrapper.INSTANCE.player().hurtTime < 5) {
				if (Wrapper.INSTANCE.player().onGround) {
					//Wrapper.INSTANCE.player().jump();
					Wrapper.INSTANCE.player().motionY = 0.405;
					float f = Utils.getDirection();
					Wrapper.INSTANCE.player().motionX -= (double) (MathHelper.sin(f) * 0.2F);
					Wrapper.INSTANCE.player().motionZ += (double) (MathHelper.cos(f) * 0.2F);
				} else {
					double currentSpeed = Math.sqrt(Wrapper.INSTANCE.player().motionX * Wrapper.INSTANCE.player().motionX + Wrapper.INSTANCE.player().motionZ * Wrapper.INSTANCE.player().motionZ);
					double speed = boost ? 1.0064 : 1.001;

					double direction = Utils.getDirection();

					Wrapper.INSTANCE.player().motionX = -Math.sin(direction) * speed * currentSpeed;
					Wrapper.INSTANCE.player().motionZ = Math.cos(direction) * speed * currentSpeed;
				}
			}
		}else {
			if (Wrapper.INSTANCE.player().moveForward > 0 && Wrapper.INSTANCE.player().hurtTime < 5) {
				//double currentSpeed = Math.sqrt(Wrapper.INSTANCE.player().motionX * Wrapper.INSTANCE.player().motionX + Wrapper.INSTANCE.player().motionZ * Wrapper.INSTANCE.player().motionZ);
				double direction = Utils.getDirection();
				Wrapper.INSTANCE.player().motionX = -Math.sin(direction) * speedV.value;
				Wrapper.INSTANCE.player().motionZ = Math.cos(direction) * speedV.value;
			}
		}
		super.onClientTick(event);
	}
	
}
