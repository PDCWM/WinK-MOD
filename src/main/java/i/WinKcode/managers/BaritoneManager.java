package i.WinKcode.managers;

import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;

public class BaritoneManager {
    public static boolean isEnable = false;

    public BaritoneManager(){

    }

    public void init() {
        try {
            this.getClass().getClassLoader().loadClass("baritone.api.BaritoneAPI");
            Class.forName("baritone.api.BaritoneAPI");
            isEnable = true;

            this.getClass().getClassLoader().loadClass("i.WinKcode.BaritoneFS");
            Class<?> g = Class.forName("i.WinKcode.BaritoneFS");
            Method method = g.getMethod("status");
            method.invoke(g);

            /*
            Class<?> g = Class.forName("baritone.api.pathing.goals.GoalXZ");
            Constructor<?> cons = g.getConstructor(int.class, int.class);
            Class<?> cg = (Class<?>) cons.newInstance(1000, 1000);

            Class<?> s = BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().getClass();
            Method method = s.getMethod("setGoalAndPath", Object.class);
            method.invoke(s, cg);*/
        } catch (Exception e) {
            e.printStackTrace();
            isEnable = false;
        }
    }

    public boolean status() {
        init();
        if(!isEnable) {
            ChatUtils.error("未检测到BaritoneAPI模块,请在命令窗口输入 ai install 热加载模块.");
        }
        return isEnable;
    }

    public void install() {
        try {
            File jarFile = new File(FileManager.GISHCODE_DIR,"baritone-1.2.15.jar");
            if(!jarFile.exists()) {
                FileOutputStream fo = new FileOutputStream(jarFile);
                InputStream InputStream = this.getClass().getClassLoader().getResource("assets/baritone-1.2.15.jar").openStream();
                byte[] bs = new byte[1024];
                int length;
                while((length = InputStream.read(bs)) > 0){
                    fo.write(bs,0,length);
                }
                fo.close();
                InputStream.close();
            }

            String path = "file:" + jarFile.getAbsolutePath();

            URL url = jarFile.toURI().toURL();
            //new net.minecraft.launchwrapper.LaunchClassLoader.newInstance()

            String tweakName = "baritone.launch.BaritoneTweaker";

            Launch.classLoader.addURL(url);
            Launch.classLoader.loadClass(tweakName);
            Launch.classLoader.loadClass("org.spongepowered.asm.service.IMixinService");
            Launch.classLoader.loadClass("io.github.impactdevelopment.simpletweaker.SimpleTweaker");
            Launch.classLoader.loadClass("io.github.impactdevelopment.simpletweaker.transform.SimpleTransformer");
            /*Launch.classLoader.loadClass("baritone.api.IBaritoneProvider");
            Launch.classLoader.loadClass("javax.annotation.processing.Processor");
            Launch.classLoader.loadClass("org.spongepowered.asm.logging.LoggerAdapterConsole");
            Launch.classLoader.loadClass("org.spongepowered.asm.service.IGlobalPropertyService");
            Launch.classLoader.loadClass("org.spongepowered.asm.service.IMixinServiceBootstrap");
            Launch.classLoader.loadClass("org.spongepowered.tools.obfuscation.service.IObfuscationService");
            Launch.classLoader.loadClass("baritone.launch.BaritoneTweaker");*/

            final ITweaker tweaker = (ITweaker) Class.forName(tweakName, true, Launch.classLoader).newInstance();
            tweaker.acceptOptions(new ArrayList<>(), Wrapper.INSTANCE.mc().mcDataDir, Wrapper.INSTANCE.mc().mcDataDir, "");
            tweaker.injectIntoClassLoader(Launch.classLoader);

            //Object newInstance = classs.newInstance();
            //Method method = classs.getMethod("injectIntoClassLoader", LaunchClassLoader.class);
            //method.invoke(newInstance, Launch.classLoader);
            //((baritone.launch.BaritoneTweaker)newInstance).injectIntoClassLoader(Launch.classLoader);
        } catch (Exception e) {
            ChatUtils.error("热加载模块失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void unInstall() {

    }
}
