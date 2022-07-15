package i.WinKcode.command;

import java.util.ArrayList;

import i.WinKcode.utils.Utils;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.network.NetworkPlayerInfo;

public class DumpPlayers extends Command
{
	public DumpPlayers()
	{
		super("dumpplayers");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			ArrayList<String> list = new ArrayList<String>();
			
			if(args[0].equalsIgnoreCase("all")) {
				for(NetworkPlayerInfo npi : Wrapper.INSTANCE.mc().getConnection().getPlayerInfoMap()) {
					list.add("\n" + npi.getGameProfile().getName());
				}
			}
			else
			if(args[0].equalsIgnoreCase("creatives")) {
				for(NetworkPlayerInfo npi : Wrapper.INSTANCE.mc().getConnection().getPlayerInfoMap()) {
					if(npi.getGameType().isCreative()) {
						list.add("\n" + npi.getGameProfile().getName());
					}
				}	
			}
			
			if(list.isEmpty()) {
				ChatUtils.error("列表为空.");
			}
			else
			{
				Utils.copy(list.toString());
				ChatUtils.message("列表已复制到剪贴板.");
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
		return "获取玩家列表.";
	}

	@Override
	public String getSyntax()
	{
		return "dumpplayers <all/creatives>";
	}
}