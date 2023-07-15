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
    public DoubleValue speed;
    public DoubleValue delay;
    public IntegerValue dropDistance;
    public BooleanValue parkour;
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
        straighten = new BooleanValue("平行折线拉直",false);
        allowPlace = new BooleanValue("允许建造",false);
        allowDestruction = new BooleanValue("允许破坏",false);
        longPath = new BooleanValue("长路线延续",true);
        this.addValue(maxBlockCount,delay,speed,dropDistance,parkour,straighten,allowPlace,allowDestruction,longPath);
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
        astar.parkour = parkour.getValue();
        astar.straighten = straighten.getValue();

        List<Point> list = astar.findPath(new BlockPos(Wrapper.INSTANCE.player().posX,
                Wrapper.INSTANCE.player().posY,
                Wrapper.INSTANCE.player().posZ), toPos);
        if(list != null) {
            pocPath = list;
        }else{
            ChatUtils.warning("返回NULL");
        }
    }

    public boolean inRange(int min, int max, double range){
        if(min > max){
            int d = min;
            min = max;
            max = d;
        }
        return range >= min && range <= max + 1;
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        BlockPos pb = new BlockPos(Wrapper.INSTANCE.player().posX,
                Wrapper.INSTANCE.player().posY,
                Wrapper.INSTANCE.player().posZ);
        Point tPath = null;
        if(pocPath != null){
            for (Point p: pocPath) {
                if(p.parent.equals(new Point(pb.getX(),pb.getY(),pb.getZ()))){
                    //去除开始节点
                    if(p.getGcost() != 0) { tPath = p; }
                }
                //寻路下方吸附
                if(p.parent.equals(new Point(pb.getX(),pb.getY() - 1,pb.getZ()))){
                    if(p.getGcost() != 0) { tPath = p; }
                }
                //跳跃下方吸附
                if(tPath == null && lOnGroundPoint != null){
                    int tHeight = pb.getY() - 2;
                    while (pb.getY() - tHeight <= dropDistance.getValue()){
                        if(!astar.passable(pb.getX(),tHeight,pb.getZ())){
                            break;
                        }
                        if(p.parent.equals(new Point(pb.getX(),tHeight,pb.getZ()))){
                            tPath = p;
                            break;
                        }
                        tHeight--;
                    }
                }

                //平行折线拉直 - 斜向走位补位
                //修复下坡遇到斜线卡住 !pocPath.contains(new Point(pb.getX(),pb.getY(),pb.getZ()))
                if(straighten.value && tPath == null && p.getType() == 4){
                    if((inRange(p.getX(),p.parent.getX(),Wrapper.INSTANCE.player().posX) &&
                            inRange(p.getZ(),p.parent.getZ(),Wrapper.INSTANCE.player().posZ)) &&
                            (p.getY() == pb.getY() || (p.getY() == pb.getY() - 1)    //修复高一格不走
                            )) {
                        tPath = p;
                    }
                }
            }
            if(tPath != null){
                //找到节点处理
                runPath(tPath);
                //ChatUtils.message(tPath);
                //ChatUtils.message("parent:" + tPath.parent);
            }else{
                //未找到节点处理
                if(inSpring){
                    Wrapper.INSTANCE.player().setSprinting(true);
                    Wrapper.INSTANCE.player().motionX = lSprintX;
                    Wrapper.INSTANCE.player().posY = lSprintY;
                    Wrapper.INSTANCE.player().motionZ = lSprintZ;
                }
            }
        }
    }

    //寻路微调
    private double posAdjustment(Double pos){
        if((int)(Math.abs(pos) * 10) % 10 > 5) {
            if(pos > 0){ return -0.1; }else{ return +0.1; }
        }
        if((int)(Math.abs(pos) * 10) % 10 < 5) {
            if(pos > 0){ return +0.1; }else{ return -0.1; }
        }
        return 0;
    }

    //记录上次起跳的跳跃节点
    private Point lOnGroundPoint = null;
    //延续跑
    private Boolean inSpring = false;
    private double lSprintX,lSprintY,lSprintZ = 0D;

    private void runPath(Point p){
        int xDc = p.getX() - p.parent.getX();
        int yDc = p.getY() - p.parent.getY();
        int zDc = p.getZ() - p.parent.getZ();

        inSpring = false;

        //跳跃约束
        if(lOnGroundPoint != null && !lOnGroundPoint.equals(p) && !Wrapper.INSTANCE.player().onGround){
            //在空中往方块中间约束
            Wrapper.INSTANCE.player().motionX = posAdjustment(Wrapper.INSTANCE.player().posX);
            Wrapper.INSTANCE.player().motionZ = posAdjustment(Wrapper.INSTANCE.player().posZ);
            return;
        }
        lOnGroundPoint = null;

        if(xDc == 0 && posAdjustment(Wrapper.INSTANCE.player().posX) != 0){
            Wrapper.INSTANCE.player().motionX = posAdjustment(Wrapper.INSTANCE.player().posX);
            return;
        }
        if(zDc == 0 && posAdjustment(Wrapper.INSTANCE.player().posZ) != 0){
            Wrapper.INSTANCE.player().motionZ = posAdjustment(Wrapper.INSTANCE.player().posZ);
            return;
        }
        //平行折线拉直 - 斜向走位
        if(straighten.value && p.getType() == 4){
            //Wrapper.INSTANCE.player().motionY = 0;
            xDc = Math.min(xDc, 1);
            zDc = Math.min(zDc, 1);
        }

        //跳跃处理
        if ((p.getType() == 2 || yDc > 0)) {
            //上台阶
            if(Wrapper.INSTANCE.player().onGround){
                Wrapper.INSTANCE.player().jump();
            }
            if(p.getType() == 2) {
                lOnGroundPoint = p;
            }
            //重新赋予跳跃速度
            Wrapper.INSTANCE.player().motionX = xDc * 0.15;
            Wrapper.INSTANCE.player().motionZ = zDc * 0.15;
            return;
        }
        //修复下楼梯卡住
        if(yDc < 0){
            //(p.parent.parent != null && p.parent.parent.getY() - p.parent.getY() > 0)
            lOnGroundPoint = p;
        }
        //赋予移动速度
        Wrapper.INSTANCE.player().motionX = xDc * (speed.getValue() * 0.1);
        Wrapper.INSTANCE.player().motionZ = zDc * (speed.getValue() * 0.1);
        if(Math.abs(Wrapper.INSTANCE.player().motionX) >= 0.15 || Math.abs(Wrapper.INSTANCE.player().motionZ) >= 0.15){
            Wrapper.INSTANCE.player().setSprinting(true);
        }

        //奔跑处理 - 必须在赋值速度后保存初始移动向量
        if(p.getType() == 3){
            //平行一格距离直接跑过去
            inSpring = true;
            //预防连跳导致卡方块
            Wrapper.INSTANCE.player().motionY = Wrapper.INSTANCE.player().motionY > 0 ? -1 : Wrapper.INSTANCE.player().motionY;
            lSprintX = Wrapper.INSTANCE.player().motionX;
            lSprintY = Math.round(Wrapper.INSTANCE.player().posY);
            lSprintZ = Wrapper.INSTANCE.player().motionZ;
        }
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
                }else{
                    RenderUtils.drawPath(p,3,0,0,0,0.4F);
                }
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
                if(p.getType() == 2){
                    RenderUtils.drawJumpPath(p,5,1,0,1,0.5F);
                }else {
                    RenderUtils.drawPath(p,5,1,1,1,0.5F);
                }

                if(p.equals(new Point(myP.getX(), myP.getY(), myP.getZ()))) {
                    debugVal = String.format("Gcost:%d  Fcost:%f",p.getGcost(),p.getFcost());
                }
            }
        }
    }
}
