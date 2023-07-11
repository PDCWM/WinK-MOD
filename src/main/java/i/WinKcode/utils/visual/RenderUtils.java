package i.WinKcode.utils.visual;

import i.WinKcode.auto.Point;
import i.WinKcode.hack.hacks.auto.Scaffold;
import i.WinKcode.hack.hacks.combat.KillAura;
import i.WinKcode.hack.hacks.visual.ClickGui;
import i.WinKcode.utils.TimerUtils;
import i.WinKcode.wrappers.Wrapper;
import i.WinKcode.xray.XRayBlock;
import i.WinKcode.xray.XRayData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Locale;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils {
	
	private static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	public static TimerUtils splashTimer = new TimerUtils();
	public static int splashTickPos = 0;
	public static boolean isSplash = false;
	
	public static String DF (Number value, int maxvalue) {
	     DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
	     df.setMaximumFractionDigits(maxvalue);
	     return df.format(value);
	}
	
	public static void drawStringWithRect(String string, int x, int y, int colorString, int colorRect, int colorRect2) {
    	RenderUtils.drawBorderedRect(x - 2, y - 2, x + Wrapper.INSTANCE.fontRenderer().getStringWidth(string) + 2, y + 10, 1, colorRect, colorRect2);
    	Wrapper.INSTANCE.fontRenderer().drawString(string, x, y, colorString);
    }

    public static void drawCenterStringWithRect(String string, int x, int y, int w, int h, int colorString, int colorRect, int colorRect2, int type) {
        RenderUtils.drawBorderedWM(x - 2, y - 2, x + w + 2, y + h + 2, 1, colorRect, colorRect2, type);
        int strX = x + ((w - Wrapper.INSTANCE.fontRenderer().getStringWidth(string)) / 2);
        Wrapper.INSTANCE.fontRenderer().drawStringWithShadow(string, strX, y + (h / 2) - 4, colorString);
    }
	
	public static void drawSplash(String text) {
		ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
		drawStringWithRect(text, sr.getScaledWidth() + 2 - splashTickPos, sr.getScaledHeight() - 10, ClickGui.getColor(),
				ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F), ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F));
		if(splashTimer.isDelay(10)) {
			splashTimer.setLastMS();
			if(isSplash) {
				splashTickPos++;
				if(splashTickPos == Wrapper.INSTANCE.fontRenderer().getStringWidth(text) + 10) {
					isSplash = false;
				}
			} else {
				if(splashTickPos > 0) {
					splashTickPos--;
				}
			}
		}
	}

    public static void drawBorderedWM(double x, double y, double x2, double y2, float l1, int col1, int col2, int type) {
        drawRect((int)x, (int)y, (int)x2, (int)y2, col2);

	    if (type > 0){
            drawRect((int)x + 4, (int)y + 4, (int)x2 - 75, (int)y2 - 4, col1);
        }
	    /*if(type > 1) {
            float f = (float) (col1 >> 24 & 0xFF) / 255F;
            float f1 = (float) (col1 >> 16 & 0xFF) / 255F;
            float f2 = (float) (col1 >> 8 & 0xFF) / 255F;
            float f3 = (float) (col1 & 0xFF) / 255F;

            x += 7;
            y += 1;
            x2 -= 7;
            y2 -= 1;

            //GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);

            GL11.glPushMatrix();
            GL11.glColor4f(f1, f2, f3, f);
            GL11.glLineWidth(l1);
            GL11.glBegin(GL11.GL_LINES);
            //GL11.glVertex2d(x, y);
            GL11.glVertex2d(x, y2);
            GL11.glVertex2d(x2, y2);
            GL11.glVertex2d(x2, y);
            //GL11.glVertex2d(x, y);
            GL11.glVertex2d(x2, y);
            GL11.glVertex2d(x, y2);
            GL11.glVertex2d(x2, y2);
            GL11.glEnd();
            GL11.glPopMatrix();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            //GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }*/
    }
	
	public static void drawBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2) {
        drawRect((int)x, (int)y, (int)x2, (int)y2, col2);

        float f = (float)(col1 >> 24 & 0xFF) / 255F;
        float f1 = (float)(col1 >> 16 & 0xFF) / 255F;
        float f2 = (float)(col1 >> 8 & 0xFF) / 255F;
        float f3 = (float)(col1 & 0xFF) / 255F;

        //GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        //GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
	
	public static void drawTracer(Entity entity, float red, float green, float blue, float alpha, float ticks) {   
        double renderPosX = Wrapper.INSTANCE.mc().getRenderManager().viewerPosX;
        double renderPosY = Wrapper.INSTANCE.mc().getRenderManager().viewerPosY;
        double renderPosZ = Wrapper.INSTANCE.mc().getRenderManager().viewerPosZ;
        double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ticks) - renderPosX;
        double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ticks)  + entity.height / 2.0f - renderPosY;
        double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ticks) - renderPosZ;
        
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);
        Vec3d eyes = null;
        if(KillAura.facingCam != null) {
        	eyes = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(KillAura.facingCam[1])).rotateYaw(-(float) Math.toRadians(KillAura.facingCam[0]));
        }
        else 
        	if(Scaffold.facingCam != null) {
            	eyes = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(Scaffold.facingCam[1])).rotateYaw(-(float) Math.toRadians(Scaffold.facingCam[0]));
            }
        	else
        {
        	eyes = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(Wrapper.INSTANCE.player().rotationPitch)).rotateYaw(-(float) Math.toRadians(Wrapper.INSTANCE.player().rotationYaw));
        }
        
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(eyes.x, Wrapper.INSTANCE.player().getEyeHeight() + eyes.y, eyes.z);
        GL11.glVertex3d(xPos, yPos, zPos);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);
    }
	
	public static void drawESP(Entity entity, float colorRed, float colorGreen, float colorBlue, float colorAlpha, float ticks) {
    	try {
            double renderPosX = Wrapper.INSTANCE.mc().getRenderManager().viewerPosX;
            double renderPosY = Wrapper.INSTANCE.mc().getRenderManager().viewerPosY;
            double renderPosZ = Wrapper.INSTANCE.mc().getRenderManager().viewerPosZ;
            double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ticks) - renderPosX;
            double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ticks)  + entity.height / 2.0f - renderPosY;
            double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ticks) - renderPosZ;
            
            float playerViewY = Wrapper.INSTANCE.mc().getRenderManager().playerViewY;
            float playerViewX = Wrapper.INSTANCE.mc().getRenderManager().playerViewX;
            boolean thirdPersonView = Wrapper.INSTANCE.mc().getRenderManager().options.thirdPersonView == 2;
            
        	GL11.glPushMatrix();
        	
            GlStateManager.translate(xPos, yPos, zPos);
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((float)(thirdPersonView ? -1 : 1) * playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            GL11.glLineWidth(1.0F);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glColor4f(colorRed, colorGreen, colorBlue, colorAlpha);
            GL11.glBegin((int) 1);
            
            GL11.glVertex3d((double) 0, (double) 0+1, (double) 0.0);
            GL11.glVertex3d((double) 0-0.5, (double) 0+0.5, (double) 0.0);
            GL11.glVertex3d((double) 0, (double) 0+1, (double) 0.0);
            GL11.glVertex3d((double) 0+0.5, (double) 0+0.5, (double) 0.0);
            
            GL11.glVertex3d((double) 0, (double) 0, (double) 0.0);
            GL11.glVertex3d((double) 0-0.5, (double) 0+0.5, (double) 0.0);
            GL11.glVertex3d((double) 0, (double) 0, (double) 0.0);
            GL11.glVertex3d((double) 0+0.5, (double) 0+0.5, (double) 0.0);
            
            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        } catch (Exception exception) {
        	exception.printStackTrace();
        }
    }
	/*
	public static void drawString2D(FontRenderer fontRendererIn, String str, Entity entity, double posX, double posY, double posZ, int colorString, float colorRed, float colorGreen, float colorBlue, float colorAlpha, int verticalShift) {
		try {
			if(str != "") {
				float distance = Wrapper.INSTANCE.player().getDistance(entity);
				float playerViewY = Wrapper.INSTANCE.mc().getRenderManager().playerViewY;
				float playerViewX = Wrapper.INSTANCE.mc().getRenderManager().playerViewX;
				boolean thirdPersonView = Wrapper.INSTANCE.mc().getRenderManager().options.thirdPersonView == 2;
				float f1 = entity.height + 0.5F;
				
				if(distance <= 50) {
					GlStateManager.pushMatrix();
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glDisable(GL11.GL_DEPTH_TEST);
					GlStateManager.translate(posX, posY, posZ);
					GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
					GlStateManager.rotate(-playerViewY, 0.0F, 1.0F, 0.0F);
					GlStateManager.rotate((float)(thirdPersonView ? -1 : 1) * playerViewX, 1.0F, 0.0F, 0.0F);
        
					if(distance <= 11) {
						GlStateManager.scale(-0.027F, -0.027F, 0.027F);
					} else {
						GlStateManager.scale(-distance / 350, -distance / 350, distance / 350);
					}
					GlStateManager.disableLighting();
					GlStateManager.depthMask(false);

					GlStateManager.enableBlend();
					GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
					int i = fontRendererIn.getStringWidth(str) / 2;
					GlStateManager.disableTexture2D();
					Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder bufferbuilder = tessellator.getBuffer();
					bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
					bufferbuilder.pos((double)(-i - 1), (double)(-1 + verticalShift), 0.0D).color(colorRed, colorGreen, colorBlue, colorAlpha).endVertex();
					bufferbuilder.pos((double)(-i - 1), (double)(8 + verticalShift), 0.0D).color(colorRed, colorGreen, colorBlue, colorAlpha).endVertex();
					bufferbuilder.pos((double)(i + 1), (double)(8 + verticalShift), 0.0D).color(colorRed, colorGreen, colorBlue, colorAlpha).endVertex();
					bufferbuilder.pos((double)(i + 1), (double)(-1 + verticalShift), 0.0D).color(colorRed, colorGreen, colorBlue, colorAlpha).endVertex();
					tessellator.draw();
					GlStateManager.enableTexture2D();
					GlStateManager.depthMask(true);
					fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, colorString);
					GL11.glEnable(GL11.GL_DEPTH_TEST);
					GL11.glEnable(GL11.GL_LIGHTING);
					GlStateManager.enableLighting();
					GlStateManager.disableBlend();
					GlStateManager.popMatrix();
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
    }
	*/
	
	public static void drawNukerBlocks(Iterable<BlockPos> blocks, float r, float g, float b, float ticks) {
		glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(1);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL11.GL_LIGHTING);

        WorldClient world = Wrapper.INSTANCE.world();
        EntityPlayerSP player = Wrapper.INSTANCE.player();

        for(BlockPos pos : blocks) {

            IBlockState iblockstate = world.getBlockState(pos);

            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)ticks;
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)ticks;
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)ticks;

            GLUtils.glColor(new Color(r, g, b, 1.0f));
            
            AxisAlignedBB boundingBox = iblockstate.getSelectedBoundingBox(world, pos).grow(0.0020000000949949026D).offset(-x, -y, -z);
            drawSelectionBoundingBox(boundingBox);
        }

        glEnable(GL11.GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
	}
	
	public static void drawXRayBlocks(LinkedList<XRayBlock> blocks, float ticks) {
		glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(1);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL11.GL_LIGHTING);

        WorldClient world = Wrapper.INSTANCE.world();
        EntityPlayerSP player = Wrapper.INSTANCE.player();

        for(XRayBlock block : blocks) {
            BlockPos pos = block.getBlockPos();
            XRayData data = block.getxRayData();

            IBlockState iblockstate = world.getBlockState(pos);

            double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)ticks;
            double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)ticks;
            double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)ticks;

            int color = new Color(data.getRed(), data.getGreen(), data.getBlue(), 255).getRGB();
            GLUtils.glColor(color);

            AxisAlignedBB boundingBox = iblockstate.getSelectedBoundingBox(world, pos).grow(0.0020000000949949026D).offset(-x, -y, -z);
            drawSelectionBoundingBox(boundingBox);
        }

        glEnable(GL11.GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
	}
	
	public static void drawBlockESP(BlockPos pos, float red, float green, float blue) {
		glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glLineWidth(1);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL11.GL_LIGHTING);
        double renderPosX = Wrapper.INSTANCE.mc().getRenderManager().viewerPosX;
        double renderPosY = Wrapper.INSTANCE.mc().getRenderManager().viewerPosY;
        double renderPosZ = Wrapper.INSTANCE.mc().getRenderManager().viewerPosZ;

        glTranslated(-renderPosX, -renderPosY, -renderPosZ);
        glTranslated(pos.getX(), pos.getY(), pos.getZ());

        glColor4f(red, green, blue, 0.30F);
        drawSolidBox();
        glColor4f(red, green, blue, 0.7F);
        drawOutlinedBox();

        glColor4f(1, 1, 1, 1);

        glEnable(GL11.GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
	}

    public static void drawPath(Point p, float width, float red, float green, float blue, float alpha) {
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_CULL_FACE);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL11.GL_LIGHTING);
        double renderPosX = Wrapper.INSTANCE.mc().getRenderManager().viewerPosX;
        double renderPosY = Wrapper.INSTANCE.mc().getRenderManager().viewerPosY;
        double renderPosZ = Wrapper.INSTANCE.mc().getRenderManager().viewerPosZ;
        glTranslated(-renderPosX, -renderPosY, -renderPosZ);

        glLineWidth(width); // 设置线条宽度
        glColor4f(red, green, blue, alpha);
        glBegin(GL_LINES);
        {
            glVertex3d(p.parent.getX() + 0.5,p.parent.getY() + 0.5,p.parent.getZ() + 0.5);
            glVertex3d(p.getX() + 0.5,p.getY() + 0.5,p.getZ() + 0.5);
        }
        glEnd();

        glEnable(GL11.GL_LIGHTING);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
    }
	
	public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		tessellator.draw();
		vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		tessellator.draw();
		vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
		vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
		tessellator.draw();
	}
	
	public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
			Tessellator ts = Tessellator.getInstance();
			BufferBuilder vb = ts.getBuffer();
			vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts X.
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			ts.draw();
			vb.begin(7, DefaultVertexFormats.POSITION_TEX);
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			ts.draw();// Ends X.
			vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts Y.
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			ts.draw();
			vb.begin(7, DefaultVertexFormats.POSITION_TEX);
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			ts.draw();// Ends Y.
			vb.begin(7, DefaultVertexFormats.POSITION_TEX);// Starts Z.
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			ts.draw();
			vb.begin(7, DefaultVertexFormats.POSITION_TEX);
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ)
				.color(red, green, blue, alpha).endVertex();
			ts.draw();// Ends Z.
		}
	public static void drawSolidBox() {

        drawSolidBox(DEFAULT_AABB);
    }

    public static void drawSolidBox(AxisAlignedBB bb) {

        glBegin(GL_QUADS);
        {
            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.minY, bb.maxZ);

            glVertex3d(bb.minX, bb.maxY, bb.minZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);
        }
        glEnd();
    }

    public static void drawOutlinedBox() {

        drawOutlinedBox(DEFAULT_AABB);
    }

    public static void drawOutlinedBox(AxisAlignedBB bb) {

        glBegin(GL_LINES);
        {
            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.minY, bb.maxZ);

            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.minY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.minY, bb.minZ);

            glVertex3d(bb.minX, bb.minY, bb.minZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            glVertex3d(bb.maxX, bb.minY, bb.maxZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.minY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.minZ);

            glVertex3d(bb.maxX, bb.maxY, bb.minZ);
            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

            glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.maxZ);

            glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            glVertex3d(bb.minX, bb.maxY, bb.minZ);
        }
        glEnd();
    }
	
	public static void drawBoundingBox(AxisAlignedBB aa) {

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.maxZ);
        vertexBuffer.pos(aa.minX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.minX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.minZ);
        vertexBuffer.pos(aa.maxX, aa.maxY, aa.maxZ);
        vertexBuffer.pos(aa.maxX, aa.minY, aa.maxZ);
        tessellator.draw();
    }
	
	public static void drawOutlineBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }
	
	public static void drawTri(double x1, double y1, double x2, double y2, double x3, double y3, double width, Color c) {

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GLUtils.glColor(c);
        GL11.glLineWidth((float) width);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x3, y3);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawHLine(float par1, float par2, float par3, int color) {

        if (par2 < par1) {
            float var5 = par1;
            par1 = par2;
            par2 = var5;
        }

        drawRect(par1, par3, par2 + 1, par3 + 1, color);
    }

    public static void drawVLine(float par1, float par2, float par3, int color) {

        if (par3 < par2) {
            float var5 = par2;
            par2 = par3;
            par3 = var5;
        }

        drawRect(par1, par2 + 1, par1 + 1, par3, color);
    }

    public static void drawRect(float left, float top, float right, float bottom, int color) {

        float var5;

        if (left < right) {
            var5 = left;
            left = right;
            right = var5;
        }

        if (top < bottom) {
            var5 = top;
            top = bottom;
            bottom = var5;
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glPushMatrix();
        GLUtils.glColor(color);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void drawImage(URL resourceName, float left, float top, float right, float bottom){

        //glEnable(GL_TEXTURE_2D);
        glPushMatrix();
        glMatrixMode(GL_PROJECTION);
        //glOrtho(0, 800, 600, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        //GL11.glScalef(1.5f, 1.5f, 1.5f);
        try {
            getTexture(resourceName);
        } catch (Exception e) {
            ChatUtils.warning(e.getMessage());
            e.printStackTrace();
        }
        // clear screen
        //glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        // translate to the right location and prepare to draw
        //glTranslatef(300, 200, 0);
        // draw a quad textured to match the sprite
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);
            glVertex2f(left, bottom);

            glTexCoord2f(0, 1);
            glVertex2f(bottom, right);

            glTexCoord2f(1, 1);
            glVertex2f(right, top);

            glTexCoord2f(1, 0);
            glVertex2f(top, left);
        }
        //glDeleteTextures(texture);
        glEnd();
        glPopMatrix();
        //glDisable(GL_TEXTURE_2D);
    }

    static int textures = -1;

    public static void getTexture(URL resourceName) throws IOException {
        if (textures == -1) {
            textures = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textures);
        }else {
            glBindTexture(GL_TEXTURE_2D, textures);
            return;
        }

        BufferedImage bufferedImage = loadImage(resourceName);
        ByteBuffer textureBuffer = convertImageData(bufferedImage);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // produce a texture from the byte buffer
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, bufferedImage.getWidth(),
                bufferedImage.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE,
                textureBuffer);
    }

    /**
     * Convert the buffered image to a texture
     */
    private static ByteBuffer convertImageData(BufferedImage bufferedImage) {
        ByteBuffer imageBuffer;
        WritableRaster raster;
        BufferedImage texImage;

        ColorModel glAlphaColorModel = new ComponentColorModel(ColorSpace
                .getInstance(ColorSpace.CS_sRGB), new int[] { 8, 8, 8, 8 },
                true, false, Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);

        raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
                bufferedImage.getWidth(), bufferedImage.getHeight(), 4, null);
        texImage = new BufferedImage(glAlphaColorModel, raster, true,
                new Hashtable());

        // copy the source image into the produced image
        Graphics g = texImage.getGraphics();
        g.setColor(new Color(0f, 0f, 0f, 0f));
        g.fillRect(0, 0, 256, 256);
        g.drawImage(bufferedImage, 0, 0, null);

        // build a byte buffer from the temporary image
        // that be used by OpenGL to produce a texture.
        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer())
                .getData();

        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();

        return imageBuffer;
    }

    /**
     * Load a given resource as a buffered image
     */
    private static BufferedImage loadImage(URL url) throws IOException {
        // due to an issue with ImageIO and mixed signed code
        // we are now using good oldfashioned ImageIcon to load
        // images and the paint it on top of a new BufferedImage
        Image img = new ImageIcon(url).getImage();
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img
                .getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics g = bufferedImage.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return bufferedImage;
    }
}
