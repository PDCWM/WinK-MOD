package i.WinKcode.auto;

import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AStar {
    //缓存
    WorldClient world;
    //可变BlockPos
    BlockPos.MutableBlockPos mutableBlockPos;

    //开表与闭表
    public Queue<Point> openList = new ConcurrentLinkedQueue<>();
    public List<Point> closeList = new ArrayList<>();
    //欧拉限制边界线
    Queue<Point> boundaryList = new ConcurrentLinkedQueue<>();

    //限制搜索方块数量
    public int compute = 2048;
    //路线扩展深度
    public double Depth = 5.0;
    //跌落距离
    public int dropDistance = 5;
    //跑酷
    public Boolean parkour = false;
    //跑酷进阶
    public Boolean parkourAdvanced = false;
    //折线拉直
    public Boolean straighten = false;
    //允许建造
    public Boolean allowPlace = false;
    //允许破坏
    public Boolean allowDestruction = false;


    //欧拉限制
    double minFCost = 0;
    //边界线值级
    //double boundaryMin,boundaryMax = 0;
    //可否vClip
    boolean vClip = false;
    //特殊节点
    boolean specialNode = false;
    //穿墙攻击
    boolean throughWalls = false;
    //瞬移距离
    float BlinkDistance = 5F;


    int startX,startY,startZ = 0;
    int endX,endY,endZ = 0;

    public List<Point> findPath(BlockPos start, BlockPos end){
        world = Wrapper.INSTANCE.world();
        mutableBlockPos = new BlockPos.MutableBlockPos();
        openList = new ConcurrentLinkedQueue<>();
        closeList = new ArrayList<>();

        //检查起点与终点是否不可通过，并向上寻找到一个可通过的位置
        if (!check(end.getX(), end.getY(), end.getZ())) {
            for (int i = 1; i < 100; ++i) {
                if (check(end.getX(), end.getY() + i, end.getZ())) {
                    end.up();
                    break;
                }
            }
        } if (!throughWalls && !check(start.getX(), start.getY(), start.getZ())) {
            for (int i = 1; i < 4; ++i) {
                if (check(start.getX(), start.getY() + i, start.getZ())) {
                    start.up();
                    break;
                }
            }
        }
        //起点或终点不可通过
        if (!throughWalls && !check(start.getX(), start.getY(), start.getZ()) ||
                !check(end.getX(), end.getY(), end.getZ())) {
            ChatUtils.warning("起点或终点不可通过");
            return null;
        }
        ChatUtils.warning("开始");
        //起点XYZ坐标
        startX = start.getX();
        startY = start.getY();
        startZ = start.getZ();
        //终点XYZ
        endX = end.getX();
        endY = end.getY();
        endZ = end.getZ();

        //把起点添加到开表
        openList.add(new Point(startX, startY, startZ, new Point(startX, startY, startZ, null, 0, 0), 0, 0));

        List<Point> retList = null;
        retList = aStar();
        while (closeList.size() < compute && boundaryList.size() > 0 && retList == null){
            //路线-欧拉边界路线扩展
            double lastMinFCost = minFCost;
            minFCost = 0;
            //是否带入自建路线
            int lastPlaceCount = 0;
            while (boundaryList.size() > 0){
                Point p = boundaryList.poll();
                openList.add(p);
            }

            //路线-自建路线扩展
            if(allowPlace || allowDestruction){
                for (int i = 0;i < closeList.size();i++){
                    Point p = closeList.get(i);
                    if(p.getFcost() < lastMinFCost + Depth){
                        //自建搭建路线
                        if(allowPlace){
                            //closeList.add(p);
                            if(lastPlaceCount++ < Depth){
                                toPlace(p,true);
                            }else{
                                toPlace(p);
                            }
                        }
                        //自建挖掘路线
                        /*if(allowDestruction){
                            if(lastPlaceCount++ < Depth){
                                toPlace(p,true);
                            }else{
                                toPlace(p);
                            }
                        }*/
                    }
                }
            }

            retList = aStar();
            if(retList != null){
                return retList;
            }
        }
        return retList;
    }


    /*
     * =============================
     *  寻路算法使用A*  权重偏预估距离
     * =============================
     *///循环寻路
    public List<Point> aStar() {
        while (openList.size() != 0 && closeList.size() < compute) {
            //出队一个节点
            Point node = openList.poll();

            //缓存
            int x = node.getX();
            int y = node.getY();
            int z = node.getZ();

            //重置
            specialNode = false;

            //在闭表
            if (closeList.contains(node)) {
                continue;
            }

            //寻找到终点
            if (x == endX && y == endY && z == endZ) {
                //根据节点的父节点返回得到路径
                List<Point> path = new ArrayList<>();

                do {
                    //平行折线拉直
                    if(straighten && node.parent != null && node.parent.parent != null) {
                        if(node.getX() - node.parent.parent.getX() != 0 && node.getZ() - node.parent.parent.getZ() != 0 &&
                                node.getY() == node.parent.parent.getY() && node.getType() == 1 &&
                                node.parent.getType() == 1 && node.parent.parent.getType() == 1){
                            node.setType(4);
                            node.parent = node.parent.parent;
                        }
                    }
                    path.add(node);
                    node = node.parent;
                } while (node != null);
                path.remove(path.size() - 1);
                //返回路径
                return path;
            }

            if(node.getType() == 5 && node.getX() == endX && node.getZ() == endZ && node.getY() > endY + dropDistance){
                boundaryList.clear();
                ChatUtils.warning("已找到终点但不可到达");
                return null;
            }

            //不可通过
            if (!checkNode(node)) {
                //尝试vClip
                if (vClip) {
                    //如果是根节点
                    Point parent = node.parent;
                    if (parent == null) {
                        continue;
                    }
                    //位置
                    int parentX = parent.getX();
                    int parentY = parent.getY();
                    int parentZ = parent.getZ();
                    //如果有阻挡才尝试
                    if (!check(parentX, parentY + 1, parentZ)) {
                        upVClip(parentX, parentY, parentZ, parent);
                    }
                    if (!check(parentX, parentY - 1, parentZ)) {
                        downVClip(parentX, parentY, parentZ, parent);
                    }
                }
                continue;
            }

            //添加到闭表
            closeList.add(node);

            //把当前节点的周围子节点添加到开表
            //if (!specialNode) {
            //    addToOpenList(x + 1, y, z, node);
            //    addToOpenList(x, y + 1, z, node);
            //    addToOpenList(x, y, z + 1, node);
            //    addToOpenList(x - 1, y, z, node);
            //    addToOpenList(x, y - 1, z, node);
            //    addToOpenList(x, y, z - 1, node);
            //} else {
                if (reachable(x + 1, y, z, x, y, z)) {
                    addToOpenList(x + 1, y, z, node);
                }
                if (reachable(x, y + 1, z, x, y, z)) {
                    addToOpenList(x, y + 1, z, node);
                }
                if (reachable(x, y, z + 1, x, y, z)) {
                    addToOpenList(x, y, z + 1, node);
                }
                if (reachable(x - 1, y, z, x, y, z)) {
                    addToOpenList(x - 1, y, z, node);
                }
                if (reachable(x, y - 1, z, x, y, z)) {
                    addToOpenList(x, y - 1, z, node);
                }
                if (reachable(x, y, z - 1, x, y, z)) {
                    addToOpenList(x, y, z - 1, node);
                }
            //}
        }
        return null;
    }

    //添加节点到开表
    public void addToOpenList(int x,int y,int z,Point parent) {
        addToOpenList(x,y,z,parent,1);
    }

    //添加节点到开表
    public void addToOpenList(int x,int y,int z,Point parent,int type) {
        //当前代价 【据地形影响移动速度判定值-未定
        float gCost = getToGCost(parent.getGcost(),type);
        //总代价，这里使用曼哈顿距离和欧拉距离
        double fCost = getToFCost(x,y,z,parent.getGcost(),type);
        //欧拉贪婪限制
        if(minFCost > fCost || minFCost == 0) {
            minFCost = fCost;
        }
        if(fCost < minFCost + Depth) {
            //添加到开表
            openList.add(new Point(x, y, z, parent, gCost, fCost, type));
        }else{
            //添加到边界线
            boundaryList.add(new Point(x, y, z, parent, gCost, fCost, type));
        }
    }

    public float getToGCost(float parentGCost,int type){
        //###曼哈顿距离重塑路线
        float gCost = parentGCost + 1;
        if(type == 5){
            gCost += 1;
        }
        return gCost;
    }

    public double getToFCost(int x,int y,int z,float parentGCost,int type){
        //XYZ距离
        int xDist = Math.abs(x - endX);
        int yDist = Math.abs(y - endY);
        int zDist = Math.abs(z - endZ);
        //当前代价 【据地形影响移动速度判定值-未定
        float gCost = getToGCost(parentGCost,type);
        //总代价，这里使用曼哈顿距离和欧拉距离
        return gCost + xDist + yDist + zDist + Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
    }

    private boolean check(int x, int y, int z) {
        return passable(x, y, z) && passable(x, y + 1, z) && groundPassable(x, y - 1, z) || special(x, y, z) && reachable(x, y, z, x, y, z);
    }

    private boolean checkNode(Point node) {
        //缓存
        int x = node.getX();
        int y = node.getY();
        int z = node.getZ();
        //检查是否通过
        return passable(x, y, z) && passable(x, y + 1, z) && groundPassableEx(x, y - 1, z, node);
        //如果是特殊方块
        /*if (special(x, y, z)) {
            //标记为特殊方块
            specialNode = true;
            //得到父节点
            Point parent = node.parent;
            //缓存
            int parentX = parent.getX();
            int parentY = parent.getY();
            int parentZ = parent.getZ();
            //检查是否通过
            return reachable(x, y, z, parentX, parentY, parentZ);
        }*/
        //不可通过
    }

    //判断是否为可破坏方块
    public boolean isDestructionBlock(int x, int y, int z){
        if(passable(x,y,z)){
           return false;
        }
        IBlockState blockState = world.getBlockState(mutableBlockPos.setPos(x, y, z));
        return Block.getStateId(blockState) != 7;
    }

    //检查位置是否可通过
    public boolean passable(int x, int y, int z) {
        IBlockState blockState = world.getBlockState(mutableBlockPos.setPos(x, y, z));
        Block block = blockState.getBlock();
        return (block.getCollisionBoundingBox(blockState, world, mutableBlockPos) == null || special(x,y,z)) &&
                !((block instanceof BlockLiquid || block instanceof BlockStaticLiquid) && block.getMaterial(blockState) != Material.WATER);
    }

    private boolean addJumpPath(int x, int y, int z,Point parent){
        if(!passable(parent.getX(),parent.getY() + 2,parent.getZ())){
            return false;
        }
        if(!passable(x,y - 1,z)){
            if(parkourAdvanced && (Math.abs(parent.getX() - x) == 2 || Math.abs(parent.getZ() - z) == 2) && parent.getY() == y) {
                addToOpenList(x,y,z,parent,3);
            }else{
                addToOpenList(x,y,z,parent,2);
            }
            return false;
        }else{
            return true;
        }
    }

    //跑酷处理
    private void parKFun(int x, int y, int z, Point parent){
        int xDc = x - parent.getX();
        int zDc = z - parent.getZ();
        if(xDc == 1){
            if(addJumpPath(x + 1,y,z,parent)){
                addJumpPath(x + 2,y,z,parent);
            }
        }
        if(xDc == -1){
            if(addJumpPath(x - 1,y,z,parent)) {
                addJumpPath(x - 2, y, z, parent);
            }
        }
        if(zDc == 1){
            if(addJumpPath(x,y,z + 1,parent)) {
                addJumpPath(x, y, z + 2, parent);
            }
        }
        if(zDc == -1){
            if(addJumpPath(x,y,z - 1,parent)) {
                addJumpPath(x, y, z - 2, parent);
            }
        }
    }

    //检查位置是否为可通过的地面
    private boolean groundPassable(int x,int y,int z) {
        IBlockState blockState = world.getBlockState(mutableBlockPos.setPos(x, y, z));
        Block block = blockState.getBlock();
        return !(block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate || block instanceof BlockMagma ||
                ((block instanceof BlockLiquid || block instanceof BlockStaticLiquid) && block.getMaterial(blockState) != Material.WATER));
    }

    //检查位置是否为可通过的地面
    private boolean groundPassableEx(int x,int y,int z,Point node) {
        if(!groundPassable(x,y,z)) {
            //搭建绕路路线处理 - 防止极致贪婪模式钻牛角尖
            if(node.parent.getType() == 5){
                //返回上一格并临时取消极致贪婪
                toPlace(node.parent, true);
            }
            return false;
        }
        if(world.getBlockState(mutableBlockPos.setPos(x,y,z)).getMaterial() == Material.WATER) {
            node.setGcost(node.getGcost() + 0.5f);
            return true;
        }

        //搭建路线处理
        if(node.getType() == 5){
            closeList.add(node);
            return toPlace(node);
        }

        Point parent = node.parent;
        // 贴地优化
        int upDown =  y - parent.getY();
        if(passable(x, y, z)){
            //离地判断
            if(upDown == 0){        //上坡处理
                if (!passable(x + 1, y, z)) {
                    addToOpenList(x + 1, y + 1, z, parent);
                }
                if (!passable(x - 1, y, z)) {
                    addToOpenList(x - 1, y + 1, z, parent);
                }
                if (!passable(x, y, z + 1)) {
                    addToOpenList(x, y + 1, z + 1, parent);
                }
                if (!passable(x, y, z - 1)) {
                    addToOpenList(x, y + 1,  z - 1, parent);
                }
                return false;
            }

            //下坡处理
            if (!passable(parent.getX(), parent.getY() - 1, parent.getZ()) && -upDown <= dropDistance) {
                if(parkour) {
                    parKFun(x, y + 1, z, parent);
                }
                addToOpenList(x, y, z, parent);
            }
            return false;
        }

        //特殊方块处理
        if(special(x, y, z)){
            if (!passable(x, y - 1, z)) {
                addToOpenList(x, y, z, parent);
            }
            return false;
        }
        return true;
    }

    private double toPlaceRepeat(boolean isAll, int x, int y, int z, double fCost, Point p,int type){
        if(isAll){
            addToOpenList(x, y, z, p,type);
            return 0;
        }else{
            double thisFCost = getToFCost(x, y, z, p.getGcost(),type);
            if(fCost == 0 || thisFCost < fCost){
                return thisFCost;
            }
        }
        return 0;
    }

    //搭建路线
    private boolean toPlace(Point p){ return toPlace(p,false); }
    private boolean toPlace(Point p,boolean isAll){
        int x = p.getX();
        int y = p.getY();
        int z = p.getZ();

        if(p.parent.getType() == 5 && p.getY() - p.parent.getY() == 0){
            if(groundPassable(x, y - 1, z)){
                p.setType(1);
                return true;
            }
            for (int i=2;i <= dropDistance; i++){
                if(groundPassable(x, y - i, z)){
                    addToOpenList(x, y - i + 1, z, p.parent);
                    return true;
                }
            }
        }
        double fCost = 0;int tx = 0;int ty = 0;int tz = 0;
        //优化-只择一近点
        if (passable(x, y + 1, z) && passable(x, y + 2, z)) {
            double thisFCost = toPlaceRepeat(isAll, x, y + 1, z, fCost ,p,5);
            if(thisFCost != 0) {
                fCost = thisFCost;tx = x;ty = y + 1;tz = z;
            }
        }
        if (passable(x + 1, y, z) && passable(x + 1, y + 1, z)) {
            double thisFCost = toPlaceRepeat(isAll, x + 1, y, z, fCost ,p,5);
            if(thisFCost != 0) {
                fCost = thisFCost;tx = x + 1;ty = y;tz = z;
            }
        }
        if (passable(x, y, z + 1) && passable(x, y + 1, z + 1)) {
            double thisFCost = toPlaceRepeat(isAll, x, y, z + 1, fCost ,p,5);
            if(thisFCost != 0) {
                fCost = thisFCost;tx = x;ty = y;tz = z + 1;
            }
        }
        if (passable(x - 1, y, z) && passable(x - 1, y + 1, z)) {
            double thisFCost = toPlaceRepeat(isAll, x - 1, y, z, fCost ,p,5);
            if(thisFCost != 0) {
                fCost = thisFCost;tx = x - 1;ty = y;tz = z;
            }
        }
        if (passable(x, y, z - 1) && passable(x, y + 1, z - 1)) {
            double thisFCost = toPlaceRepeat(isAll, x, y, z - 1, fCost ,p,5);
            if(thisFCost != 0) {
                fCost = thisFCost;tx = x;ty = y;tz = z - 1;
            }
        }
        if(fCost != 0){
            addToOpenList(tx,ty,tz,p,5);
        }
        return false;
    }

    //破坏路线
    private boolean toDestruction(Point p){ return toPlace(p,false); }
    private boolean toDestruction(Point p,boolean isAll){
        int x = p.getX();
        int y = p.getY();
        int z = p.getZ();

        double fCost = 0;int tx = 0;int ty = 0;int tz = 0;
        //优化-只择一近点
        if (passable(x, y + 1, z) && passable(x, y + 2, z)) {
            double thisFCost = toPlaceRepeat(isAll, x, y + 1, z, fCost ,p,6);
            if(thisFCost != 0) {
                fCost = thisFCost;tx = x;ty = y + 1;tz = z;
            }
        }
        if (passable(x + 1, y, z) && passable(x + 1, y + 1, z)) {
            double thisFCost = toPlaceRepeat(isAll, x + 1, y, z, fCost ,p,6);
            if(thisFCost != 0) {
                fCost = thisFCost;tx = x + 1;ty = y;tz = z;
            }
        }
        if (passable(x, y, z + 1) && passable(x, y + 1, z + 1)) {
            double thisFCost = toPlaceRepeat(isAll, x, y, z + 1, fCost ,p,6);
            if(thisFCost != 0) {
                fCost = thisFCost;tx = x;ty = y;tz = z + 1;
            }
        }
        if (passable(x - 1, y, z) && passable(x - 1, y + 1, z)) {
            double thisFCost = toPlaceRepeat(isAll, x - 1, y, z, fCost ,p,6);
            if(thisFCost != 0) {
                fCost = thisFCost;tx = x - 1;ty = y;tz = z;
            }
        }
        if (passable(x, y, z - 1) && passable(x, y + 1, z - 1)) {
            double thisFCost = toPlaceRepeat(isAll, x, y, z - 1, fCost ,p,6);
            if(thisFCost != 0) {
                fCost = thisFCost;tx = x;ty = y;tz = z - 1;
            }
        }
        if(fCost != 0){
            addToOpenList(tx,ty,tz,p,6);
        }
        return false;
    }



    //检查位置是否为特殊方块
    private boolean special(int x,int y,int z) {
        BlockPos blockPos = mutableBlockPos.setPos(x, y, z);
        Block block = world.getBlockState(blockPos).getBlock();
        return block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow && block.isReplaceable(world, blockPos) || block == Block.getBlockById(111) || block instanceof BlockTrapDoor || block instanceof BlockDoor;
    }


    //碰撞箱列表
    List<AxisAlignedBB> boxList = new ArrayList<>();;

    //是否可以移动过去
    private boolean reachable(int x,int y,int z,int x2,int y2,int z2) {
        //检查Y轴碰撞
        AxisAlignedBB yBB = new AxisAlignedBB(x + 0.5 - 0.3, (y2 < y ? y2 : y) + 0.0626, z + 0.5 - 0.3, x + 0.5 + 0.3, (y2 > y ? y2 : y) + 1.8 + 0.0626, z + 0.5 + 0.3);
        for (;;) {
            if (intersects(yBB, x, y, z)) {
                return false;
            } if (y == y2) {
                if (intersects(yBB, x, y + (y > y2 ? -1 : 1), z)) {
                    return false;
                }
                break;
            } if (y > y2) {
                --y;
            } else {
                ++y;
            }
        }

        //检查X轴碰撞
        AxisAlignedBB xBB = new AxisAlignedBB((x2 < x ? x2 : x) + 0.5 - 0.3, y2 + 0.0626, z + 0.5 - 0.3, (x2 > x ? x2 : x) + 0.5 + 0.3, y2 + 1.8 + 0.0626, z + 0.5 + 0.3);
        for (;;) {
            if (intersects(xBB, x, y, z) || intersects(xBB, x, y + 1, z) || intersects(xBB, x, y - 1, z)) {
                return false;
            } if (x == x2) {
                break;
            } if (x > x2) {
                --x;
            } else {
                ++x;
            }
        }

        //检查Z轴碰撞
        AxisAlignedBB zBB = new AxisAlignedBB(x2 + 0.5 - 0.3, y2 + 0.0626, (z2 < z ? z2 : z) + 0.5 - 0.3, x2 + 0.5 + 0.3, y2 + 1.8 + 0.0626, (z2 > z ? z2 : z) + 0.5 + 0.3);
        for (;;) {
            if (intersects(zBB, x, y, z) || intersects(zBB, x, y + 1, z) || intersects(zBB, x, y - 1, z)) {
                return false;
            } if (z == z2) {
                break;
            } if (z > z2) {
                --z;
            } else {
                ++z;
            }
        }

        //可通过
        return true;
    }

    //检查是否碰撞
    private boolean intersects(AxisAlignedBB bb,int x,int y,int z) {
        IBlockState blockState = world.getBlockState(mutableBlockPos.setPos(x, y, z));
        int size = boxList.size();
        blockState.getBlock().addCollisionBoxToList(blockState, world, new BlockPos(mutableBlockPos), bb, boxList, null, false);
        return size != boxList.size();
    }

    //向上vClip
    public void upVClip(int x,int y,int z,Point parent) {
        //最终Y
        int finalY = y;
        //距离限制
        int blinkDistance = (int) Math.floor(BlinkDistance) + 1;
        //寻找到最近的可用位置
        for (int i = 2; i < blinkDistance; ++i) {
            if (check(x, y + i, z)) {
                finalY = y + i;
            }
        }
        //如果最终Y具有意义
        if (finalY > y) {

            //计算代价
                int xDist = Math.abs(x - endX);
                int yDist = Math.abs(finalY - endY);
                int zDist = Math.abs(z - endZ);
                float gCost = parent.getGcost() + Math.abs(finalY - y);
                double fCost = gCost + xDist + yDist + zDist + Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);

            //添加到节点列表
            openList.add(new Point(x, finalY, z, parent, gCost, fCost));
        }
    }

    //向下vClip
    public void downVClip(int x,int y,int z,Point parent) {
        //最终Y
        int finalY = y;
        //距离限制
        int blinkDistance = (int) Math.floor(BlinkDistance) + 1;
        //寻找到最近的可用位置
        for (int i = 3; i < blinkDistance; ++i) {
            if (check(x, y - i, z)) {
                finalY = y - i;
            }
        }
        //如果最终Y具有意义
        if (finalY < y) {
            //计算代价
                int xDist = Math.abs(x - endX);
                int yDist = Math.abs(finalY - endY);
                int zDist = Math.abs(z - endZ);
                float gCost = parent.getGcost() + Math.abs(finalY - y);
                double fCost = gCost + xDist + yDist + zDist + Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
            //添加到节点列表
            openList.add(new Point(x, finalY, z, parent, gCost, fCost));
        }
    }
}
