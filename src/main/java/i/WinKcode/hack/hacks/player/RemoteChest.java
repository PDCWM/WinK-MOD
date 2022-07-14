package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.hacks.visual.ChestESP;
import i.WinKcode.managers.HackManager;
import i.WinKcode.utils.system.Connection;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.utils.visual.RenderUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.ArrayDeque;

public class RemoteChest extends Hack {
    public RemoteChest() {
        super("RemoteChest", HackCategory.PLAYER);
        this.GUIName = "远程开箱";
    }

    @Override
    public String getDescription() {
        return "远距离开启箱子,需要配合箱子透视使用.";
    }

    @Override
    public void onEnable() {
        if (!HackManager.getHack("ChestESP").isToggled()) {
            ChatUtils.warning("请先打开箱子透视配合使用.");
            this.toggle();
        }
    }

    BlockPos espBPo = null;

    @Override
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        ChestESP c = (ChestESP) HackManager.getHack("ChestESP");
        if (c.isToggled()) {
            EntityPlayer player = Wrapper.INSTANCE.player();
            ArrayDeque<BlockPos> espBPos = new ArrayDeque<BlockPos>();
            for (BlockPos pos : c.ChestsPos) {
                if (pos.getZ() > player.posZ - 7 && pos.getZ() < player.posZ + 7) {
                    if (pos.getX() > player.posX - 7 && pos.getX() < player.posX + 7) {
                        if (pos.getY() > player.posY - 7 && pos.getY() < player.posY + 7) {
                            espBPos.add(pos);
                        }
                    }
                }
            }
            float DistanceD = 7.5f;
            espBPo = null;
            for (BlockPos pos : espBPos) {
                float f = (float)(player.posX - pos.getX());
                float f1 = (float)(player.posY - pos.getY());
                float f2 = (float)(player.posZ - pos.getZ());
                float Distance = MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
                if(DistanceD > Distance) {
                    DistanceD = Distance;
                    espBPo = pos;
                }
            }
            if(espBPo != null) {
                RenderUtils.drawBlockESP(espBPo, 0F, 0F, 0F);
            }
        }else{
            this.toggle();
        }
    }

    @Override
    public void onMouse(MouseEvent event) {
        if (event.getButton() == 1 && espBPo != null) {
            // 远程开箱 7格范围
            //Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Position(espBPos.getX(), espBPos.getY() + 1, espBPos.getZ(), Wrapper.INSTANCE.player().onGround));
            Wrapper.INSTANCE.sendPacket(new CPacketPlayerTryUseItemOnBlock(
                    espBPo, EnumFacing.DOWN, EnumHand.MAIN_HAND,
                    (float) 0.29399353, (float) 0.875, (float) 0.7193027
            ));
            Wrapper.INSTANCE.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (packet instanceof net.minecraft.network.play.server.SPacketCloseWindow) {
            return false;
        }

        return true;
    }
}
