package i.WinKcode.command;

import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.network.play.client.CPacketChatMessage;

public class Say extends Command
{
	public Say()
	{
		super("say");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			String content = "";
			for(int i = 0; i < args.length; i++) {
				content = content + " " + args[i];
			}
			Wrapper.INSTANCE.sendPacket(new CPacketChatMessage(content));
		}
		catch(Exception e)
		{
			ChatUtils.error("Usage: " + getSyntax());
		}
	}

	@Override
	public String getDescription()
	{
		return "发送聊天消息.";
	}

	@Override
	public String getSyntax()
	{
		return "say <message>";
	}
}