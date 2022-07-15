package i.WinKcode.hack.hacks.another;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;
import i.WinKcode.managers.PickupFilterManager;
import i.WinKcode.value.types.BooleanValue;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class PickupFilter extends Hack{
	
	public BooleanValue all;
	
	public PickupFilter() {
		super("PickupFilter", HackCategory.ANOTHER);
		this.GUIName = "拾取过滤";
		
		this.all = new BooleanValue("忽略所有", false);
		this.addValue(all);
	}
	
	@Override
	public String getDescription() {
		return "筛选项目物品选择.";
	}
	
	@Override
	public void onItemPickup(EntityItemPickupEvent event) {
		if(this.all.getValue()) {
			event.setCanceled(true);
			return;
		}
		for(int itemId : PickupFilterManager.items) {
			Item item = Item.getItemById(itemId);
			if(item == null) continue;
			if(event.getItem().getItem().getItem() == item)
				event.setCanceled(true);
		}
		super.onItemPickup(event);
	}
}
