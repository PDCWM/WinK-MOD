package i.WinKcode.hack.hacks.another;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.utils.system.Connection.Side;
import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;
import joptsimple.internal.Strings;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.server.SPacketTabComplete;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PluginsGetter extends Hack{
    
	public PluginsGetter() {
		super("PluginsGetter", HackCategory.ANOTHER);
		this.GUIName = "插件获取";
	}
	
	@Override
	public String getDescription() {
		return "显示当前服务器上的所有插件.";
	}
	
	@Override
	public void onEnable() {
		if(Wrapper.INSTANCE.player() == null) {
            return;
		}
        Wrapper.INSTANCE.sendPacket(new CPacketTabComplete("/", null, false));
		super.onEnable();
	}
	
	@Override
	public boolean onPacket(Object packet, Side side) {
		 if(packet instanceof SPacketTabComplete) {
	            SPacketTabComplete s3APacketTabComplete = (SPacketTabComplete) packet;
	 
	            List<String> plugins = new ArrayList<String>();
	            String[] commands = s3APacketTabComplete.getMatches();
	 
	            for(int i = 0; i < commands.length; i++) {
	                String[] command = commands[i].split(":");
	 
	                if(command.length > 1) {
	                    String pluginName = command[0].replace("/", "");
	 
	                    if(!plugins.contains(pluginName)) {
	                        plugins.add(pluginName);
	                    }
	                }
	            }
	            
	            Collections.sort(plugins);
	            
	            if(!plugins.isEmpty()) {
	                ChatUtils.message("插件列表 \u00a77(\u00a78" + plugins.size() + "\u00a77): \u00a79" + Strings.join(plugins.toArray(new String[0]), "\u00a77, \u00a79"));
	            }
	            else
	            {
	                ChatUtils.error("没有找到插件.");
	            }
	            this.setToggled(false);   
	        }
		return true;
	}
}
