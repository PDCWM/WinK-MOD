package i.WinKcode.hack.hacks.auto;

import i.WinKcode.Main;
import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.ValidUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AiMob extends Hack {
    public AiMob() {
        super("AiMob", HackCategory.AUTO);
        this.GUIName = "自动寻怪";
    }

    private EntityLivingBase target = null;
    private BlockPos LastPos = new BlockPos(1,1,1);

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        target = null;
        for (Object object : Utils.getEntityList()) {
            if(!(object instanceof EntityLivingBase)) continue;
            EntityLivingBase entity = (EntityLivingBase) object;
            if(!check(entity)) continue;
            target = entity;
        }
        if(target == null) return;
        if(LastPos == target.getPosition()) return;
        LastPos = target.getPosition();
        if(target.onGround){
            //Main.baritoneManager.GoalBlock(LastPos);
        }else{
            //Main.baritoneManager.GoalBlock(new BlockPos(LastPos.getX(), LastPos.getY() - 1, LastPos.getZ()));
        }

    }


    boolean isPriority(EntityLivingBase entity) {
        return ValidUtils.isClosest(entity, target);
    }

    public boolean check(EntityLivingBase entity) {
        if(entity instanceof EntityArmorStand) { return false; }
        if(ValidUtils.isValidEntity(entity)) { return false; }
        if(!ValidUtils.isNoScreen()) { return false; }
        if(entity == Wrapper.INSTANCE.player()) { return false; }
        if(entity.isDead) { return false; }
        if(entity.deathTime > 0) { return false; }
        if(ValidUtils.isBot(entity)) { return false; }
        if(!ValidUtils.isFriendEnemy(entity)) { return false; }
        if(!ValidUtils.isInvisible(entity)) { return false; }
        if(!ValidUtils.isTeam(entity)) { return false; }
        if(!ValidUtils.pingCheck(entity)) { return false; }
        return isPriority(entity);
    }
}
