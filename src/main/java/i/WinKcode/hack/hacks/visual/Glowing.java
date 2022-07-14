package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.HackCategory;

import i.WinKcode.hack.Hack;
import i.WinKcode.managers.HackManager;

import i.WinKcode.utils.Utils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class Glowing extends Hack{
    
	public Glowing() {
		super("Glowing", HackCategory.VISUAL);
		this.GUIName = "实体发光";
	}
	
	@Override
	public String getDescription() {
		return "你周围实体会发光.";
	}
	
	@Override
	public void onDisable() {
		for (Object object : Utils.getEntityList()) {
	    	  Entity entity = (Entity)object;
	    	  if(entity.isGlowing()) {
	    		  entity.setGlowing(false);
	    	  }
		}
		super.onDisable();
	}
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		for (Object object : Utils.getEntityList()) {
	    	  Entity entity = (Entity)object;
	    	  if(checkEntity(entity) != null && entity != Wrapper.INSTANCE.player()) { 
	    		  if(!entity.isGlowing()) {
	    			  entity.setGlowing(true);
	    		  }
	      	}
		}
		super.onRenderWorldLast(event);
	}
	
	Entity checkEntity(Entity e) {
		Entity entity = null;
		Hack targets = HackManager.getHack("Targets");
		if(targets.isToggledValue("玩家") && e instanceof EntityPlayer) {
			entity = e;
		} else if(targets.isToggledValue("生物") && e instanceof EntityLiving) {
			entity = e;
		} else if(e instanceof EntityItem) {
			entity = e;
		} else if(e instanceof EntityArrow) {
			entity = e;
		}
		return entity;	
	}
}
