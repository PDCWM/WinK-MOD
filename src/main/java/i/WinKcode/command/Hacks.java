package i.WinKcode.command;

import i.WinKcode.hack.Hack;
import i.WinKcode.managers.HackManager;
import i.WinKcode.utils.visual.ChatUtils;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class Hacks extends Command
{
	public Hacks()
	{
		super("hacks");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			if(args.length > 0 && args[0].equalsIgnoreCase("load")){
				// 动态替换功能class
				Hack hack = HackManager.getHack(args[1]);
				if(hack != null){
					String path = "E:\\源码中心\\java源码\\Minecraft\\WinK-1.12.2-new\\build\\classes\\java\\main\\";
					String className = "i.WinKcode.hack.hacks." + hack.getCategory().toString().toLowerCase() + "." + hack.getName();
					ChatUtils.warning(className);

					HackManager.removeHack(hack);
					hack = null;
					System.gc();

					// 首先是动态加载，如果从文件加载，需要获取文件的位置：
					File file= new File(path);
					URL url=file.toURI().toURL();
					// 然后加载类：
					ClassLoader loader = new URLClassLoader(new URL[]{url});
					Class<?> cls = loader.loadClass(className);
					Object o = cls.newInstance();
					if(o instanceof Hack) {
						Hack obj = (Hack) o;
						HackManager.addHack(obj);
					}else{
						ChatUtils.error("类型不符");
					}
				}else{
					ChatUtils.error("没有该黑客功能.");
				}
				return;
			}
			for(Hack hack : HackManager.getHacks()) {
				ChatUtils.message(String.format("%s (%s) \u00a79| \u00a7f%s \u00a79| \u00a7f%s \u00a79| \u00a7f%s", hack.getName(), hack.GUIName, hack.getCategory(), hack.getKey(), hack.isToggled()));
			}
			ChatUtils.message("Loaded " + HackManager.getHacks().size() + " Hacks.");
		}
		catch(Exception e)
		{
			//ChatUtils.error(e.getLocalizedMessage());
			ChatUtils.error("Usage: " + getSyntax());
		}
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