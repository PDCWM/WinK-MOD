package i.WinKcode.hack.hacks.combat;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.ValidUtils;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.value.types.DoubleValue;
import i.WinKcode.value.types.IntegerValue;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;

public class AimBot extends Hack{

	public ModeValue priority;
    public BooleanValue walls;
    
    public DoubleValue yaw;
    public DoubleValue pitch;
    public DoubleValue range;
    public IntegerValue FOV;
    
    public EntityLivingBase target;
    
	public AimBot() {
		super("AimBot", HackCategory.COMBAT);
		this.GUIName = "自动瞄准";
		this.priority = new ModeValue("优先级", new Mode("最近的", true), new Mode("生命值", false));

		walls = new BooleanValue("穿墙", false);
		
		yaw = new DoubleValue("偏移", 15.0D, 0D, 50D);
		pitch = new DoubleValue("倾斜", 15.0D, 0D, 50D);
		range = new DoubleValue("范围", 4.7D, 0.1D, 100D);
		FOV = new IntegerValue("视角", 90, 1, 360);
		
		this.addValue(priority, walls, yaw, pitch, range, FOV);
	}
	
	@Override
	public String getDescription() {
		return "自动指向玩家.";
	}
	
	@Override
	public void onDisable() {
		this.target = null;
		super.onDisable();
	}

	boolean type = false;

	@Override
	public void onKeyPressed(int key){
		if(key == Keyboard.KEY_TAB) {
			type = !type;
		}
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		updateTarget();
		Utils.assistFaceEntity(
				this.target, 
				this.yaw.getValue().floatValue(),
				this.pitch.getValue().floatValue(),
				type);
		this.target = null;
		super.onClientTick(event);
	}

	void updateTarget(){
		for (Object object : Utils.getEntityList()) {
			if(!(object instanceof EntityLivingBase)) continue;
			EntityLivingBase entity = (EntityLivingBase) object;
			if(!check(entity)) continue;
			this.target = entity;
		}
	}
	
	public boolean check(EntityLivingBase entity) {
		if(entity instanceof EntityArmorStand) { return false; }
		if(ValidUtils.isValidEntity(entity)){ return false; }
		if(!ValidUtils.isNoScreen()) { return false; }
		if(entity == Wrapper.INSTANCE.player()) { return false; }
		if(entity.isDead) { return false; }
		if(ValidUtils.isBot(entity)) { return false; }
		if(!ValidUtils.isFriendEnemy(entity)) { return false; }
    	if(!ValidUtils.isInvisible(entity)) { return false; }
    	if(!ValidUtils.isInAttackFOV(entity, (FOV.getValue() / 2))) { return false; }
		if(!ValidUtils.isInAttackRange(entity, range.getValue().floatValue())) { return false; }
		if(!ValidUtils.isTeam(entity)) { return false; }
    	if(!ValidUtils.pingCheck(entity)) { return false; }
    	if(!isPriority(entity)) { return false; }
		if(!this.walls.getValue()) { if(!Wrapper.INSTANCE.player().canEntityBeSeen(entity)) { return false; } }
		return true;
    }

	boolean isPriority(EntityLivingBase entity) {
		return priority.getMode("最近的").isToggled() && ValidUtils.isClosest(entity, target)
				|| priority.getMode("生命值").isToggled() && ValidUtils.isLowHealth(entity, target);
	}

}
