package i.WinKcode.command;

import i.WinKcode.managers.EnemyManager;
import i.WinKcode.utils.Utils;
import i.WinKcode.utils.visual.ChatUtils;
import net.minecraft.entity.player.EntityPlayer;

public class Enemy extends Command
{
	public Enemy()
	{
		super("enemy");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			if(args[0].equalsIgnoreCase("add")) {
				if(args[1].equalsIgnoreCase("all")) {
					for(Object object : Utils.getEntityList()) {
						if(object instanceof EntityPlayer) {
							EntityPlayer player = (EntityPlayer) object;
							if(!player.isInvisible()) {
								EnemyManager.addEnemy(Utils.getPlayerName(player));
							}
						}
					}
				} else {
					EnemyManager.addEnemy(args[1]);
				}
			}
			else
			if(args[0].equalsIgnoreCase("remove")) {
				EnemyManager.removeEnemy(args[1]);
			}
			else
			if(args[0].equalsIgnoreCase("clear")) {
				EnemyManager.clear();
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
		return "敌人管理.";
	}

	@Override
	public String getSyntax()
	{
		return "enemy <add/remove/clear> <nick>";
	}
}