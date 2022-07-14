package i.WinKcode.command;

import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.managers.HackManager;

public class Toggle extends Command
{
	public Toggle()
	{
		super("toggle");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			HackManager.getHack(args[0]).toggle();
		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription()
	{
		return "开启/关闭选定的黑客功能.";
	}

	@Override
	public String getSyntax()
	{
		return "toggle <hackname>";
	}
}