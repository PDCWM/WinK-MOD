package i.WinKcode.hack.hacks.combat;

import com.mojang.authlib.GameProfile;
import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.BlockUtils;
import i.WinKcode.utils.EntityBot;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.system.Connection.Side;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.value.types.IntegerValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

import java.util.ArrayList;
import java.util.UUID;

public class AntiBot extends Hack{
	
	public static ArrayList<EntityBot> bots = new ArrayList<EntityBot>();
	
	public IntegerValue level;
	public IntegerValue tick;
	
	public BooleanValue ifInAir;
	public BooleanValue ifGround;
	public BooleanValue ifZeroHealth;
	public BooleanValue ifInvisible;
	public BooleanValue ifEntityId;
	public BooleanValue ifTabName;
	public BooleanValue ifPing;
	
	public BooleanValue remove;
	public BooleanValue gwen;
	
	public AntiBot() {
		super("AntiBot", HackCategory.COMBAT);
		this.GUIName = "反机器人";

		level = new IntegerValue("AI等级", 0, 0, 6);
		tick = new IntegerValue("时钟存活", 0, 0, 999);
		
		ifInvisible = new BooleanValue("隐形", false);
		ifInAir = new BooleanValue("在空中", false);
		ifGround = new BooleanValue("在地面", false);
		ifZeroHealth = new BooleanValue("零健康", false);
		ifEntityId = new BooleanValue("实体ID", false);
		ifTabName = new BooleanValue("输出标签名称", false);
		ifPing = new BooleanValue("Ping检查", false);
		
		remove = new BooleanValue("删除机器人", false);
		gwen = new BooleanValue("Gwen", false);
		
		this.addValue(level, tick, remove, gwen, ifInvisible, ifInAir, ifGround, ifZeroHealth, ifEntityId, ifTabName, ifPing);
	}
	
	@Override
	public String getDescription() {
		return "忽略/删除反作弊机器人.";
	}
	
	@Override
	public void onEnable() {
		bots.clear();
		super.onEnable();
	}
	
	@Override
	public boolean onPacket(Object packet, Side side) {
		if(gwen.getValue()) {
			for (Object entity : Utils.getEntityList()) {
				if (packet instanceof SPacketSpawnPlayer) {
					SPacketSpawnPlayer spawn = (SPacketSpawnPlayer) packet;
						double posX = spawn.getX() / 32.0D;
						double posY = spawn.getY() / 32.0D;
						double posZ = spawn.getZ() / 32.0D;
					  
						double difX = Wrapper.INSTANCE.player().posX - posX;
						double difY = Wrapper.INSTANCE.player().posY - posY;
						double difZ = Wrapper.INSTANCE.player().posZ - posZ;

						double dist = Math.sqrt(difX * difX + difY * difY + difZ * difZ);
					  	if ((dist <= 17.0D) && (posX != Wrapper.INSTANCE.player().posX) && (posY != Wrapper.INSTANCE.player().posY) && (posZ != Wrapper.INSTANCE.player().posZ)) {
						  	return false;
					  	}
				}
			}
		}
		return true;
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if (tick.getValue() > 0.0) {
			bots.clear();
        }
		for(Object object : Utils.getEntityList()) {
			if(object instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) object;
				if (!(entity instanceof EntityPlayerSP) 
						&& entity instanceof EntityPlayer
						&& !(entity instanceof EntityArmorStand)
						&& entity != Wrapper.INSTANCE.player()) {
					EntityPlayer bot = (EntityPlayer)entity;
					if(!isBotBase(bot)) {
						int ailevel = level.getValue();
						boolean isAi = ailevel > 0.0;
						if(isAi && botPercentage(bot) > ailevel) {
							addBot(bot);
						} 
						else if(!isAi && botCondition(bot)) {
							addBot(bot);
						}
					} else {
						addBot(bot);
						if(remove.getValue()) {
							Wrapper.INSTANCE.world().removeEntity(bot);
						}
					}
		        }
			}
		}
		super.onClientTick(event);
	}
	
	void addBot(EntityPlayer player) {
		if(!isBot(player)) {
			bots.add(new EntityBot(player));
		}
	}
	
	public static boolean isBot(EntityPlayer player) {
		for(EntityBot bot : bots) {
			if(bot.getName().equals((player.getName()))){
				if(player.isInvisible() != bot.isInvisible()) {
					return player.isInvisible();
				}
				return true;
			}
			else {
				if(bot.getId() == player.getEntityId() 
						|| bot.getUuid().equals(player.getGameProfile().getId())) {
					return true;
				}
			}
		}
		return false;
	}
	
	boolean botCondition(EntityPlayer bot) {
		int percentage = 0;
		if (tick.getValue() > 0.0 && bot.ticksExisted < tick.getValue()) {
			return true;
        }
		if (ifInAir.getValue()
				&& bot.isInvisible() 
				&& bot.motionY == 0.0 
				&& bot.posY > Wrapper.INSTANCE.player().posY + 1.0 
				&& BlockUtils.isBlockMaterial(new BlockPos(bot).down(), Blocks.AIR)) {
			return true;
        }
		if (ifGround.getValue()
				&& bot.motionY == 0.0 
        		&& !bot.collidedVertically 
        		&& bot.onGround 
        		&& bot.posY % 1.0 != 0.0 
        		&& bot.posY % 0.5 != 0.0) {
			return true;
        }
		if(ifZeroHealth.getValue() && bot.getHealth() <= 0) {
			return true;
		}
		if (ifInvisible.getValue() && bot.isInvisible()) {
			return true;
		}
		if (ifEntityId.getValue() && bot.getEntityId() >= 1000000000) {
			return true;
        }
		if(ifTabName.getValue()) {
		boolean isTabName = false;
			for (NetworkPlayerInfo npi : Wrapper.INSTANCE.mc().getConnection().getPlayerInfoMap()) {
        		if(npi.getGameProfile() != null) {
        			if(npi.getGameProfile().getName().contains(bot.getName())) {
        				isTabName = true;	
        			}
        		}
			}
			if(!isTabName) {
				return true;
			}
		}
		return false;
	}
	
	int botPercentage(EntityPlayer bot) {
		int percentage = 0;
		if (tick.getValue() > 0.0 && bot.ticksExisted < tick.getValue()) {
			percentage++;
        }
		if (ifInAir.getValue()
				&& bot.isInvisible() 
				&& bot.posY > Wrapper.INSTANCE.player().posY + 1.0 
				&& BlockUtils.isBlockMaterial(new BlockPos(bot).down(), Blocks.AIR)) {
			percentage++;
        }
		if (ifGround.getValue()
				&& bot.motionY == 0.0 
        		&& !bot.collidedVertically 
        		&& bot.onGround 
        		&& bot.posY % 1.0 != 0.0 
        		&& bot.posY % 0.5 != 0.0) {
			percentage++;
        }
		if(ifZeroHealth.getValue() && bot.getHealth() <= 0) {
			percentage++;
		}
		if (ifInvisible.getValue() && bot.isInvisible()) {
			percentage++;
		}
		if (ifEntityId.getValue() && bot.getEntityId() >= 1000000000) {
			percentage++;
        }
		if(ifTabName.getValue()) {
		boolean isTabName = false;
			for (NetworkPlayerInfo npi : Wrapper.INSTANCE.mc().getConnection().getPlayerInfoMap()) {
        		if(npi.getGameProfile() != null) {
        			if(npi.getGameProfile().getName().contains(bot.getName())) {
        				isTabName = true;	
        			}
        		}
			}
			if(!isTabName) {
				percentage++;
			}
		}
		return percentage;
	}
	
	boolean isBotBase(EntityPlayer bot) {
		if(isBot(bot)) {
			return true;
		}
		if(bot.getGameProfile() == null) {
			return true;
		}
		GameProfile botProfile = bot.getGameProfile();
		if(bot.getUniqueID() == null) {
			return true;
		}
		UUID botUUID = bot.getUniqueID();
		if(botProfile.getName() == null) {
			return true;
		}
		String botName = botProfile.getName();
		if(botName.contains("Body #") || botName.contains("NPC") 
				|| botName.equalsIgnoreCase(Utils.getEntityNameColor(bot))) {
			return true;
		}
		return false;
	}
}