package i.WinKcode.hack.hacks.another;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;

public class NoGuiEvents extends Hack{

	public NoGuiEvents() {
		super("NoGuiEvents", HackCategory.ANOTHER);
		this.GUIName = "Gui事件";
	}
	
	@Override
	public String getDescription() {
		return "当GUI打开时禁用事件.";
	}
}
