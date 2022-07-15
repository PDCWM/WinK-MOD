package i.WinKcode.hack.hacks.combat;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.ValidUtils;
import i.WinKcode.value.types.DoubleValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class HitBox extends Hack{
	
	public DoubleValue width;
	public DoubleValue height;
	
	public HitBox() {
		super("HitBox", HackCategory.COMBAT);
		this.GUIName = "命中框";
		
		this.width = new DoubleValue("宽度", 0.6D, 0.1D, 5.0D);
		this.height = new DoubleValue("高度", 1.8D, 0.1D, 5.0D);
		
		this.addValue(width, height);
	}
	
	@Override
	public String getDescription() {
		return "改变玩家的命中框大小.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		for(EntityPlayer player : Utils.getPlayersList()) {
			if(!check(player)) continue;
			float width = (float)(this.width.getValue().floatValue());
			float height = (float)(this.height.getValue().floatValue());
			Utils.setEntityBoundingBoxSize(player, width, height);
		}
		super.onClientTick(event);
	}
	
	@Override
	public void onDisable() {
		for(EntityPlayer player : Utils.getPlayersList())
			Utils.setEntityBoundingBoxSize(player);
		super.onDisable();
	}
	
	public boolean check(EntityLivingBase entity) {
		if(entity instanceof EntityPlayerSP) { return false; }
		if(entity == Wrapper.INSTANCE.player()) { return false; }
		if(entity.isDead) { return false; }
		if(!ValidUtils.isFriendEnemy(entity)) { return false; }
		if(!ValidUtils.isTeam(entity)) { return false; }
		return true;
    }
}