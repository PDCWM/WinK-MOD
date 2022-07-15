package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.HackCategory;
import i.WinKcode.managers.FastGuiManager;
import i.WinKcode.hack.Hack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.input.Keyboard;

public class FastGUI extends Hack{

    public FastGUI() {
        super("FastGUI", HackCategory.VISUAL);
        this.GUIName = "快速GUI";
        this.setKey(Keyboard.KEY_RSHIFT);
        this.setShow(false);
    }

    @Override
    public String getDescription() {
        return "新版图形界面.";
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {

        FastGuiManager.FastGuiDraw();
        super.onRenderGameOverlay(event);
    }

    @Override
    public void onKeyPressed(int key){
        FastGuiManager.onKeyPressed(key);
    }
}
