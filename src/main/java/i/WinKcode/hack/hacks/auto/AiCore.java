package i.WinKcode.hack.hacks.auto;

import i.WinKcode.auto.AStar;
import i.WinKcode.auto.Point;
import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.utils.visual.ColorUtils;
import i.WinKcode.utils.visual.RenderUtils;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.value.types.DoubleValue;
import i.WinKcode.value.types.IntegerValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

public class AiCore extends Hack {
    public IntegerValue maxBlockCount;
    public DoubleValue delay;
    public IntegerValue dropDistance;
    public BooleanValue parkour;
    public BooleanValue operate;

    public AiCore() {
        super("AiCore", HackCategory.AUTO);
        this.GUIName = "AI核心";

        maxBlockCount = new IntegerValue("区块深度",2,1,10);
        delay = new DoubleValue("路线扩展", 5.0, 2.0, 20.0);
        dropDistance = new IntegerValue("跌落距离", 5, 2, 50);
        parkour = new BooleanValue("跑酷模式",false);
        operate = new BooleanValue("建造+破坏",true);
        this.addValue(maxBlockCount,delay,dropDistance,parkour);
    }

    private List<Point> pocPath = null;
    private AStar astar = new AStar();

    @Override
    public String getDescription() {
        return "所有AI功能的前置核心.";
    }

    public void findPath(BlockPos toPos){
        astar = new AStar();
        astar.compute = maxBlockCount.getValue() * 1024;
        astar.Depth = delay.getValue();
        astar.dropDistance = dropDistance.getValue();
        List<Point> list = astar.findPath(Wrapper.INSTANCE.player().getPosition(), toPos);
        if(list != null) {
            pocPath = list;
        }else{
            ChatUtils.warning("返回NULL");
        }
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        /*if(pocPath == null){
            return;
        }
        for (Point p: pocPath) {
            Wrapper.INSTANCE.player().move(MoverType.SELF,p.getX(),p.getY(),p.getZ());
            Wrapper.INSTANCE.player().moveToBlockPosAndAngles(new BlockPos(p.getX(),p.getY(),p.getZ()),0,0);
            ChatUtils.message(p);
        }
        pocPath = null;*/
    }

    private String debugVal = "";
    @Override
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        int colorRect = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F);
        int colorRect2 = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F);

        RenderUtils.drawStringWithRect(debugVal,
                sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(debugVal) - 5,
                100,
                ColorUtils.color(1, 1, 1, 0.0F), colorRect, colorRect2);
    }

    @Override
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        BlockPos myP = Wrapper.INSTANCE.player().getPosition();
        if(astar.closeList != null){  //闭表显示
            for (Point p: astar.closeList) {
                RenderUtils.drawPath(p,3,0,0,0,0.4F);

                if(p.equals(new Point(myP.getX(), myP.getY(), myP.getZ()))) {
                    debugVal = String.format("Gcost:%d  Fcost:%f",p.getGcost(),p.getFcost());
                }
            }
        }

        if(astar.openList != null){  //开表显示
            for (Point p: astar.openList) {
                RenderUtils.drawPath(p,3,1,0,0,0.7F);

                if(p.equals(new Point(myP.getX(), myP.getY(), myP.getZ()))) {
                    debugVal = String.format("Gcost:%d  Fcost:%f",p.getGcost(),p.getFcost());
                }
            }
        }

        if(pocPath != null){
            for (Point p: pocPath) {
                RenderUtils.drawPath(p,5,1,1,1,0.5F);

                if(p.equals(new Point(myP.getX(), myP.getY(), myP.getZ()))) {
                    debugVal = String.format("Gcost:%d  Fcost:%f",p.getGcost(),p.getFcost());
                }
            }
        }
    }
}
