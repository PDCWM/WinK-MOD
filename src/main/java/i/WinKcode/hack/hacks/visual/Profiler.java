package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.managers.EnemyManager;
import i.WinKcode.managers.FriendManager;
import i.WinKcode.utils.MathUtils;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.ValidUtils;
import i.WinKcode.utils.visual.ColorUtils;
import i.WinKcode.utils.visual.RenderUtils;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Profiler extends Hack{
	
	public BooleanValue armor;
    
	public Profiler() {
		super("Profiler", HackCategory.VISUAL);
		this.GUIName = "目标探查";
		
	    this.armor = new BooleanValue("盔甲", true);
	    
	    this.addValue(armor);
	}
	
	@Override
	public String getDescription() {
		return "允许您查看实体目标的盔甲或物品信息.";
	}
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		for (Object object : Utils.getEntityList()) {
			if(object instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase)object;
				RenderManager renderManager = Wrapper.INSTANCE.mc().getRenderManager();
            	double renderPosX = renderManager.viewerPosX;
            	double renderPosY = renderManager.viewerPosY;
            	double renderPosZ = renderManager.viewerPosZ;
            	double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks()) - renderPosX;
            	double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks()) - renderPosY;
            	double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks()) - renderPosZ;
            
            	this.renderNameTag(entity, entity.getName(), xPos, yPos, zPos);
			}
		}
		super.onRenderWorldLast(event);
	}

	private int lastSs = 0;

	void renderNameTag(EntityLivingBase entity, String tag, double x, double y, double z) {
    	if(entity instanceof EntityArmorStand || ValidUtils.isValidEntity(entity) || entity == Wrapper.INSTANCE.player()) { 
			return;
    	}
    	
    	int color = ColorUtils.color(0, 0, 0, 130);
    	EntityPlayerSP player = Wrapper.INSTANCE.player();
    	FontRenderer fontRenderer = Wrapper.INSTANCE.fontRenderer();
    	if(player.canEntityBeSeen(entity))
    		color = ColorUtils.color(139,139,139,160);
    	y += (entity.isSneaking() ? 0.5D : 0.7D);
    	float distance = player.getDistance(entity) / 3.5F;
    	if (distance < 2.0F) {
    		distance = 2.0F;
 	    }
    	
    	if(entity instanceof EntityPlayer) {
    		EntityPlayer entityPlayer = (EntityPlayer)entity;
    		String ID = Utils.getPlayerName(entityPlayer);

			color = ColorUtils.color(
					ColorUtils.rainbow().getRed(),
					ColorUtils.rainbow().getGreen(),
					ColorUtils.rainbow().getBlue(),
					200);

    		if(EnemyManager.enemysList.contains(ID)) {
   	         	tag = "\u00a7c" + ID;
   	         	color = ColorUtils.color(179, 20, 20, 200);
    		}
    		if(FriendManager.friendsList.contains(ID)) {
    			tag = "\u00a73" + ID;
    			color = ColorUtils.color(66, 147, 179, 200);
    		}
    		if(ValidUtils.isBot(entityPlayer)) {
    			tag = "\u00a7e" + ID;
   	         	color = ColorUtils.color(200, 200, 0, 200);
    		}
    	}
    	
    	int health = (int)entity.getHealth();
    	int xtColor = ColorUtils.color(50,205,50,180);
	     if (health <= entity.getMaxHealth() * 0.25D) {
	       	//tag = tag + "\u00a74";
			 if(lastSs++ < 10){
				 xtColor = ColorUtils.color(255,0,0,180);
			 }else{ xtColor = color; }
			 if(lastSs >= 15)
			 	lastSs = 0;
	     } else if (health <= entity.getMaxHealth() * 0.5D) {
	       	//tag = tag + "\u00a76";
	       	xtColor = ColorUtils.color(255,165,0,180);
	     } else if (health <= entity.getMaxHealth() * 0.75D) {
	       	//tag = tag + "\u00a7e";
	       	xtColor = ColorUtils.color(144,238,144,180);
	     } else if (health <= entity.getMaxHealth()) {
	       	//tag = tag + "\u00a72";
	       	xtColor = ColorUtils.color(50,205,50,180);
	     }

	     float distanceV = player.getDistance(entity);
	     tag = tag + " " + Math.round(distanceV) + "m";
	     
	     RenderManager renderManager = Wrapper.INSTANCE.mc().getRenderManager();
	     float scale = distance;
	     scale /= 30.0F;
	     scale = (float)(scale * 0.3D);
	     GL11.glPushMatrix();
	     GL11.glTranslatef((float)x, (float)y + 1.4F, (float)z);
	     GL11.glNormal3f(1.0F, 1.0F, 1.0F);
	     GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
	     GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
	     GL11.glScalef(-scale, -scale, scale);
	     GL11.glDisable(2896);
	     GL11.glDisable(2929);
	     Tessellator var14 = Tessellator.getInstance();
	     BufferBuilder var15 = var14.getBuffer();
	     int tagWidth = fontRenderer.getStringWidth(tag) / 2;
	     GL11.glEnable(3042);
	     GL11.glBlendFunc(770, 771);
	     RenderUtils.drawRect(-tagWidth - 2, -(fontRenderer.FONT_HEIGHT + 1), tagWidth + 2, 2.0F, color);
//	     RenderUtils.drawRect(-width - 3.0F, -(fontRenderer.FONT_HEIGHT + 1) - 1.0F, width + 3.0F, -(fontRenderer.FONT_HEIGHT + 1), color);
//	     RenderUtils.drawRect(-width - 3.0F, 3.0F, width + 3.0F, 2.0F, color);
//	     RenderUtils.drawRect(-width - 3.0F, -(fontRenderer.FONT_HEIGHT + 1) - 1.0F, -width - 2, 3.0F, color);
//	     RenderUtils.drawRect(width + 3.0F, -(fontRenderer.FONT_HEIGHT + 1) - 1.0F, width + 2, 3.0F, color);
	     fontRenderer.drawString(tag, MathUtils.getMiddle(-tagWidth - 2, tagWidth + 2) - tagWidth, -(fontRenderer.FONT_HEIGHT - 1), Color.WHITE.getRGB(), false);
		float bHealth = entity.getHealth() / entity.getMaxHealth();
		if (entity instanceof EntityPlayer)
		{
			float bottom = 120F - ((Math.min(distanceV, 22.5F)) * 4.0F);
			float width = -40F + Math.min(distanceV, 38F);
	    	RenderUtils.drawRect(width, 4F, width + 4F, bottom, color);
	     	RenderUtils.drawRect(width + 0.5F, 4.5F, width + 3.5F, (bottom - 0.5F) * bHealth, xtColor);
	     	if(this.armor.getValue()) {
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				GlStateManager.translate(0.0F, 1.0F, 0.0F);
				renderArmor(entityPlayer, 0, -(fontRenderer.FONT_HEIGHT + 1) - 20);
				GlStateManager.translate(0.0F, -1.0F, 0.0F);
			}
		}else {
			RenderUtils.drawRect((-tagWidth - 2) * bHealth, -2, (tagWidth + 2) * bHealth, 2.0F, xtColor);
		}
		GL11.glPushMatrix();
		GL11.glPopMatrix();
		GL11.glEnable(2896);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
	   
		public void renderArmor(EntityPlayer player, int x, int y) {
		     InventoryPlayer items = player.inventory;
		     ItemStack MainHand = player.getHeldItemMainhand();
		     ItemStack OffHand = player.getHeldItemOffhand();
		     ItemStack boots = items.armorItemInSlot(0);
		     ItemStack leggings = items.armorItemInSlot(1);
		     ItemStack body = items.armorItemInSlot(2);
		     ItemStack helm = items.armorItemInSlot(3);
		     ItemStack[] stuff = null;

			 stuff = new ItemStack[] { MainHand, helm, body, leggings, boots, OffHand };

		     List<ItemStack> stacks = new ArrayList();
		     ItemStack[] array;
		     int length = (array = stuff).length;
		     
		     for (int j = 0; j < length; j++)
		     {
		       ItemStack i = array[j];
		       if ((i != null) && (i.getItem() != null)) {
		         stacks.add(i);
		       }
		     }
		     int width = 16 * stacks.size() / 2;
		     x -= width;
		     GlStateManager.disableDepth();
		     for (ItemStack stack : stacks)
		     {
		       renderItem(stack, x, y);
		       x += 16;
		     }
		     GlStateManager.enableDepth();
		   }
		   
		  public void renderItem(ItemStack stack, int x, int y) {
			 FontRenderer fontRenderer = Wrapper.INSTANCE.fontRenderer();
			 RenderItem renderItem = Wrapper.INSTANCE.mc().getRenderItem();
		     EnchantEntry[] enchants = { 
		    		 new EnchantEntry(Enchantments.PROTECTION, "Pro"),
		    		 new EnchantEntry(Enchantments.THORNS, "Th"), 
		    		 new EnchantEntry(Enchantments.SHARPNESS, "Shar"),
		    		 new EnchantEntry(Enchantments.FIRE_ASPECT, "Fire"),
		    		 new EnchantEntry(Enchantments.KNOCKBACK, "Kb"), 
		    		 new EnchantEntry(Enchantments.UNBREAKING, "Unb"), 
		    		 new EnchantEntry(Enchantments.POWER, "Pow"),
		    		 new EnchantEntry(Enchantments.INFINITY, "Inf"),
		    		 new EnchantEntry(Enchantments.PUNCH, "Punch") 
		     };
		     GlStateManager.pushMatrix();
		     GlStateManager.pushMatrix();
		     float scale1 = 0.3F;
		     GlStateManager.translate(x - 3, y + 10, 0.0F);
		     GlStateManager.scale(0.3F, 0.3F, 0.3F);
		     GlStateManager.popMatrix();
		     RenderHelper.enableGUIStandardItemLighting();
		     renderItem.zLevel = -100.0F;
		     GlStateManager.disableDepth();
		     renderItem.renderItemIntoGUI(stack, x, y);
		     renderItem.renderItemOverlayIntoGUI(fontRenderer, stack, x, y, null);
		     GlStateManager.enableDepth();
		     EnchantEntry[] array;
		     int length = (array = enchants).length; for (int i = 0; i < length; i++) {
		       EnchantEntry enchant = array[i];
		       int level = EnchantmentHelper.getEnchantmentLevel(enchant.getEnchant(), stack);
		       String levelDisplay = "" + level;
		       if (level > 10) {
		         levelDisplay = "10+";
		       }
		       if (level > 0) {
		         float scale2 = 0.32F;
		         GlStateManager.translate(x - 2, y + 1, 0.0F);
		         GlStateManager.scale(0.42F, 0.42F, 0.42F);
		         GlStateManager.disableDepth();
		         GlStateManager.disableLighting();
		         GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		         fontRenderer.drawString("\u00a7f" + enchant.getName() + " " + levelDisplay,
		        		 20 - fontRenderer.getStringWidth("\u00a7f" + enchant.getName() + " " + levelDisplay) / 2, 0, Color.WHITE.getRGB(), true);
		         GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		         GlStateManager.enableLighting();
		         GlStateManager.enableDepth();
		         GlStateManager.scale(2.42F, 2.42F, 2.42F);
		         GlStateManager.translate(-x, -y, 0.0F);
		         y += (int)((fontRenderer.FONT_HEIGHT + 3) * 0.28F);
		       }
		     }
		     renderItem.zLevel = 0.0F;
		     RenderHelper.disableStandardItemLighting();
		     GlStateManager.enableAlpha();
		     GlStateManager.disableBlend();
		     GlStateManager.disableLighting();
		     GlStateManager.popMatrix();
		 }
	
	public static class EnchantEntry {
	     private Enchantment enchant;
	     private String name;
	     
	     public EnchantEntry(Enchantment enchant, String name)
	     {
	       this.enchant = enchant;
	       this.name = name;
	     }
	     
	     public Enchantment getEnchant()
	     {
	       return this.enchant;
	     }
	     
	     public String getName()
	     {
	       return this.name;
	     }
	   }
}
