package i.WinKcode.command;

import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.managers.CommandManager;

public class Help extends Command
{
	public Help()
	{
		super("help");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		for(Command cmd: CommandManager.commands)
		{
			if(cmd != this) {
				ChatUtils.message(cmd.getSyntax().replace("<", "<\2479").replace(">", "\2477>") + " - " + cmd.getDescription());
			}
		}
	}

	@Override
	public String getDescription()
	{
		return "列出所有命令.";
	}

	@Override
	public String getSyntax()
	{
		return "help";
	}
}