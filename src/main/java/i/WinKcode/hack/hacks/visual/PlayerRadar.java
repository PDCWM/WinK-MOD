package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.visual.ColorUtils;
import i.WinKcode.utils.visual.RenderUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;

public class PlayerRadar extends Hack {

	public PlayerRadar() {
		super("PlayerRadar", HackCategory.VISUAL);
		this.GUIName = "玩家雷达";
	}

	@Override
	public String getDescription() {
		return "显示你周围的所有玩家.";
	}

	@Override
	public void onRenderGameOverlay(Text event) {
		int y = Wrapper.INSTANCE.player().getActivePotionEffects().size() > 0 ? 28 : 2;
		ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());

		for (EntityPlayer e : Utils.getPlayersList()) {
			float range = Wrapper.INSTANCE.player().getDistance(e);
			float health = ((EntityPlayer) e).getHealth();

			String heal = " \u00a72[" + RenderUtils.DF(health, 0) + "] ";
			if (health >= 12.0) {
				heal = " \u00a72[" + RenderUtils.DF(health, 0) + "] ";
			} else if (health >= 4.0) {
				heal = " \u00a76[" + RenderUtils.DF(health, 0) + "] ";
			} else {
				heal = " \u00a74[" + RenderUtils.DF(health, 0) + "] ";
			}

			String name = e.getGameProfile().getName();
			String str = name + heal + "\u00a77" + "[" + RenderUtils.DF(range, 0) + "]";

			int color;
			if (e.isInvisible()) {
				color = ColorUtils.color(155, 155, 155, 255);
			} else {
				color = ColorUtils.color(255, 255, 255, 255);
			}

			Wrapper.INSTANCE.fontRenderer().drawString(str,
					sr.getScaledWidth() - Wrapper.INSTANCE.fontRenderer().getStringWidth(str), y, color);

			y += 12;
		}
		super.onRenderGameOverlay(event);
	}

}
