package i.WinKcode.hack.hacks.another;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;

public class Enemys extends Hack{

	public Enemys() {
		super("Enemys", HackCategory.ANOTHER);
		this.GUIName = "敌人";
	}
	
	@Override
	public String getDescription() {
		return "目标只在敌人名单中.";
	}
}
