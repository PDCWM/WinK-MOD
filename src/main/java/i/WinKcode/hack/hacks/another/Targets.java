package i.WinKcode.hack.hacks.another;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.value.types.BooleanValue;

public class Targets extends Hack{

	public BooleanValue players;
    public BooleanValue mobs;
    public BooleanValue invisibles;
    public BooleanValue murder;
    
	public Targets() {
		super("Targets", HackCategory.ANOTHER);
		this.GUIName = "目标";
		this.setShow(false);
		this.setToggled(true);
		
		players = new BooleanValue("玩家", true);
		mobs = new BooleanValue("生物", false);
		invisibles = new BooleanValue("隐形人", false);
		murder = new BooleanValue("Murder", false);
		
		addValue(players, mobs, invisibles, murder);
	}
	
	@Override
	public String getDescription() {
		return "管理黑客攻击目标.";
	}
}
