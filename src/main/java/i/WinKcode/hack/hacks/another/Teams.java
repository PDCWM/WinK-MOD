package i.WinKcode.hack.hacks.another;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.Hack;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.ModeValue;

public class Teams extends Hack{

	public ModeValue mode;
	
	public Teams() {
		super("Teams", HackCategory.ANOTHER);
		this.GUIName = "团队";
		this.mode = new ModeValue("模式", new Mode("Base", false), new Mode("ArmorColor", false), new Mode("NameColor", true));
		this.addValue(mode);
	}
	
	@Override
	public String getDescription() {
		return "忽略你的队友.";
	}

}
