package i.WinKcode.command;

import i.WinKcode.Main;
import i.WinKcode.utils.visual.ChatUtils;

public class Ai extends Command
{
	public Ai()
	{
		super("ai");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			if(args[0].equalsIgnoreCase("status")) {
				if(Main.baritoneManager.status()){
					ChatUtils.message("正常");
				}
			}else
			if(args[0].equalsIgnoreCase("install")) {
				Main.baritoneManager.install();
			}else
			if(args[0].equalsIgnoreCase("uninstall")) {

			}
		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
			e.printStackTrace();
		}
	}

	@Override
	public String getDescription()
	{
		return "AI人工智能,由BaritoneAPI模块提供.";
	}

	@Override
	public String getSyntax()
	{
		return "ai <install/uninstall/status>";
	}
}