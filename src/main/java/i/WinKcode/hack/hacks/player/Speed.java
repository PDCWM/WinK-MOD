package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;

import i.WinKcode.utils.Utils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Speed extends Hack{
	
	public Speed() {
		super("Speed", HackCategory.PLAYER);
		this.GUIName = "速度";
	}
	
	@Override
	public String getDescription() {
		return "让你移动得更快.";
	}
    
	@Override
	public void onClientTick(ClientTickEvent event) {
		boolean boost = Math.abs(Wrapper.INSTANCE.player().rotationYawHead - Wrapper.INSTANCE.player().rotationYaw) < 90;
		
      if (Wrapper.INSTANCE.player().moveForward > 0 && Wrapper.INSTANCE.player().hurtTime < 5) {
          if (Wrapper.INSTANCE.player().onGround) {
//            Wrapper.INSTANCE.player().jump();
        	  Wrapper.INSTANCE.player().motionY = 0.405;
              float f = Utils.getDirection();
              Wrapper.INSTANCE.player().motionX -= (double)(MathHelper.sin(f) * 0.2F);
              Wrapper.INSTANCE.player().motionZ += (double)(MathHelper.cos(f) * 0.2F);
          } else {
              double currentSpeed = Math.sqrt(Wrapper.INSTANCE.player().motionX * Wrapper.INSTANCE.player().motionX + Wrapper.INSTANCE.player().motionZ * Wrapper.INSTANCE.player().motionZ);
              double speed = boost ? 1.0064 : 1.001;

              double direction = Utils.getDirection();

              Wrapper.INSTANCE.player().motionX = -Math.sin(direction) * speed * currentSpeed;
              Wrapper.INSTANCE.player().motionZ = Math.cos(direction) * speed * currentSpeed;
          }
      }
		super.onClientTick(event);
	}
	
}