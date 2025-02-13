package i.WinKcode.utils;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.hacks.combat.AntiBot;
import i.WinKcode.managers.EnemyManager;
import i.WinKcode.managers.FriendManager;
import i.WinKcode.managers.HackManager;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ValidUtils {
	
	public static boolean isLowHealth(EntityLivingBase entity, EntityLivingBase entityPriority) {
		return entityPriority == null || entity.getHealth() < entityPriority.getHealth();
	}
	
	public static boolean isClosest(EntityLivingBase entity, EntityLivingBase entityPriority) {
		return entityPriority == null || Wrapper.INSTANCE.player().getDistance(entity) < Wrapper.INSTANCE.player().getDistance(entityPriority);
	}
	
    public static boolean isInAttackFOV(EntityLivingBase entity, int fov) {
        return Utils.getDistanceFromMouse(entity) <= fov;
    }
    
    public static boolean isInAttackRange(EntityLivingBase entity, float range) {
        return entity.getDistance(Wrapper.INSTANCE.player()) <= range;
    }
	
    public static boolean isValidEntity(EntityLivingBase e) {
		Hack targets = HackManager.getHack("Targets");
		if(targets.isToggled()) {
			if(targets.isToggledValue("玩家") && e instanceof EntityPlayer) {
				return false;
			} 
			else 
			if(targets.isToggledValue("生物") && e instanceof EntityLiving) {
				return false;
			}
		}
		return true;	
	}
    
	
	public static boolean pingCheck(EntityLivingBase entity) {
		Hack hack = HackManager.getHack("AntiBot");
		if(hack.isToggled() && hack.isToggledValue("Ping检查") && entity instanceof EntityPlayer) {
			if (Wrapper.INSTANCE.mc().getConnection().getPlayerInfo(entity.getUniqueID()) != null) {
				if (Wrapper.INSTANCE.mc().getConnection().getPlayerInfo(entity.getUniqueID()).getResponseTime() > 5) {
					return true;
				}
			}
        	return false;
		}
		return true;
	}
    
	public static boolean isBot(EntityLivingBase entity) {
    	if(entity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)entity;
    		Hack hack = HackManager.getHack("AntiBot");
			return hack.isToggled() && AntiBot.isBot(player);
    	}
    	return false;
    }
    
	public static boolean isFriendEnemy(EntityLivingBase entity) {
		if(entity instanceof EntityPlayer) {
    		EntityPlayer player = (EntityPlayer)entity;
    		String ID = Utils.getPlayerName(player);
    		if(FriendManager.friendsList.contains(ID)) {
    			return false;
    		}
    		if(HackManager.getHack("Enemys").isToggled()) {
    			if(!EnemyManager.enemysList.contains(ID)) {
    				return false;
    			}
    		}
    	}
		return true;
	}
	public static boolean isTeam(EntityLivingBase entity) {
		Hack teams = HackManager.getHack("Teams");
		if(teams.isToggled()) {
			if(entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				if(teams.isToggledMode("Base")) {
					if(player.getTeam() != null && Wrapper.INSTANCE.player().getTeam() != null) {
						if(player.getTeam().isSameTeam(Wrapper.INSTANCE.player().getTeam())){
							return false;
						}
					}
				}
				if(teams.isToggledMode("ArmorColor")) {
					if(!Utils.checkEnemyColor(player)) {
						return false;
					}
				}
				if(teams.isToggledMode("NameColor")) {
					if(!Utils.checkEnemyNameColor(player)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	public static boolean isInvisible(EntityLivingBase entity) {
		Hack targets = HackManager.getHack("Targets");
		if(!targets.isToggledValue("隐形人")) {
			if(entity.isInvisible()) {
				return false;
			}
		}
		return true;
	}
	public static boolean isNoScreen() {
		if(HackManager.getHack("NoGuiEvents").isToggled()) {
			if(!Utils.screenCheck()) {
				return false;
			}
		}
		return true;
	}
}
