package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;
import i.WinKcode.utils.BlockUtils;
import i.WinKcode.utils.visual.RenderUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class BlockOverlay extends Hack{

	public BlockOverlay() {
		super("BlockOverlay", HackCategory.VISUAL);
		this.GUIName = "目标方块框";
	}
	
	@Override
    public String getDescription() {
        return "突出显示你鼠标指定的目标块.";
    }
	
	@Override
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if(Wrapper.INSTANCE.mc().objectMouseOver == null) {
			return;
		}
		if (Wrapper.INSTANCE.mc().objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            Block block = BlockUtils.getBlock(Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos());
            BlockPos blockPos = Wrapper.INSTANCE.mc().objectMouseOver.getBlockPos();

            if (Block.getIdFromBlock(block) == 0) {
                return;
            }
            RenderUtils.drawBlockESP(blockPos, 1F, 1F, 1F);
        }
		
		super.onRenderWorldLast(event);
	}

}
