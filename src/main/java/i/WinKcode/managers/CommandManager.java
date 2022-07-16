package i.WinKcode.managers;

import i.WinKcode.command.*;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;

import java.util.ArrayList;

public class CommandManager
{
	public static ArrayList<Command> commands = new ArrayList<Command>();
    private volatile static CommandManager instance;
	
	public static char cmdPrefix = '.';

	public CommandManager()
	{
		addCommands();
	}

	public void addCommands()
	{
		commands.add(new Help());
		commands.add(new Hacks());
		commands.add(new Bind());
		commands.add(new VClip());
		commands.add(new Login());
		commands.add(new Say());
		commands.add(new Effect());
		commands.add(new DumpPlayers());
		commands.add(new DumpClasses());
		commands.add(new SkinSteal());
		commands.add(new SelfDamage());
		commands.add(new SelfKick());
		commands.add(new Friend());
		commands.add(new Enemy());
		commands.add(new Toggle());
		commands.add(new PFilter());
		commands.add(new Config());
		commands.add(new XRay());
		commands.add(new Ai());
	}

	public void runCommands(String s)
	{
		String readString = s.trim().substring(Character.toString(cmdPrefix).length()).trim();
		boolean commandResolved = false;
		boolean hasArgs = readString.trim().contains(" ");
		String commandName = hasArgs ? readString.split(" ")[0] : readString.trim();
		String[] args = hasArgs ? readString.substring(commandName.length()).trim().split(" ") : new String[0];

		for(Command command: commands)
		{	
			if(command.getCommand().trim().equalsIgnoreCase(commandName.trim())) 
			{
				command.runCommand(readString, args);
				commandResolved = true;
				break;
			}
		}
		if(!commandResolved){
			ChatUtils.error("Cannot resolve internal command: \u00a7c"+commandName);
		}
	}
	
	public static void onKeyPressed(int key) {
		if (Wrapper.INSTANCE.mc().currentScreen != null) return;
		for(Command cmd : commands)
    		if(cmd.getKey() == key)
    			CommandManager.getInstance().runCommands("." + cmd.getExecute());
	}
	
	public static CommandManager getInstance(){
		if(instance == null){
			instance = new CommandManager();
		}
		return instance;
	}
}