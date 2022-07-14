package i.WinKcode.hack.hacks.auto;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.system.Connection;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AutoFish extends Hack {
    public AutoFish() {
        super("AntiFish", HackCategory.AUTO);
        this.GUIName = "自动钓鱼";
    }

    @Override
    public String getDescription() {
        return "解放双手,挂机钓鱼.居家旅行的利器";
    }

    private int isFish = -2;

    @Override
    public void onEnable() {
        isFish = -2;
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        /*ItemStack stack = Wrapper.INSTANCE.player().getHeldItemMainhand();
        if(stack.getItem() instanceof ItemTool) {
            ItemTool itemTool = (ItemTool) stack.getItem();
            ChatUtils.warning(itemTool.getToolMaterialName());
            return;
        }*/
        if (isFish == -1)
            return;
        if (isFish == -2) {
            Wrapper.INSTANCE.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            Wrapper.INSTANCE.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            isFish = -1;
            return;
        }

        if(isFish > 0) {
            isFish++;
        }

        if(isFish == 30) {
            Wrapper.INSTANCE.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            Wrapper.INSTANCE.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        }

        if(isFish > 100) {
            isFish = -2;
        }
    }

    @Override
    public boolean onPacket(Object packet, Connection.Side side) {
        if (packet instanceof net.minecraft.network.play.server.SPacketSoundEffect) {
            if(((SPacketSoundEffect) packet).getSound().getSoundName().toString().equals("minecraft:entity.bobber.splash")){
                isFish = 1;
            }
        }
        return true;
    }
}
