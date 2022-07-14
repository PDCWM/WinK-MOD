package i.gishreloaded.gishcode.hack.hacks;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import i.gishreloaded.gishcode.hack.Hack;
import i.gishreloaded.gishcode.hack.HackCategory;
import i.gishreloaded.gishcode.managers.XRayManager;
import i.gishreloaded.gishcode.utils.BlockUtils;
import i.gishreloaded.gishcode.utils.TimerUtils;
import i.gishreloaded.gishcode.utils.system.Connection;
import i.gishreloaded.gishcode.utils.visual.RenderUtils;
import i.gishreloaded.gishcode.value.types.BooleanValue;
import i.gishreloaded.gishcode.value.types.IntegerValue;
import i.gishreloaded.gishcode.wrappers.Wrapper;
import i.gishreloaded.gishcode.xray.XRayBlock;
import i.gishreloaded.gishcode.xray.XRayData;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class XRay extends Hack{

    public IntegerValue distance;
	public IntegerValue delay;
	public BooleanValue anti;
	public BooleanValue antiMagma;
	public TimerUtils timer;
	public boolean change = false;
	public boolean ready = true;
	
	LinkedList<XRayBlock> blocks = new LinkedList<XRayBlock>();
	//放置掉落岩浆
	LinkedList<XRayBlock> block = new LinkedList<>();
	public XRay() {
		super("矿物透视", HackCategory.VISUAL);
		distance = new IntegerValue("范围", 50, 4, 100);
		delay = new IntegerValue("更新速度", 100, 100, 300);
		antiMagma = new BooleanValue("防岩浆",true);
		anti = new BooleanValue("反假矿",false);
		timer = new TimerUtils();
		this.addValue(distance, delay,anti,antiMagma);
	}

	@Override
	public String getDescription() {
		return "允许你透视矿物";
	}
	
	@Override
	public void onEnable() {
		block.clear();
		blocks.clear();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		int distance = this.distance.getValue();
		if(!ready &&!timer.isDelay((long) (delay.getValue() * 10))) {
			return;
		}
		ready = false;
		blocks.clear();
		block.clear();
		for(XRayData data : XRayManager.xrayList) {
			for (BlockPos blockPos : BlockUtils.findBlocksNearEntity2(Wrapper.INSTANCE.player(), data.getId(), data.getMeta(), distance)) {
				XRayBlock xRayBlock = new XRayBlock(blockPos, data);
				if(antiMagma.getValue()){
				if (data.getId() == 11 || data.getId() == 10) {
					for (int i = 1; i <= 3; i++) {
						BlockPos blockPos1 = new BlockPos(blockPos.getX(), blockPos.getY() + i, blockPos.getZ());
						if (BlockUtils.getBlock(blockPos1) != Blocks.AIR && BlockUtils.getBlock(blockPos1)!= Blocks.MAGMA) {
							//用于放置不透明方块
							XRayBlock xRayBlock2 = new XRayBlock(blockPos1, data);
							block.add(xRayBlock2);
							break;
						}
						//放置透明方块
						if (i == 3 && BlockUtils.getBlock(blockPos1) == Blocks.AIR) {
							XRayBlock xRayBlock2 = new XRayBlock(blockPos1, data);
							block.add(xRayBlock2);
						}
					}
				}
			}
				else{
					if(xRayBlock.getxRayData().getId()!=10||xRayBlock.getxRayData().getId()!=11){
						blocks.add(xRayBlock);
					}
				}
				if(anti.getValue() && (change) && timer.isDelay((long) (delay.getValue() * 100))){
					new Thread(new Runnable() {
						@Override
						public void run() {
							System.out.println("run");;
							try{

							final ListIterator<XRayBlock> xRayBlockListIterator = blocks.listIterator();
								System.out.println(xRayBlockListIterator.toString());
							while(xRayBlockListIterator.hasNext()){
								Wrapper.INSTANCE.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, xRayBlockListIterator.next().getBlockPos(), EnumFacing.DOWN));
								Wrapper.INSTANCE.mc().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("发送数据包"+String.valueOf(xRayBlockListIterator.next().getBlockPos())));
								//Wrapper.INSTANCE.player().sendChatMessage(String.valueOf(xRayBlockListIterator.next().getBlockPos()));
							}
								Thread.sleep(1000);
							}catch(Exception e){
							blocks.clear();
						}
							ready = true;
							timer.setLastMS();
							change =false;
						}
					}
					).start();
				}
			}
		}
//		for(XRayBlock csgo : blocks){
//			BlockPos blockPos = csgo.getBlockPos();
//			for(int i = 0;i<=6;i++){
//				if(BlockUtils.getBlock(blockPos) != Blocks.AIR){
//					block.add(csgo);
//					continue;
//				}
//			}
//		}
		if(!anti.getValue()){
			timer.setLastMS();
			ready = true;
		}

		super.onClientTick(event);
	}

	@Override
	public boolean onPacket(Object packet, Connection.Side side) {
		if(side == Connection.Side.IN){
			if(packet instanceof SPacketBlockChange){
				Wrapper.INSTANCE.mc().ingameGUI.getChatGUI().printChatMessage(new TextComponentString("获取到服务器包改变"));

				change = true;
			}
		}
		return super.onPacket(packet, side);
	}

	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		RenderUtils.drawXRayBlocks(blocks, event.getPartialTicks());
		RenderUtils.drawXRayBlocks(block, event.getPartialTicks());
		super.onRenderWorldLast(event);
	}
}
