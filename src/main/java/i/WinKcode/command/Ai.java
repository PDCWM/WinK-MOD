package i.WinKcode.command;

import i.WinKcode.hack.hacks.auto.AiCore;
import i.WinKcode.managers.HackManager;
import i.WinKcode.utils.visual.ChatUtils;
import net.minecraft.util.math.BlockPos;


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
			AiCore ac = (AiCore) HackManager.getHack("AiCore");
			if(ac==null || !ac.isToggled()){
				ChatUtils.warning("需要开启自动核心!");
				return;
			}

			if(args[0].equalsIgnoreCase("goto")) {
				//ChatUtils.message(String.format("X:%s Y:%s Z:%s",
				//		Wrapper.INSTANCE.player().getPosition().getX(),
				//		Wrapper.INSTANCE.player().getPosition().getY(),
				//		Wrapper.INSTANCE.player().getPosition().getZ()));
				//ChatUtils.message(String.format("X:%s Y:%s Z:%s",args[1],args[2],args[3]));
				ac.findPath(new BlockPos(Integer.parseInt(args[1]),
						Integer.parseInt(args[2]),
						Integer.parseInt(args[3])));
			}

			if(args[0].equalsIgnoreCase("help")) {

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
		return "执行AI智能命令.";
	}

	@Override
	public String getSyntax()
	{
		return "ai goto <x> <y> <z> | stop | help";
	}
}