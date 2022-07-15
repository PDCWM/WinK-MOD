package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;
import i.WinKcode.managers.EnemyManager;
import i.WinKcode.managers.FriendManager;
import i.WinKcode.managers.HackManager;

import i.WinKcode.utils.Utils;
import i.WinKcode.utils.ValidUtils;
import i.WinKcode.utils.visual.RenderUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Tracers extends Hack{
    
	public Tracers() {
		super("Tracers", HackCategory.VISUAL);
		this.GUIName = "目标线追";
	}
	
	@Override
    public String getDescription() {
        return "追踪一条线到目标.";
    }
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		for (Object object : Utils.getEntityList()) {
			if(object instanceof EntityLivingBase  && !(object instanceof EntityArmorStand)) {
				EntityLivingBase entity = (EntityLivingBase)object;
	    		  this.render(entity, event.getPartialTicks());
			}
		}
		super.onRenderWorldLast(event);
	}
	
	void render(EntityLivingBase entity, float ticks) {
    	if(ValidUtils.isValidEntity(entity) || entity == Wrapper.INSTANCE.player()) { 
			return;
    	}
    	if(entity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)entity;
    		String ID = Utils.getPlayerName(player);
    		if(EnemyManager.enemysList.contains(ID)) {
    			RenderUtils.drawTracer(entity, 0.8f, 0.3f, 0.0f, 1.0f, ticks);
    			return;
    		}
    		if(FriendManager.friendsList.contains(ID)) {
    			RenderUtils.drawTracer(entity, 0.0f, 0.7f, 1.0f, 1.0f, ticks);
    			return;
    		}
    	}
    	if(HackManager.getHack("Targets").isToggledValue("Murder")) {
    		if(Utils.isMurder(entity)) {
    			RenderUtils.drawTracer(entity, 1.0f, 0.0f, 0.8f, 1.0f, ticks);
        		return;
    		}
    		if(Utils.isDetect(entity)) {
    			RenderUtils.drawTracer(entity, 0.0f, 0.0f, 1.0f, 0.5f, ticks);
        		return;
    		}
		} 
    	if(entity.isInvisible()) {
			RenderUtils.drawTracer(entity, 0.0f, 0.0f, 0.0f, 0.5f, ticks);
			return;
    	}
    	if(entity.hurtTime > 0) {
    		RenderUtils.drawTracer(entity, 1.0f, 0.0f, 0.0f, 1.0f, ticks);
    		return;
    	}
    	RenderUtils.drawTracer(entity, 1.0f, 1.0f, 1.0f, 0.5f, ticks);
    }
}
