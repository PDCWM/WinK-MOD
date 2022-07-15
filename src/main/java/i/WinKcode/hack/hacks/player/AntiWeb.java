package i.WinKcode.hack.hacks.player;

import java.lang.reflect.Field;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;

import i.WinKcode.utils.system.Mapping;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class AntiWeb extends Hack{
	
	public AntiWeb() {
		super("AntiWeb", HackCategory.PLAYER);
		this.GUIName = "无视蜘蛛网";
	}
	
	@Override
	public String getDescription() {
		return "不会改变蜘蛛网中的步行速度.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		try {
			Field isInWeb = Entity.class.getDeclaredField(Mapping.isInWeb);
			isInWeb.setAccessible(true);
			isInWeb.setBoolean(Wrapper.INSTANCE.player(), false);
		} catch (Exception ex) {
			this.setToggled(false);
		}
		super.onClientTick(event);
	}
	
}
