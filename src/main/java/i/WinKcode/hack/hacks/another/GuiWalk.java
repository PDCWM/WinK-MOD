package i.WinKcode.hack.hacks.another;

import i.WinKcode.gui.click.ClickGuiScreen;
import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.Utils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;

public class GuiWalk extends Hack{

	public GuiWalk() {
		super("GuiWalk", HackCategory.ANOTHER);
		this.GUIName = "打开GUI时走路";
	}
	
	@Override
	public String getDescription() {
		return "允许你在GUI打开时走路.";
	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		if (!(Wrapper.INSTANCE.mc().currentScreen instanceof GuiContainer) 
				&& !(Wrapper.INSTANCE.mc().currentScreen instanceof ClickGuiScreen))
			return;
		
		double speed = 0.05;
		
		if(!Wrapper.INSTANCE.player().onGround)
			speed /= 4.0;
		
		handleJump();
		handleForward(speed);
		
		if(!Wrapper.INSTANCE.player().onGround)
			speed /= 2.0;
		
		handleBack(speed);
		handleLeft(speed);
		handleRight(speed);
		
		super.onClientTick(event);
	}
	
	void moveForward(double speed) {
        float direction = Utils.getDirection();
        Wrapper.INSTANCE.player().motionX -= (double)(MathHelper.sin(direction) * speed);
        Wrapper.INSTANCE.player().motionZ += (double)(MathHelper.cos(direction) * speed);
	}
	
	void moveBack(double speed) {
        float direction = Utils.getDirection();
        Wrapper.INSTANCE.player().motionX += (double)(MathHelper.sin(direction) * speed);
        Wrapper.INSTANCE.player().motionZ -= (double)(MathHelper.cos(direction) * speed);
	}
	
	void moveLeft(double speed) {
        float direction = Utils.getDirection();
        Wrapper.INSTANCE.player().motionZ += (double)(MathHelper.sin(direction) * speed);
        Wrapper.INSTANCE.player().motionX += (double)(MathHelper.cos(direction) * speed);
	}
	
	void moveRight(double speed) {
        float direction = Utils.getDirection();
        Wrapper.INSTANCE.player().motionZ -= (double)(MathHelper.sin(direction) * speed);
        Wrapper.INSTANCE.player().motionX -= (double)(MathHelper.cos(direction) * speed);
	}
	
	void handleForward(double speed) {
		if(!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindForward.getKeyCode())) 
			return;
		moveForward(speed);
	}
	
	void handleBack(double speed) {
		if(!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindBack.getKeyCode())) 
			return;
		moveBack(speed);
	}
	
	void handleLeft(double speed) {
		if(!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindLeft.getKeyCode())) 
			return;
		moveLeft(speed);
	}
	
	void handleRight(double speed) {
		if(!Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindRight.getKeyCode())) 
			return;
		moveRight(speed);
	}
	
	void handleJump() {
		if(Wrapper.INSTANCE.player().onGround && 
    			Keyboard.isKeyDown(Wrapper.INSTANCE.mcSettings().keyBindJump.getKeyCode())) 
    		Wrapper.INSTANCE.player().jump();
	}

}
