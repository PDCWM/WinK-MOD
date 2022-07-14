package i.WinKcode.command;

import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.hack.Hack;
import i.WinKcode.managers.HackManager;

public class Hacks extends Command
{
	public Hacks()
	{
		super("hacks");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		for(Hack hack : HackManager.getHacks()) {
			ChatUtils.message(String.format("%s (%s)\u00a79| \u00a7f%s \u00a79| \u00a7f%s \u00a79| \u00a7f%s", hack.getName(), hack.GUIName, hack.getCategory(), hack.getKey(), hack.isToggled()));
		}
		ChatUtils.message("Loaded " + HackManager.getHacks().size() + " Hacks.");
	}

	@Override
	public String getDescription()
	{
		return "列出所有黑客功能.";
	}

	@Override
	public String getSyntax()
	{
		return "hacks";
	}
}