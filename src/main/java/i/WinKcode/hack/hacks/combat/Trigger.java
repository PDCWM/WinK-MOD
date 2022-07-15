package i.WinKcode.hack.hacks.combat;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;
import i.WinKcode.hack.hacks.combat.AutoShield;
import i.WinKcode.utils.TimerUtils;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.ValidUtils;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.value.types.IntegerValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Trigger extends Hack {

	public BooleanValue autoDelay;
	public BooleanValue advanced;

	public IntegerValue minCPS;
	public IntegerValue maxCPS;

	public EntityLivingBase target;

	public TimerUtils timer;

	public Trigger() {
		super("Trigger", HackCategory.COMBAT);
		this.GUIName = "扳机";

		autoDelay = new BooleanValue("自动延迟", true);
		advanced = new BooleanValue("先进的", false);

		minCPS = new IntegerValue("MinCPS", 4, 1, 20);
		maxCPS = new IntegerValue("MaxCPS", 8, 1, 20);

		this.addValue(advanced, autoDelay, minCPS, maxCPS);

		timer = new TimerUtils();
	}

	@Override
	public String getDescription() {
		return "自动攻击您正在查看的实体.";
	}

	@Override
	public void onDisable() {
		this.target = null;
		AutoShield.block(false);
		super.onDisable();
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		updateTarget();
		attackTarget(target);
		super.onClientTick(event);
	}

	void attackTarget(EntityLivingBase target) {
		if (check(target)) {
			if (this.autoDelay.getValue()) {
				if (Wrapper.INSTANCE.player().getCooledAttackStrength(0) == 1)
					processAttack(target, false);
			} else {
				int currentCPS = Utils.random(minCPS.getValue(), maxCPS.getValue());
				if (timer.isDelay(1000 / currentCPS)) {
					processAttack(target, true);
					timer.setLastMS();
				}
			}
			return;
		}
		AutoShield.block(false);
	}

	public void processAttack(EntityLivingBase entity, boolean packet) {
		AutoShield.block(false);
		float sharpLevel = EnchantmentHelper.getModifierForCreature(Wrapper.INSTANCE.player().getHeldItemMainhand(),
				target.getCreatureAttribute());
		if (packet)
			Wrapper.INSTANCE.sendPacket(new CPacketUseEntity(target));
		else
			Utils.attack(target);
		Utils.swingMainHand();
		if (sharpLevel > 0.0f) {
			Wrapper.INSTANCE.player().onEnchantmentCritical(target);
		}
		AutoShield.block(true);
	}

	void updateTarget() {
		RayTraceResult object = Wrapper.INSTANCE.mc().objectMouseOver;
		if (object == null) {
			return;
		}
		EntityLivingBase entity = null;
		if (this.target != entity) {
			this.target = null;
		}
		if (object.typeOfHit == RayTraceResult.Type.ENTITY) {
			if (object.entityHit instanceof EntityLivingBase) {
				entity = (EntityLivingBase) object.entityHit;
				this.target = entity;
			}
		} else if (object.typeOfHit != RayTraceResult.Type.ENTITY && advanced.getValue()) {
			entity = getClosestEntity();
		}
		if (entity != null) {
			this.target = entity;
		}
	}

	EntityLivingBase getClosestEntity() {
		EntityLivingBase closestEntity = null;
		for (Object o : Utils.getEntityList()) {
			if (o instanceof EntityLivingBase && !(o instanceof EntityArmorStand)) {
				EntityLivingBase entity = (EntityLivingBase) o;
				if (check(entity)) {
					if (closestEntity == null || Wrapper.INSTANCE.player().getDistance(entity) < Wrapper.INSTANCE
							.player().getDistance(closestEntity)) {
						closestEntity = entity;
					}
				}
			}
		}
		return closestEntity;
	}

	public boolean check(EntityLivingBase entity) {
		if (entity instanceof EntityArmorStand) {
			return false;
		}
		if (ValidUtils.isValidEntity(entity)) {
			return false;
		}
		if (!ValidUtils.isNoScreen()) {
			return false;
		}
		if (entity == Wrapper.INSTANCE.player()) {
			return false;
		}
		if (entity.isDead) {
			return false;
		}
		if (ValidUtils.isBot(entity)) {
			return false;
		}
		if (!ValidUtils.isFriendEnemy(entity)) {
			return false;
		}
		if (!ValidUtils.isInvisible(entity)) {
			return false;
		}
		if (advanced.getValue()) {
			if (!ValidUtils.isInAttackFOV(entity, 50)) {
				return false;
			}
			if (!ValidUtils.isInAttackRange(entity, 4.7F)) {
				return false;
			}
		}
		if (!ValidUtils.isTeam(entity)) {
			return false;
		}
		if (!ValidUtils.pingCheck(entity)) {
			return false;
		}
		if (!Wrapper.INSTANCE.player().canEntityBeSeen(entity)) {
			return false;
		}
		return true;
	}
}
