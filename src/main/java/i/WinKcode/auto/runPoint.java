package i.WinKcode.auto;

import i.WinKcode.wrappers.Wrapper;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class runPoint {

    //记录上次起跳的跳跃节点
    private static Point lOnGroundPoint = null;
    //延续跑
    private static Boolean inSpring = false;
    private static double lSprintX,lSprintY,lSprintZ = 0D;

    public static void onRunPointClientTick(List<Point> pocPath, AStar astar, int dropDistance, double speed){
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
                    while (pb.getY() - tHeight <= dropDistance){
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
                if(tPath == null && p.getType() == 4){
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
                runPath(tPath, speed);
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

    public static boolean inRange(int min, int max, double range){
        if(min > max){
            int d = min;
            min = max;
            max = d;
        }
        return range >= min && range <= max + 1;
    }

    //寻路微调
    private static double posAdjustment(Double pos){
        if((int)(Math.abs(pos) * 10) % 10 > 5) {
            if(pos > 0){ return -0.1; }else{ return +0.1; }
        }
        if((int)(Math.abs(pos) * 10) % 10 < 5) {
            if(pos > 0){ return +0.1; }else{ return -0.1; }
        }
        return 0;
    }

    private static void runPath(Point p, double speed) {
        int xDc = p.getX() - p.parent.getX();
        int yDc = p.getY() - p.parent.getY();
        int zDc = p.getZ() - p.parent.getZ();

        inSpring = false;

        //跳跃约束
        if (lOnGroundPoint != null && !lOnGroundPoint.equals(p) && !Wrapper.INSTANCE.player().onGround) {
            //在空中往方块中间约束
            Wrapper.INSTANCE.player().motionX = posAdjustment(Wrapper.INSTANCE.player().posX);
            Wrapper.INSTANCE.player().motionZ = posAdjustment(Wrapper.INSTANCE.player().posZ);
            return;
        }
        lOnGroundPoint = null;

        if (xDc == 0 && posAdjustment(Wrapper.INSTANCE.player().posX) != 0) {
            Wrapper.INSTANCE.player().motionX = posAdjustment(Wrapper.INSTANCE.player().posX);
            return;
        }
        if (zDc == 0 && posAdjustment(Wrapper.INSTANCE.player().posZ) != 0) {
            Wrapper.INSTANCE.player().motionZ = posAdjustment(Wrapper.INSTANCE.player().posZ);
            return;
        }
        //平行折线拉直 - 斜向走位
        if (p.getType() == 4) {
            //Wrapper.INSTANCE.player().motionY = 0;
            xDc = Math.min(xDc, 1);
            zDc = Math.min(zDc, 1);
        }

        //跳跃处理
        if ((p.getType() == 2 || yDc > 0)) {
            //上台阶
            if (Wrapper.INSTANCE.player().onGround) {
                Wrapper.INSTANCE.player().jump();
            }
            if (p.getType() == 2) {
                lOnGroundPoint = p;
            }
            //重新赋予跳跃速度
            Wrapper.INSTANCE.player().motionX = xDc * 0.15;
            Wrapper.INSTANCE.player().motionZ = zDc * 0.15;
            return;
        }
        //修复下楼梯卡住
        if (yDc < 0) {
            //(p.parent.parent != null && p.parent.parent.getY() - p.parent.getY() > 0)
            lOnGroundPoint = p;
        }
        //赋予移动速度
        Wrapper.INSTANCE.player().motionX = xDc * (speed * 0.1);
        Wrapper.INSTANCE.player().motionZ = zDc * (speed * 0.1);
        if (Math.abs(Wrapper.INSTANCE.player().motionX) >= 0.15 || Math.abs(Wrapper.INSTANCE.player().motionZ) >= 0.15) {
            Wrapper.INSTANCE.player().setSprinting(true);
        }

        //奔跑处理 - 必须在赋值速度后保存初始移动向量
        if (p.getType() == 3) {
            //平行一格距离直接跑过去
            inSpring = true;
            //预防连跳导致卡方块
            Wrapper.INSTANCE.player().motionY = Wrapper.INSTANCE.player().motionY > 0 ? -1 : Wrapper.INSTANCE.player().motionY;
            lSprintX = Wrapper.INSTANCE.player().motionX;
            lSprintY = Math.round(Wrapper.INSTANCE.player().posY);
            lSprintZ = Wrapper.INSTANCE.player().motionZ;
        }
    }
}
