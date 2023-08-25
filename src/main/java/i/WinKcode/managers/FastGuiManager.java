package i.WinKcode.managers;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.hacks.visual.ClickGui;
import i.WinKcode.utils.visual.ColorUtils;
import i.WinKcode.utils.visual.RenderUtils;
import org.lwjgl.input.Keyboard;

public class FastGuiManager {

    private static HackCategory category = HackCategory.PLAYER;
    private static Hack THack;
    private static int TConut = 0;
    private static int TSelect = 0;

    private static boolean isFash = false;

    public static void FastGuiDraw(){
        //ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
        //int colorRect = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F);
        //int colorRect2 = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F);
        //RenderUtils.drawStringWithRect("\u00a77FPS: \u00a7f" + Wrapper.INSTANCE.mc().getDebugFPS(), 4, heightFPS,
        // ClickGui.getColor(), colorRect, colorRect2);
        String name = "";

        if(category == HackCategory.PLAYER) name = "玩家";
        if(category == HackCategory.VISUAL) name = "视觉";
        if(category == HackCategory.COMBAT) name = "战斗";
        if(category == HackCategory.AUTO) name = "自动化";
        if(category == HackCategory.ANOTHER) name = "其他";

        RenderUtils.drawCenterStringWithRect("\u00a77:)\u00a7f" + name, 4, 4, 80, 10,
                ClickGui.getColor(), ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F), ClickGui.getColor(), 0);

        TConut = 0;
        THack = null;
        int y = 18;

        for(final Hack hack : HackManager.getHacks()){
            if(hack.getCategory() == category){
                TConut++;

                int x = 4, w = 80, h = 10;
                int type = 0;
                int BColor = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.0F);
                int BgColor = ColorUtils.color(1F, 1F, 1F, 0.4F);
                int FColor = ColorUtils.color(0F, 0F, 0F, 0F);

                if (hack.isToggled()){
                    BColor = ClickGui.getColor();
                    FColor = BColor;
                    type = 1;
                }
                if (TConut == TSelect)
                {
                    BColor = ClickGui.getColor();
                    FColor = ColorUtils.color(1F, 1F, 1F, 1F);
                    BgColor = ColorUtils.color(0.0F, 0.0F, 0.0F, 0.4F);
                    x += 1;y += 1;w -= 2;h -= 1;
                    THack = hack;
                }

                RenderUtils.drawCenterStringWithRect(hack.GUIName, x, y, w, h,
                        FColor,
                        BColor,
                        BgColor, type);
                y += 14;
            }
        }
    }

    public static void toggle(){
        if(THack != null && !isFash){
            THack.toggle();
        }
    }

    public static void upItem(){
        if (!isFash){
            TSelect -= 1;
            if(TSelect <= 0){
                TSelect = TConut;
            }
        }
    }

    public static void downItem(){
        if (!isFash) {
            TSelect += 1;
            if (TSelect > TConut) {
                TSelect = 1;
            }
        }
    }

    public static void ChangeCategory(){
        TSelect = 0;
        if(category == HackCategory.PLAYER) {
            category = HackCategory.VISUAL;
        }else if(category == HackCategory.VISUAL) {
            category = HackCategory.COMBAT;
        }else if(category == HackCategory.COMBAT) {
            category = HackCategory.AUTO;
        }else if(category == HackCategory.AUTO) {
            category = HackCategory.ANOTHER;
        }else if(category == HackCategory.ANOTHER){
            category = HackCategory.PLAYER;
        }
        //isFash = true;
        FastGuiDraw();
    }

    public static void onKeyPressed(int key){
        if (key == Keyboard.KEY_LEFT) {
            FastGuiManager.ChangeCategory();
        }else if(key == Keyboard.KEY_UP){
            FastGuiManager.upItem();
        }else if(key == Keyboard.KEY_DOWN){
            FastGuiManager.downItem();
        }else if(key == Keyboard.KEY_RIGHT){
            FastGuiManager.toggle();
        }
    }
}
