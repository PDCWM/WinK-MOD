package i.WinKcode.hack.hacks.player;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Objects;

public class AntiHunger extends Hack {
    public AntiHunger() {
        super("AntiHunger", HackCategory.PLAYER);
        this.GUIName = "反负面效果";
    }

    @Override
    public String getDescription() {
        return "清除所有负面效果.饥饿,中毒...";
    }

    @Override
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Wrapper.INSTANCE.player().getActivePotionEffects().isEmpty())
            return;

        Wrapper.INSTANCE.player().removePotionEffect(Objects.requireNonNull(Potion.getPotionById(17)));
        Wrapper.INSTANCE.player().removePotionEffect(Objects.requireNonNull(Potion.getPotionById(19)));
    }
}
