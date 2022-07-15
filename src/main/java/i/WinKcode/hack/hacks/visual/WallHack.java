package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.managers.HackManager;
import i.WinKcode.utils.Utils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

public class WallHack extends Hack{
    
	public WallHack() {
		super("WallHack", HackCategory.VISUAL);
		this.GUIName = "火眼金睛";
	}
	
	@Override
	public String getDescription() {
		return "无视墙壁遮挡显示实体.";
	}
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT);
		RenderHelper.enableStandardItemLighting();
		for (Object object : Utils.getEntityList()) {
	    	  Entity entity = (Entity)object;
	    	  this.render(entity, event.getPartialTicks());
		}
		super.onRenderWorldLast(event);
	}
	
	void render(Entity entity, float ticks) {
		Entity ent = checkEntity(entity);
    	if(ent == null || ent == Wrapper.INSTANCE.player()) return;
    	if (ent == Wrapper.INSTANCE.mc().getRenderViewEntity() 
    			&& Wrapper.INSTANCE.mcSettings().thirdPersonView == 0) return;
    	Wrapper.INSTANCE.mc().entityRenderer.disableLightmap();
		Wrapper.INSTANCE.mc().getRenderManager().renderEntityStatic(ent, ticks, false);
		Wrapper.INSTANCE.mc().entityRenderer.enableLightmap();
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
