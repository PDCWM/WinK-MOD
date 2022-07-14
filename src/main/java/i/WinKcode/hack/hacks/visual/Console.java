package i.WinKcode.hack.hacks.visual;

import i.WinKcode.gui.GuiConsole;
import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.hacks.another.GhostMode;
import i.WinKcode.wrappers.Wrapper;
import org.lwjgl.input.Keyboard;

public class Console extends Hack{

	public Console() {
		super("Console", HackCategory.VISUAL);
		this.GUIName = "控制台";
		this.setKey(Keyboard.KEY_Y);
		this.setShow(false);
	}
	
	@Override
	public String getDescription() {
		return "控制台.";
	}

	@Override
	public void onEnable() {
		if(GhostMode.enabled) return;
		Wrapper.INSTANCE.mc().displayGuiScreen(new GuiConsole());

		super.onEnable();
	}
}
