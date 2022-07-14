package i.WinKcode.hack.hacks;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.visual.ChatUtils;

public class TestHack extends Hack {
	
	public TestHack() {
		super("TestHack", HackCategory.ANOTHER);
		this.GUIName = "TestHack";
	}
	
	@Override
	public String getDescription() {
		return "我不建议启用此功能!";
	}
	
	@Override
	public void onEnable() {
		ChatUtils.warning("Why ?");
		ChatUtils.warning("Fuck You!");
		super.onEnable();
	}
	
	
}
