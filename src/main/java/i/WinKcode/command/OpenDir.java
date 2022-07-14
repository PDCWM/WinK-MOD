package i.WinKcode.command;

import java.awt.Desktop;

import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.managers.FileManager;

public class OpenDir extends Command
{
	public OpenDir()
	{
		super("opendir");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			Desktop.getDesktop().open(FileManager.GISHCODE_DIR);
		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription()
	{
		return "打开配置目录.";
	}

	@Override
	public String getSyntax()
	{
		return "opendir";
	}
}