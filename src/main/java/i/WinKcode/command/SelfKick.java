package i.WinKcode.command;

import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.network.play.client.CPacketPlayer;

public class SelfKick extends Command
{
	public SelfKick()
	{
		super("selfkick");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			Wrapper.INSTANCE.sendPacket(new CPacketPlayer.Rotation(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, false));
		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription()
	{
		return "让服务器踢出你.";
	}

	@Override
	public String getSyntax()
	{
		return "selfkick";
	}
}