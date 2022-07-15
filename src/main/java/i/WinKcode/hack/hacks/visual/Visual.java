package i.WinKcode.hack.hacks.visual;

import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.value.types.IntegerValue;
import i.WinKcode.wrappers.Wrapper;

public class Visual extends Hack {
    public IntegerValue fovD;

    public Visual() {
        super("visual", HackCategory.VISUAL);
        this.GUIName = "视角调整";

        this.fovD = new IntegerValue("广角", 70, 50, 160);

        this.addValue(fovD);
    }

    @Override
    public String getDescription() {
        return "调整角色视角参数.";
    }

    private float lastFov = 70;
    @Override
    public void onEnable(){
        //ChatUtils.message(Wrapper.INSTANCE.player().getFovModifier());
        lastFov = Wrapper.INSTANCE.mc().gameSettings.fovSetting;
        Wrapper.INSTANCE.mc().gameSettings.fovSetting = fovD.getValue();
    }

    @Override
    public void onDisable() {
        Wrapper.INSTANCE.mc().gameSettings.fovSetting = lastFov;
    }
}
