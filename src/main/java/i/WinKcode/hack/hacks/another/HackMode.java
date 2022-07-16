package i.WinKcode.hack.hacks.another;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;

public class HackMode extends Hack{
	
	public static boolean enabled = false;
	
	public HackMode() {
		super("HackMode", HackCategory.ANOTHER);
		this.GUIName = "黑客模式";
	}
	
	@Override
	public String getDescription() {
		return "禁用所有黑客,绑定按键后生效.";
	}
	
	@Override
	public void onEnable() {
		if(this.getKey() == -1) 
			return;
		enabled = true;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		enabled = false;
		super.onDisable();
	}
	
}
