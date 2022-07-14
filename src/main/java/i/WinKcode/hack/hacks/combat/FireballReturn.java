package i.WinKcode.hack.hacks.combat;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.RobotUtils;
import i.WinKcode.utils.TimerUtils;
import i.WinKcode.utils.Utils;
import i.WinKcode.value.types.DoubleValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class FireballReturn extends Hack{
	
    public DoubleValue yaw;
    public DoubleValue pitch;
    public DoubleValue range;
    
	public EntityFireball target;
	public TimerUtils timer;
	
	public FireballReturn() {
		super("FireballReturn", HackCategory.COMBAT);
		this.GUIName = "火球反弹";
		yaw = new DoubleValue("偏移", 25.0D, 0D, 50D);
		pitch = new DoubleValue("倾斜", 25.0D, 0D, 50D);
		range = new DoubleValue("范围", 10.0D, 0.1D, 10D);
		
		this.addValue(yaw, pitch, range);
		
		this.timer = new TimerUtils();
	}
	
	@Override
	public String getDescription() {
		return "当火球飞向你时击败它们.";
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		updateTarget();
		attackTarget();
		super.onClientTick(event);
	}

	void updateTarget(){
		for (Object object : Utils.getEntityList()) {
			if(object instanceof EntityFireball) {
				EntityFireball entity = (EntityFireball) object;
				if(isInAttackRange(entity) && !entity.isDead && !entity.onGround && entity.canBeAttackedWithItem()) {
					target = entity;
				}
			}
		}
	}
	
	void attackTarget() {
		if(target == null) {
			return;
		}
		Utils.assistFaceEntity(this.target, this.yaw.getValue().floatValue(), this.pitch.getValue().floatValue(), false);
		int currentCPS = Utils.random(4, 7);
		if(timer.isDelay(1000 / currentCPS)) {
			RobotUtils.clickMouse(0);
			timer.setLastMS();
			target = null;
		}
	}
	
	public boolean isInAttackRange(EntityFireball entity) {
        return entity.getDistance(Wrapper.INSTANCE.player()) <= this.range.getValue().floatValue();
    }
	
}
