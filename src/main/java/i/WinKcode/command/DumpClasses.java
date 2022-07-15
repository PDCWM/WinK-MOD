package i.WinKcode.command;

import i.WinKcode.utils.Utils;
import i.WinKcode.utils.visual.ChatUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;

public class DumpClasses extends Command
{
	public DumpClasses()
	{
		super("dumpclasses");
	}

	@Override
	public void runCommand(String s, String[] args)
	{
		try
		{
			ArrayList<String> list = new ArrayList<String>();
			
			Field f = ClassLoader.class.getDeclaredField("classes");
			f.setAccessible(true);

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Vector<Class> classes =  (Vector<Class>) f.get(classLoader);
			
	        for (Class<?> clazz: classes) {
	        	String className = clazz.getName();
	        	if(args.length > 0) {
	        		if(className.contains(args[0]))
	        			list.add("\n" + className);
	        	} else { list.add("\n" + className); }
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
		return "通过正则表达式从 ClassLoader 获取类.";
	}

	@Override
	public String getSyntax()
	{
		return "dumpclasses <regex>";
	}
}