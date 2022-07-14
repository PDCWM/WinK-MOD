package i.WinKcode.xray;

import net.minecraft.util.math.BlockPos;

public class XRayBlock {

    private final BlockPos blockPos;
    private final XRayData xRayData;

    public XRayBlock(BlockPos blockPos, XRayData xRayData){
        this.blockPos = blockPos;
        this.xRayData = xRayData;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof XRayBlock){
            BlockPos pos = ((XRayBlock) obj).getBlockPos();
            return this.blockPos.getX() == pos.getX() && this.blockPos.getZ() == pos.getZ() && this.blockPos.getY() == pos.getY();
        }
        return false;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public XRayData getxRayData() {
        return xRayData;
    }
}
