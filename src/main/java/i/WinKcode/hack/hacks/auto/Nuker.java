package i.WinKcode.hack.hacks.auto;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.BlockUtils;
import i.WinKcode.utils.PlayerControllerUtils;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.visual.RenderUtils;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.DoubleValue;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class Nuker extends Hack{
	
	public ModeValue mode;
    public DoubleValue distance;
    
    public final ArrayDeque<Set<BlockPos>> prevBlocks = new ArrayDeque<Set<BlockPos>>();
    public BlockPos currentBlock;
    public float progress;
    public float prevProgress;
    public int id;
	
	public Nuker() {
		super("Nuker", HackCategory.AUTO);
		this.GUIName = "自动破坏";
		
		this.mode = new ModeValue("类型", new Mode("ID", true), new Mode("All", false));
		distance = new DoubleValue("距离", 6.0D, 0.1D, 10.0D);
		
		this.addValue(mode, distance);
	}
	
	@Override
	public String getDescription() {
		return "自动打破你周围的障碍,或者自动破坏附近指定ID块.";
	}
	
	@Override
	public void onDisable() {
		if(currentBlock != null) {
			PlayerControllerUtils.setIsHittingBlock(true);
			Wrapper.INSTANCE.controller().resetBlockRemoving();
			currentBlock = null;
		}
		prevBlocks.clear();
		id = 0;
		super.onDisable();
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		currentBlock = null;
		
		Vec3d eyesPos = Utils.getEyesPos().subtract(0.5, 0.5, 0.5);
		BlockPos eyesBlock = new BlockPos(Utils.getEyesPos());
		
		double rangeSq = Math.pow(distance.getValue(), 2);
		int blockRange = (int)Math.ceil(distance.getValue());
		
		Stream<BlockPos> stream = StreamSupport.stream(BlockPos.getAllInBox(
						eyesBlock.add(blockRange, blockRange, blockRange),
						eyesBlock.add(-blockRange, -blockRange, -blockRange)).spliterator(), true);

		stream = stream.filter(pos -> eyesPos.squareDistanceTo(new Vec3d(pos)) <= rangeSq)
				.filter(pos -> BlockUtils.canBeClicked(pos))
				.sorted(Comparator.comparingDouble(pos -> eyesPos.squareDistanceTo(new Vec3d(pos))));
		
		if(mode.getMode("ID").isToggled()) {
			stream = stream.filter(pos -> Block.getIdFromBlock(BlockUtils.getBlock(pos)) == id);
		} 
		else if(mode.getMode("All").isToggled()) {
			//stream = stream.filter(pos -> BlockUtils.getHardness(pos) >= 1);
		}
		
		List<BlockPos> blocks = stream.collect(Collectors.toList());
		
		if(Wrapper.INSTANCE.player().capabilities.isCreativeMode){
			Stream<BlockPos> stream2 = blocks.parallelStream();
			
			for(Set<BlockPos> set : prevBlocks) {
				stream2 = stream2.filter(pos -> !set.contains(pos));
			}
			
			List<BlockPos> blocks2 = stream2.collect(Collectors.toList());
			prevBlocks.addLast(new HashSet<>(blocks2));
			
			while(prevBlocks.size() > 5) {
				prevBlocks.removeFirst();
			}
			
			if(!blocks2.isEmpty()) {
				currentBlock = blocks2.get(0);
			}
			
			Wrapper.INSTANCE.controller().resetBlockRemoving();
			progress = 1;
			prevProgress = 1;
			BlockUtils.breakBlocksPacketSpam(blocks2);
			return;
		}
		
		for(BlockPos pos : blocks)
			if(BlockUtils.breakBlockSimple(pos)){
				currentBlock = pos;
				break;
			}
		
		if(currentBlock == null) {
			Wrapper.INSTANCE.controller().resetBlockRemoving();
		}
		
		if(currentBlock != null && BlockUtils.getHardness(currentBlock) < 1) {
			prevProgress = progress;
		}
		
		progress = PlayerControllerUtils.getCurBlockDamageMP();	
			
		if(progress < prevProgress) {
			prevProgress = progress;
		} else {
			progress = 1;
			prevProgress = 1;
		}
		super.onClientTick(event);
	}
	
	@Override
	public void onLeftClickBlock(LeftClickBlock event) {
		if(mode.getMode("ID").isToggled() && Wrapper.INSTANCE.world().isRemote) {
			IBlockState blockState = BlockUtils.getState(event.getPos());
			id = Block.getIdFromBlock(blockState.getBlock());
		}
		super.onLeftClickBlock(event);
	}
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if(currentBlock == null) {
			return;
		}
		if(mode.getMode("All").isToggled()) {
			RenderUtils.drawBlockESP(currentBlock, 1.0f, 0.0f, 0.0f);
		} 
		else if(mode.getMode("ID").isToggled()) {
			RenderUtils.drawBlockESP(currentBlock, 0.0f, 0.0f, 1.0f);
		}
		super.onRenderWorldLast(event);
	}
	
	
}
