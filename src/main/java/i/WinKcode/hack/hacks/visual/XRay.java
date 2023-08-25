package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.managers.HackManager;
import i.WinKcode.managers.XRayManager;
import i.WinKcode.utils.BlockUtils;
import i.WinKcode.utils.TimerUtils;
import i.WinKcode.utils.system.Connection;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.utils.visual.ColorUtils;
import i.WinKcode.utils.visual.RenderUtils;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.IntegerValue;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import i.WinKcode.xray.XRayBlock;
import i.WinKcode.xray.XRayData;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class XRay extends Hack {

    public IntegerValue distance;
    public ModeValue FakeMine;
	public IntegerValue delay;

	public LinkedList<BlockPos> LastBlocks = new LinkedList<BlockPos>();
	public LinkedList<XRayBlock> blocks = new LinkedList<XRayBlock>();

	private static final Lock bufferLock = new ReentrantLock();

	public int sendCount = 0;
	public int recvCount = 0;
	public BlockPos thisPos = new BlockPos(1,1,1);
	public boolean isXR = false;

	public XRay() {
		super("XRay", HackCategory.VISUAL);
		this.GUIName = "方块透视";
		FakeMine = new ModeValue("模式",
				new Mode("简易",true),
				new Mode("矿洞模式",false),
				new Mode("反假矿扫描",false),
				new Mode("智能模式",false)
		);
		distance = new IntegerValue("距离", 80, 4, 100);
		delay = new IntegerValue("更新延迟", 150, 0, 300);

		new String("别翻了 ！！说的就是你！！ 团队扩招中,有兴趣一起交流学习,加我时记得写备注,QQ:1601152156");

		this.addValue(FakeMine, distance, delay);
	}

	@Override
	public String getDescription() {
		return "允许你透过墙壁看到方块.";
	}

	@Override
	public void onEnable() {
		blocks.clear();
		if (!FakeMine.getMode("智能模式").isToggled()){
			isXR = true;
			new Thread(new XRayHandle(this)).start();
		}

		sendCount = 0;
		recvCount = 0;
		LastBlocks.clear();
	}

	@Override
	public void onKeyPressed(int key) {
		XRay x = (XRay) HackManager.getHack("XRay");
		if(x.FakeMine.getMode("反假矿扫描").isToggled()){
			if(key == Keyboard.KEY_Z){
				if(!x.isXR) {
					x.onEnable();
				}else {
					ChatUtils.warning("还没扫完呐.");
				}
			}
		}/*
		if(x.FakeMine.getMode("智能模式").isToggled()){
			if(key == Keyboard.KEY_X) {
				x.lastPx = (int)Wrapper.INSTANCE.player().posX;
				x.lastPz = (int)Wrapper.INSTANCE.player().posY;
				x.XrayAiStart = !x.XrayAiStart;
			}else if(key == Keyboard.KEY_Z){
				x.ClearBlock();
			}
		}*/
	}

	@Override
	public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
		if(FakeMine.getMode("反假矿扫描").isToggled() && isXR){
			ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
			String text = String.format("正在扫描正前方区块(send: %d | recv: %d)",sendCount,recvCount);
			int colorRect = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F);
			int colorRect2 = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F);

			int textWidth = Wrapper.INSTANCE.fontRenderer().getStringWidth(text);
			int xPos = sr.getScaledWidth() / 2 - (textWidth / 2);
			int yPos = sr.getScaledHeight() / 2 - 15;
			RenderUtils.drawBorderedRect(xPos - 2, yPos - 2, xPos + textWidth + 2, yPos + 10, 1, colorRect, ClickGui.getColor());
			RenderUtils.drawStringWithRect(text, xPos, yPos, ClickGui.getColor(),
					colorRect, colorRect2);
		}
		super.onRenderGameOverlay(event);
	}

	public void BlockChuck(IBlockState ibs, BlockPos bps){

		if (Block.getIdFromBlock(ibs.getBlock()) == 0) {
			for (XRayBlock x : blocks) {
				if (x.equals(
						new XRayBlock(
								bps,
								new XRayData(0, 1, 1, 1, 1)
						))) {
					bufferLock.lock();
					blocks.remove(x);
					bufferLock.unlock();
					return;
				}
			}
		}

		recvCount++;
		for (BlockPos pos : LastBlocks) {
			if(bps.getX() == pos.getX() && bps.getY() == pos.getY() && bps.getZ() == pos.getZ()) {
				return;
			}
		}

		LastBlocks.add(bps);

		for (XRayData xray : XRayManager.xrayList) {
			if (xray.getMeta() == ibs.getBlock().getMetaFromState(ibs) &&
					xray.getId() == Block.getIdFromBlock(ibs.getBlock())) {
				XRayBlock xBlock = new XRayBlock(bps, xray);
				for (XRayBlock x : blocks) {
					if (x.equals(xBlock)) {
						return;
					}
				}
				bufferLock.lock();
				blocks.add(xBlock);
				bufferLock.unlock();
			}
		}

	}

	@Override
	public boolean onPacket(Object packet, Connection.Side side) {
		if (FakeMine.getMode("反假矿扫描").isToggled()) {
			if (packet instanceof SPacketBlockChange) {
				BlockChuck(((SPacketBlockChange) packet).blockState,((SPacketBlockChange) packet).getBlockPosition());
			}
			if (packet instanceof SPacketMultiBlockChange) {
				for (SPacketMultiBlockChange.BlockUpdateData data : ((SPacketMultiBlockChange) packet).getChangedBlocks())
					BlockChuck(data.getBlockState(),data.getPos());
			}
		}
		return true;
	}

	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		bufferLock.lock();
		RenderUtils.drawBlockESP(thisPos,0.1f,0.1f,0.1f);
		RenderUtils.drawXRayBlocks(blocks, event.getPartialTicks());
		bufferLock.unlock();
		super.onRenderWorldLast(event);
	}

	public boolean isAirBlockByXYZ(int x, int y,int z){
		return Block.getIdFromBlock(BlockUtils.getBlock(new BlockPos(x, y, z))) == 0;
	}

	public int lastPx,lastPz;
	public boolean XrayAiStart = false;
	public void XrayAi(int distance){
		int px = (int)Wrapper.INSTANCE.player().posX;
		int pz = (int)Wrapper.INSTANCE.player().posZ;
		if (px == lastPx && pz == lastPz) return;

		int MaxX = 0,MaxZ = 0;
		if(px - lastPx > 0) MaxX = distance;
		if(px - lastPx < 0) MaxX = -distance;
		if(pz - lastPz > 0) MaxZ = distance;
		if(pz - lastPz < 0) MaxZ = -distance;
		if(Math.abs(MaxX) > Math.abs(MaxZ)){
			MaxZ = 0;
			MaxX = px + MaxX;
		}else{
			MaxX = 0;
			MaxZ = px + MaxZ;
		}

		if(MaxX != 0){
			int tx = px;
			while (MaxX != tx){
				if(isAirBlockByXYZ(tx,0,pz)){
					break;
				}
				if (px - lastPx > 0){
					tx++;
				}else{
					tx--;
				}
			}
			ChatUtils.warning("X轴最远边界: " + tx);
		}

		if(MaxZ != 0){
			int tz = pz;
			while (MaxZ != tz){
				if(isAirBlockByXYZ(px,0,tz)){
					break;
				}
				if (pz - lastPz > 0){
					tz++;
				}else{
					tz--;
				}
			}
			ChatUtils.warning("Z轴最远边界: " + tz);
		}

		lastPx = px;
		lastPz = pz;
	}

	@Override
	public void onClientTick(ClientTickEvent event) {
		if(XrayAiStart){
			// XrayAi(distance.getValue());
			// nextPacket();
		}

		super.onClientTick(event);
	}

	public void ClearFakeBlock() {
		LinkedList<XRayBlock> TBlocks = blocks;
		for(XRayBlock xrayB : TBlocks) {
			XRayData xray = xrayB.getxRayData();
			IBlockState block = BlockUtils.getState(xrayB.getBlockPos());
			if(xray.getMeta() != block.getBlock().getMetaFromState(block) ||
					xray.getId() != Block.getIdFromBlock(block.getBlock())) {
				TBlocks.remove(xrayB);
			}
		}
		this.setBlock(TBlocks);
	}

	public void ClearBlock() {
		blocks.clear();
	}

	public void setBlock(LinkedList<XRayBlock> xBlocks){
		bufferLock.lock();
		blocks.clear();
		blocks.addAll(xBlocks);
		bufferLock.unlock();
	}
}

class XRayHandle implements Runnable{ // 实现Runnable接口，作为线程的实现类
	private final XRay xr;
	private final TimerUtils timer;
	private final LinkedList<XRayBlock> TBlocks = new LinkedList<XRayBlock>();

	public XRayHandle (XRay xr){
		this.xr = xr;
		timer = new TimerUtils();
	}

	public void run() {
		while (xr.isToggled()){
			if(!timer.isDelay((long) (xr.delay.getValue() * 10))) {
				continue;
			}

			if(XRayManager.xrayList.size() == 0){
				ChatUtils.warning("方块都不加你让我透什么？");
				xr.setToggled(false);
				break;
			}

			int distance = xr.distance.getValue();
			if(xr.FakeMine.getMode("简易").isToggled()){
				DefXr(distance);
			}else if(xr.FakeMine.getMode("矿洞模式").isToggled()){
				XRayD(distance);
			}else if(xr.FakeMine.getMode("反假矿扫描").isToggled()){
				Dkl(distance);
				break;
			}else{
				break;
			}

			if(TBlocks.size() > 5000) {
				ChatUtils.warning("疑似遇到大量假矿,本次矿透已屏蔽.");
				TBlocks.clear();
			}

			xr.setBlock(TBlocks);
		}
		xr.isXR = false;
	}

	private EnumFacing facing;
	private int millis = 0;

	public void Dkl_dk(int tx,int tz) {
		int height = 20;
		try {

			while (height > 0) {
				if(millis > 30 || !xr.isToggled()) {
					break;
				}
				if(xr.recvCount + 200 < xr.sendCount){
					Thread.sleep(1000L);
					millis++;
					continue;
				}
				xr.sendCount++;
				millis = 0;
				BlockPos tPos = new BlockPos(tx, height, tz);
				xr.thisPos = tPos;
				Wrapper.INSTANCE.sendPacket(
						new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK
								, tPos, facing));
				height--;
				Thread.sleep(30L);
			}
			Thread.sleep(50L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void Dkl(int distance) {
		int x = (int) Wrapper.INSTANCE.player().posX;
		int z = (int) Wrapper.INSTANCE.player().posZ;
		facing = Wrapper.INSTANCE.player().getHorizontalFacing();
		if(facing == EnumFacing.EAST){
			for (int tx = x; tx <= x + 16; tx++) {
				for (int tz = z - 8; tz <= z + 8; tz++) {
					Dkl_dk(tx, tz);
				}
			}
		}else if(facing == EnumFacing.WEST){
			for (int tx = x; tx >= x - 16; tx--) {
				for (int tz = z - 8; tz <= z + 8; tz++) {
					Dkl_dk(tx, tz);
				}
			}
		}else if(facing == EnumFacing.NORTH){
			for (int tz = z; tz >= z - 16; tz--) {
				for (int tx = x - 8; tx <= x + 8; tx++) {
					Dkl_dk(tx, tz);
				}
			}
		}
		else if(facing == EnumFacing.SOUTH){
			for (int tz = z; tz <= z + 16; tz++) {
				for (int tx = x - 8; tx <= x + 8; tx++) {
					Dkl_dk(tx, tz);
				}
			}
		}

		if (millis > 30) {
			ChatUtils.warning("长时间未接收到更新包,反假矿扫描已结束.");
		}else{
			ChatUtils.warning("区块扫描已结束.");
		}
	}

	public void DefXr(int distance) {
		TBlocks.clear();
		TBlocks.addAll(BlockUtils.findBlocksReNearEntity(distance));
		timer.setLastMS();
	}

	public boolean isAirBlockByXYZ(int x, int y,int z) {
		return Block.getIdFromBlock(BlockUtils.getBlock(new BlockPos(x, y, z))) == 0;
	}

	public void XRayD(int distance) {
		TBlocks.clear();
		for(XRayData data : XRayManager.xrayList) {
			for (BlockPos blockPos : BlockUtils.findBlocksNearEntity(data.getId(), data.getMeta(), distance)) {
				int x = blockPos.getX(); int y = blockPos.getY(); int z = blockPos.getZ();
				XRayBlock xRayBlock = new XRayBlock(blockPos, data);
				if(isAirBlockByXYZ(x - 1,y,z)) {
					TBlocks.add(xRayBlock);
					continue;
				}
				if(isAirBlockByXYZ(x + 1,y,z)) {
					TBlocks.add(xRayBlock);
					continue;
				}
				if(isAirBlockByXYZ(x,y - 1,z)) {
					TBlocks.add(xRayBlock);
					continue;
				}
				if(isAirBlockByXYZ(x,y + 1,z)) {
					TBlocks.add(xRayBlock);
					continue;
				}
				if(isAirBlockByXYZ(x,y,z - 1)) {
					TBlocks.add(xRayBlock);
					continue;
				}
				if(isAirBlockByXYZ(x,y,z + 1)) {
					TBlocks.add(xRayBlock);
				}
			}
			timer.setLastMS();
		}
	}

}
