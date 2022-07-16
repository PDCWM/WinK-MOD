/*******************************************************************************
 *     WinKMod a Forge Hacked Client
 *     Copyright (C) 2019 Gish_Reloaded
 ******************************************************************************/
package i.WinKcode;

import i.WinKcode.managers.BaritoneManager;
import i.WinKcode.managers.FileManager;
import i.WinKcode.managers.HackManager;
import i.WinKcode.utils.system.Nan0EventRegister;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;

/**
 * Created by Gish_Reloaded on 23/07/2019.
 */

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, acceptableRemoteVersions = "*")
public class Main {
	
	public static final String MODID = "wink-wm";
	public static final String NAME = "WinK";
	public static final String NAMECN = "WinK-Mod";
	public static final String VERSION = "beta V2";
	public static final String MCVERSION = "1.12.2";
	public static int initCount = 0;
	public static HackManager hackManager;
	public static BaritoneManager baritoneManager;
	public static FileManager fileManager;
	public static EventsHandler eventsHandler;

	private static final int MAX_TITLE_LENGTH = 1024;

	public static int GUI_PORTABLE_CRAFT_BENCH_ID = 1;

	public Main() {
		init(null);
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent E) {
		//proxy.preInitialization();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent E) {
		if(initCount > 0) { return; }
		Display.setTitle(NAME + " " + VERSION);

		hackManager = new HackManager();
		fileManager = new FileManager();
		eventsHandler = new EventsHandler();
		baritoneManager = new BaritoneManager();
		Nan0EventRegister.register(MinecraftForge.EVENT_BUS, eventsHandler);
		Nan0EventRegister.register(FMLCommonHandler.instance().bus(), eventsHandler);
		initCount++;

		//proxy.initialization();
		//NetworkRegistry.INSTANCE.registerGuiHandler(this, new PortableCraftBenchGuiHandler());
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent E) {
		//proxy.postInitialization();
	}
}
