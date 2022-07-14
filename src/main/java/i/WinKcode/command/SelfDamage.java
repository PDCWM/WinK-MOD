package i.WinKcode.command;

import i.WinKcode.utils.Utils;
import i.WinKcode.utils.visual.ChatUtils;

public class SelfDamage extends Command
{
	public SelfDamage()
	{
		super("selfdamage");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{   //0.0625D
			double damage = Double.parseDouble(args[0]);
			Utils.selfDamage(damage);
		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription()
	{
		return "对你造成伤害（用于绕过AC）.";
	}

	@Override
	public String getSyntax()
	{
		return "selfdamage <damage>";
	}
}