package i.gishreloaded.gishcode.hack.hacks;

import i.gishreloaded.gishcode.hack.Hack;
import i.gishreloaded.gishcode.hack.HackCategory;
import i.gishreloaded.gishcode.managers.XRayManager;
import i.gishreloaded.gishcode.utils.BlockUtils;
import i.gishreloaded.gishcode.utils.TimerUtils;
import i.gishreloaded.gishcode.utils.visual.RenderUtils;
import i.gishreloaded.gishcode.value.types.IntegerValue;
import i.gishreloaded.gishcode.wrappers.Wrapper;
import i.gishreloaded.gishcode.xray.XRayBlock;
import i.gishreloaded.gishcode.xray.XRayData;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.LinkedList;

public class AntiMagma extends Hack {
    public IntegerValue distance;
    public IntegerValue AntiDistance;
    public TimerUtils timer;
    LinkedList<XRayBlock> block = new LinkedList<>();
    LinkedList<XRayData> xrayList = new LinkedList<>();
    public AntiMagma() {
        super("AntiMagma", HackCategory.PLAYER);
        distance = new IntegerValue("Distance", 10, 4, 20);
        AntiDistance = new IntegerValue("AntiDistance", 3, 2, 6);
        timer = new TimerUtils();
        this.addValue(distance,AntiDistance);
        xrayList.add(new XRayData(10,0,255,0,0));
        xrayList.add(new XRayData(11,0,255,0,0));
    }
    @Override
    public String getDescription() {
        return "AntiMagma";
    }

    @Override
    public void onEnable() {
       block.clear();
    }
    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        int distance = this.distance.getValue();
        if(!timer.isDelay((long) (1000))) {
            return;
        }
        block.clear();
        for(XRayData data : xrayList) {
            for (BlockPos blockPos : BlockUtils.findBlocksNearEntity2(Wrapper.INSTANCE.player(), data.getId(), data.getMeta(), distance)) {
                if(data.getId()==11 || data.getId()==10){
                    for(int i = 1;i<=AntiDistance.getValue();i++){
                        BlockPos blockPos1 = new BlockPos(blockPos.getX(),blockPos.getY()+i,blockPos.getZ());
                        if(BlockUtils.getBlock(blockPos1) != Blocks.AIR){
                            XRayBlock xRayBlock2 = new XRayBlock(blockPos1,data);
                            block.add(xRayBlock2);
                            break;
                        }
                        if(i==AntiDistance.getValue() && BlockUtils.getBlock(blockPos1) == Blocks.AIR){
                            XRayBlock xRayBlock2 = new XRayBlock(blockPos1,data);
                            block.add(xRayBlock2);
                        }
                    }
                }

            }
        }
        timer.setLastMS();
    }
    @Override
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        RenderUtils.drawXRayBlocks(block, event.getPartialTicks());
        super.onRenderWorldLast(event);
    }
}
