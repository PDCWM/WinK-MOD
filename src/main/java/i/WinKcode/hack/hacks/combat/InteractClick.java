package i.WinKcode.hack.hacks.combat;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.managers.EnemyManager;
import i.WinKcode.managers.FriendManager;
import i.WinKcode.utils.Utils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Mouse;

public class InteractClick extends Hack{

	public InteractClick() {
		super("InteractClick", HackCategory.COMBAT);
		this.GUIName = "互动点击";
	}
	
	@Override
	public String getDescription() {
		return "左 - 添加到敌人,右 - 添加到朋友,滚轮 - 从全部删除.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
		if(object == null) {
			return;
		}
		if(object.typeOfHit == RayTraceResult.Type.ENTITY) {
			Entity entity = object.entityHit;
			if(entity instanceof EntityPlayer && !(entity instanceof EntityArmorStand) && !Wrapper.INSTANCE.player().isDead && Wrapper.INSTANCE.player().canEntityBeSeen(entity)){
				EntityPlayer player = (EntityPlayer)entity;
				String ID = Utils.getPlayerName(player);
				if(Mouse.isButtonDown(1) && Wrapper.INSTANCE.mc().currentScreen == null) 
				{
					FriendManager.addFriend(ID);
				}
				else if(Mouse.isButtonDown(0) && Wrapper.INSTANCE.mc().currentScreen == null) 
				{
					EnemyManager.addEnemy(ID);
				}
				else if(Mouse.isButtonDown(2) && Wrapper.INSTANCE.mc().currentScreen == null) 
				{
					EnemyManager.removeEnemy(ID);
					FriendManager.removeFriend(ID);
				}
			}
    	}
		super.onClientTick(event);
	}

}
