package i.WinKcode.hack.hacks.auto;

import i.WinKcode.gui.click.ClickGuiScreen;
import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.managers.HackManager;
import i.WinKcode.utils.Utils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;

public class AutoSprint extends Hack{

	public AutoSprint() {
		super("AutoSprint", HackCategory.AUTO);
		this.GUIName = "自动冲刺";
	}
	
	@Override
    public String getDescription() {
        return "当你应该走路时自动冲刺.";
    }
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if(isMoveInGui() && this.canSprint(false)) {
			Wrapper.INSTANCE.player().setSprinting(true); 
			return;
		}
		if(this.canSprint(true))
			Wrapper.INSTANCE.player().setSprinting(Utils.isMoving(Wrapper.INSTANCE.player()));
		super.onClientTick(event);
	}
	
	boolean isMoveInGui() {
		return Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode())
				&& (boolean)(Wrapper.INSTANCE.mc().currentScreen instanceof GuiContainer
				|| Wrapper.INSTANCE.mc().currentScreen instanceof ClickGuiScreen)
				&& HackManager.getHack("GuiWalk").isToggled();
	}
	
	boolean canSprint(boolean forward) {
		if(!Wrapper.INSTANCE.player().onGround) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isSprinting()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isOnLadder()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isInWater()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isInLava()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().collidedHorizontally) {
			return false;
		}
		if(forward && Wrapper.INSTANCE.player().moveForward < 0.1F) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isSneaking()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().getFoodStats().getFoodLevel() < 6) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isRiding()) {
			return false;
		}
		if(Wrapper.INSTANCE.player().isPotionActive(MobEffects.BLINDNESS)) {
			return false;
		}
        return true;
    }
}
