package i.WinKcode.managers;

import i.WinKcode.utils.visual.ChatUtils;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.math.BlockPos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class BaritoneManager {
    public static boolean isEnable = false;

    public BaritoneManager(){

    }

    public Object call(String method, Object... args){
        try {
            this.getClass().getClassLoader().loadClass("baritone.api.BaritoneAPI");
            Class.forName("baritone.api.BaritoneAPI");
            isEnable = true;

            this.getClass().getClassLoader().loadClass("i.WinKcode.BaritoneFS");
            Class<?> g = Class.forName("i.WinKcode.BaritoneFS");

            if (Arrays.stream(args).count() == 0){
                Method m = g.getMethod(method);
                return m.invoke(g);
            }

            if (Arrays.stream(args).count() == 1){
                Method m = g.getMethod(method, args[0].getClass());
                return m.invoke(g, args[0]);
            }

            if (Arrays.stream(args).count() == 2){
                Method m = g.getMethod(method, args[0].getClass(), args[1].getClass());
                return m.invoke(g, args[0], args[1]);
            }

            if (Arrays.stream(args).count() == 3){
                Method m = g.getMethod(method, args[0].getClass(), args[1].getClass(), args[2].getClass());
                return m.invoke(g, args[0], args[1], args[2]);
            }


        } catch (Exception e) {
            e.printStackTrace();
            isEnable = false;
        }
        return null;
    }

    public void init() {
        call("status");
    }

    public void GoalBlock(BlockPos bps) {
        call("GoalBlock", bps.getX(), bps.getY(), bps.getZ());
    }

    public boolean status() {
        init();
        //GoalBlock(1,1,1);
        if(!isEnable) {
            ChatUtils.error("未检测到BaritoneAPI模块,请在命令窗口输入 ai install 热加载模块.");
        }
        return isEnable;
    }

    public void install() {
        try {
            File jarFile = new File(FileManager.WinKMod_DIR,"baritone-1.2.15.jar");
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
