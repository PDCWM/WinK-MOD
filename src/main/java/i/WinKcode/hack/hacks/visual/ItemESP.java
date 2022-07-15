package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.visual.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class ItemESP extends Hack{
	
	public ItemESP() {
		super("ItemESP", HackCategory.VISUAL);
		this.GUIName = "物品透视";
	}
	
	@Override
	public String getDescription() {
		return "高亮附近的物品.";
	}
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		for (Object object : Utils.getEntityList()) {
			if(object instanceof EntityItem || object instanceof EntityArrow) {
				Entity item = (Entity)object;
				RenderUtils.drawESP(item, 1.0f, 1.0f, 1.0f, 1.0f, event.getPartialTicks());
			}
		}
		super.onRenderWorldLast(event);
	}
}
