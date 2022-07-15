package i.WinKcode.command;

import i.WinKcode.managers.PickupFilterManager;
import i.WinKcode.utils.visual.ChatUtils;

public class PFilter extends Command
{
	public PFilter()
	{
		super("pfilter");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			if(args[0].equalsIgnoreCase("add")) {
				PickupFilterManager.addItem(Integer.parseInt(args[1]));
			}
			else
			if(args[0].equalsIgnoreCase("remove")) {
				PickupFilterManager.removeItem(Integer.parseInt(args[1]));
			}
			else
			if(args[0].equalsIgnoreCase("clear")) {
				PickupFilterManager.clear();
			}
		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription()
	{
		return "拾取过滤器管理.";
	}

	@Override
	public String getSyntax()
	{
		return "pfilter add <id> | remove <id> | clear";
	}
}