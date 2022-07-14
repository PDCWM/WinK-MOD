package i.WinKcode.command;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import i.WinKcode.managers.SkinChangerManager;
import i.WinKcode.utils.visual.ChatUtils;

public class SkinSteal extends Command
{
	public SkinSteal()
	{
		super("skinsteal");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			SkinChangerManager.addTexture(Type.SKIN, args[0]);
			//if(args[0].equalsIgnoreCase("add")) {
				//SkinChangerManager.setTexture(Type.valueOf(args[1].toUpperCase()), args[2]);
//			} else 
//				if(args[0].equalsIgnoreCase("remove")) {
//					SkinChangerManager.removeTexture(Type.valueOf(args[1].toUpperCase()));
//				}
//				else 
//					if(args[0].equalsIgnoreCase("clear")) {
//						SkinChangerManager.clear();
//					}
		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription()
	{
		return "为'换肤者'窃取皮肤.";
	}

	@Override
	public String getSyntax()
	{
		return "skinsteal <nickname/URL>";
	}
}