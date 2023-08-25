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

import static i.WinKcode.auto.runPoint.onRunPointClientTick;

public class AiCore extends Hack {
    public IntegerValue maxBlockCount;
    public DoubleValue speed;
    public DoubleValue delay;
    public IntegerValue dropDistance;
    public BooleanValue parkour;
    public BooleanValue parkourAdvanced;
    public BooleanValue straighten;
    public BooleanValue allowPlace;
    public BooleanValue allowDestruction;
    public BooleanValue longPath;

    public AiCore() {
        super("AiCore", HackCategory.AUTO);
        this.GUIName = "AI核心";

        maxBlockCount = new IntegerValue("区块深度",2,1,10);
        delay = new DoubleValue("路线扩展", 5.0, 2.0, 20.0);
        speed = new DoubleValue("行走速度",2.0,1.0,10.0);
        dropDistance = new IntegerValue("跌落距离", 5, 2, 50);
        parkour = new BooleanValue("跑酷模式",false);
        parkourAdvanced = new BooleanValue("跑酷进阶",false);
        straighten = new BooleanValue("平行折线拉直",false);
        allowPlace = new BooleanValue("允许建造",false);
        allowDestruction = new BooleanValue("允许破坏",false);
        longPath = new BooleanValue("长路线延续",true);
        this.addValue(maxBlockCount,delay,speed,dropDistance,parkour,parkourAdvanced,straighten,allowPlace,allowDestruction,longPath);
    }

    public List<Point> pocPath = null;
    public AStar astar = new AStar();
    public boolean isThreadRun = false;

    @Override
    public String getDescription() {
        return "所有AI功能的前置核心.";
    }

    public void findPath(BlockPos toPos){
        if(!isThreadRun){
            isThreadRun = true;
            new Thread(new AiHandle(this,toPos)).start();
        }else{
            ChatUtils.warning("上次的任务还未完成！");
        }
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        onRunPointClientTick(pocPath, astar, dropDistance.getValue(), speed.getValue());
    }

    private String debugVal = "";
    @Override
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
        ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        int colorRect = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F);
        int colorRect2 = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F);

        String motionStr = "X:" + Wrapper.INSTANCE.player().motionX + " Y:" + Wrapper.INSTANCE.player().motionY + " Z:" + Wrapper.INSTANCE.player().motionZ;

        RenderUtils.drawStringWithRect(debugVal,
                sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(debugVal) - 5,
                100,
                ColorUtils.color(1, 1, 1, 0.0F), colorRect, colorRect2);
        RenderUtils.drawStringWithRect(motionStr,
                sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(motionStr) - 5,
                150,
                ColorUtils.color(1, 1, 1, 0.0F), colorRect, colorRect2);
    }

    @Override
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        BlockPos myP = Wrapper.INSTANCE.player().getPosition();
        if(astar.closeList != null){  //闭表显示
            for (Point p: astar.closeList) {
                if(p.getType() == 2){
                    RenderUtils.drawJumpPath(p,3,0,0,0,0.4F);
                }else if(p.getType() == 5){
                    RenderUtils.drawPath(p,5,0,0,1,0.2F);
                }else{
                    RenderUtils.drawPath(p,3,0,0,0,0.4F);
                }
                if(p.equals(new Point(myP.getX(), myP.getY(), myP.getZ()))) {
                    debugVal = String.format("Gcost:%f  Fcost:%f",p.getGcost(),p.getFcost());
                }
            }
        }

        if(astar.openList != null){  //开表显示
            for (Point p: astar.openList) {
                RenderUtils.drawPath(p,3,1,0,0,0.7F);

                if(p.equals(new Point(myP.getX(), myP.getY(), myP.getZ()))) {
                    debugVal = String.format("Gcost:%f  Fcost:%f",p.getGcost(),p.getFcost());
                }
            }
        }

        if(pocPath != null){
            for (Point p: pocPath) {
                if(p.getType() == 2){
                    RenderUtils.drawJumpPath(p,5,1,0,1,0.5F);
                }else if(p.getType() == 5){
                    RenderUtils.drawPath(p,5,0,0,1,0.5F);
                }else {
                    RenderUtils.drawPath(p,5,1,1,1,0.5F);
                }

                if(p.equals(new Point(myP.getX(), myP.getY(), myP.getZ()))) {
                    debugVal = String.format("Gcost:%f  Fcost:%f",p.getGcost(),p.getFcost());
                }
            }
        }
    }
}

class AiHandle implements Runnable{
    AiCore Ai;
    BlockPos endPos;
    public AiHandle (AiCore ai,BlockPos toPos){ Ai = ai;endPos = toPos; }
    public void run() {
        Ai.astar = new AStar();
        Ai.astar.compute = Ai.maxBlockCount.getValue() * 1024;
        Ai.astar.Depth = Ai.delay.getValue();
        Ai.astar.dropDistance = Ai.dropDistance.getValue();
        Ai.astar.parkour = Ai.parkour.getValue();
        Ai.astar.parkourAdvanced = Ai.parkourAdvanced.getValue();
        Ai.astar.straighten = Ai.straighten.getValue();
        Ai.astar.allowPlace = Ai.allowPlace.getValue();
        Ai.astar.allowDestruction = Ai.allowDestruction.getValue();

        List<Point> list = Ai.astar.findPath(new BlockPos(Wrapper.INSTANCE.player().posX,
                Wrapper.INSTANCE.player().posY,
                Wrapper.INSTANCE.player().posZ), endPos);
        if(list != null) {
            Ai.pocPath = list;
        }else{
            ChatUtils.warning("返回NULL");
        }

        Ai.isThreadRun = false;
    }

}
