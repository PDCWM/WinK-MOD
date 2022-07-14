package i.WinKcode.command;

import i.WinKcode.utils.LoginUtils;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;

public class Login extends Command
{
	public Login()
	{
		super("login");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			if(args.length > 1 || args[0].contains(":")) {
				String email = "";
				String password = "";
				if(args[0].contains(":")) {
					String[] split = args[0].split(":", 2);
					email = split[0];
					password = split[1];
				}
				else
				{
					email = args[0];
					password = args[1];
				}
				String log = LoginUtils.loginAlt(email, password);
				ChatUtils.warning(log);
			} 
			else 
			{
				LoginUtils.changeCrackedName(args[0]);
				ChatUtils.warning("Logged [Cracked]: " + Wrapper.INSTANCE.mc().getSession().getUsername());
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
		return "更改会话.";
	}

	@Override
	public String getSyntax()
	{
		return "login <email> <password>/<nick>";
	}
}