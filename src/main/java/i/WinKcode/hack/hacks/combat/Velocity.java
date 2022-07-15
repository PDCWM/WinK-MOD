package i.WinKcode.hack.hacks.combat;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.system.Connection.Side;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Velocity extends Hack{
	
	public ModeValue mode;
	
	//public NumberValue percentage;
	
	public Velocity() {
		super("Velocity", HackCategory.COMBAT);
		this.GUIName = "金钟罩";
		
		this.mode = new ModeValue("模式", new Mode("AAC", false), new Mode("简易", true));
		
		this.addValue(mode);
	}
	
	@Override
	public String getDescription() {
		return "防止你被玩家、怪物和流水推动.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(mode.getMode("AAC").isToggled()) {
			EntityPlayerSP player = Wrapper.INSTANCE.player();
			if(player.hurtTime > 0 && player.hurtTime <= 7) {
				player.motionX *= 0.5;
						player.motionZ *= 0.5;
		      }
		      if(player.hurtTime > 0 && player.hurtTime < 6) {
		    	  player.motionX = 0.0;
		    			  player.motionZ = 0.0;
		      }
		}
		super.onClientTick(event);
	}
	
	@Override
	public boolean onPacket(Object packet, Side side) {
		if(packet instanceof SPacketEntityVelocity && mode.getMode("简易").isToggled()) {
			SPacketEntityVelocity p = (SPacketEntityVelocity)packet;
			if(p.getEntityID() == Wrapper.INSTANCE.player().getEntityId()) {
				return false;
				/*
				double x = p.getMotionX() / 8000.0D;
			    double y = p.getMotionY() / 8000.0D;
			    double z = p.getMotionZ() / 8000.0D;  
			    double percent = this.percentage.getValue() / 100.0D;
			    x *= percent;
			    y *= percent;
			    z *= percent;
			    if(this.percentage.getValue() > 0) {
			    	try {
						ChatUtils.warning("x" + x + " y" + y + " z" + z);
			        	Field mX = SPacketEntityVelocity.class.getDeclaredField("motionX");
			        	Field mY = SPacketEntityVelocity.class.getDeclaredField("motionY");
			        	Field mZ = SPacketEntityVelocity.class.getDeclaredField("motionZ");  	
			        	mX.setAccessible(true);
			        	mY.setAccessible(true);
			        	mZ.setAccessible(true);
			        	mX.set(p, (int)x);
			        	mY.set(p, (int)y);
			        	mZ.set(p, (int)z);
					    //return true;
					} catch (Exception e) {
						e.printStackTrace();
					}
								  
			    }
			    */
			}
		}
		return true;
	}
}
