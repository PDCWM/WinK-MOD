package i.WinKcode.wrappers;

import i.WinKcode.Main;
import i.WinKcode.managers.FileManager;
import i.WinKcode.utils.system.HttpRetVal;
import i.WinKcode.utils.system.HttpUtils;
import i.WinKcode.utils.system.property;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.Packet;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Wrapper {

	public static volatile Wrapper INSTANCE = new Wrapper();
	
    public Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public EntityPlayerSP player() {
        return Wrapper.INSTANCE.mc().player;
    }

    public WorldClient world() {
        return Wrapper.INSTANCE.mc().world;
    }

    public GameSettings mcSettings() {
        return Wrapper.INSTANCE.mc().gameSettings;
    }

    public FontRenderer fontRenderer() {
        return Wrapper.INSTANCE.mc().fontRenderer;
    }
    
    public void sendPacket(Packet packet) {
        this.player().connection.sendPacket(packet);
    }
    
    public InventoryPlayer inventory() { 
		return this.player().inventory; 
	}
	
	public PlayerControllerMP controller() { 
		return Wrapper.INSTANCE.mc().playerController; 
	}

	public static boolean checkUpdate() {
        File file = new File(FileManager.getDirectory(), "user.ini");
        if(!file.exists()) {
            return false;
        }

        String str1,str2;
        try (Scanner sc = new Scanner(new FileReader(file))) {
            sc.useDelimiter("\\|");  //分隔符
            str1 = sc.next();
            str2 = sc.next();
        } catch (Exception ignored) {
            return false;
        }

        List<property> list = new ArrayList<>();
        list.add(new property("User-Agent", Main.NAME + "|" + Main.VERSION + "|"
                + Main.MCVERSION));
        //Date d = new Date();
        //long date = new Date(d.getYear(),d.getMonth(),d.getDate()).getTime() / 1000;
        //String token = DigestUtils.md5Hex(str2 + date);
        list.add(new property("TOKEN", str1 + "|" + str2));
        HttpRetVal ret = HttpUtils.doGet("http://wm.go176.net/wink-wm/mod.php",list);
        if(ret.code != 200){
            JOptionPane.showMessageDialog(null, ret.value, "WinkMod", 2);
            return false;
        }
        //JOptionPane.showMessageDialog(null, ret.value);
        Main.QQ = str1;
        Main.QQ_NAME = ret.value;
        Display.setTitle(Main.NAME + " " + Main.VERSION + " 欢迎你：" + ret.value);
        return true;
    }
}
