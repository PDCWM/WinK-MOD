package i.WinKcode.command;

import i.WinKcode.managers.FileManager;
import i.WinKcode.utils.visual.ChatUtils;

import java.awt.*;

public class Config extends Command
{
	public Config()
	{
		super("config");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			if(args[0].equalsIgnoreCase("opendir")) {
				Desktop.getDesktop().open(FileManager.WinKMod_DIR);
			}

			if(args[0].equalsIgnoreCase("load")) {
				FileManager.allLoad();
			}

			if(args[0].equalsIgnoreCase("save")) {
				FileManager.allSave();
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
		return "配置文件操作.";
	}

	@Override
	public String getSyntax()
	{
		return "config <opendir/load/save>";
	}
}