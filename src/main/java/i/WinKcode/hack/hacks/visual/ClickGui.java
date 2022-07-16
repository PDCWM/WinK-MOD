package i.WinKcode.hack.hacks.visual;

import i.WinKcode.Main;
import i.WinKcode.hack.Hack;
import i.WinKcode.hack.HackCategory;
import i.WinKcode.hack.hacks.another.HackMode;
import i.WinKcode.utils.visual.ColorUtils;
import i.WinKcode.utils.visual.RenderUtils;
import i.WinKcode.value.Mode;
import i.WinKcode.value.types.BooleanValue;
import i.WinKcode.value.types.IntegerValue;
import i.WinKcode.value.types.ModeValue;
import i.WinKcode.wrappers.Wrapper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Text;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import org.lwjgl.input.Keyboard;

public class ClickGui extends Hack{

	public ModeValue theme;
	public static ModeValue language;
	public static BooleanValue rainbow;
	public static BooleanValue shadow;
	public static BooleanValue tooltip;
	
	public static IntegerValue red;
	public static IntegerValue green;
	public static IntegerValue blue;
	public static IntegerValue alpha;
	
	private static int color;
	public static boolean isLight = false;
	
	public ClickGui() {
		super("ClickGui", HackCategory.VISUAL);
		this.GUIName = "图形界面";
		this.setKey(Keyboard.KEY_F12);
		this.setShow(false);
		
		this.theme = new ModeValue("主题", new Mode("黑暗", true), new Mode("明亮", false));
		language = new ModeValue("语言", new Mode("中文", true), new Mode("English", false));
		tooltip = new BooleanValue("提示文本", true);
		shadow = new BooleanValue("阴影", true);
		rainbow = new BooleanValue("彩虹光", true);
		red = new IntegerValue("Red", 255, 0, 255);
		green = new IntegerValue("Green", 255, 0, 255);
		blue = new IntegerValue("Blue", 255, 0, 255);
		alpha = new IntegerValue("Alpha", 255, 0, 255);
		
		this.addValue(theme, language, tooltip, shadow, rainbow, red, green, blue, alpha);
		setColor();
	}
	
	@Override
	public String getDescription() {
		return "图形用户界面.";
	}
	
	 public static int getColor() {
		 return rainbow.getValue() ? ColorUtils.rainbow().getRGB() : color;
	 }
	
	 public static void setColor() {
		color = ColorUtils.color(red.getValue(),
				green.getValue(),
				blue.getValue(),
				alpha.getValue());
	}
	
	@Override
	public void onEnable() {
		if(HackMode.enabled)
			return;
		Wrapper.INSTANCE.mc().displayGuiScreen(Main.hackManager.getGui());
		super.onEnable();
	}

	@Override
	public void onDisable() {

	}
	
	@Override
	public void onClientTick(ClientTickEvent event) {
		setColor();
		isLight = theme.getMode("明亮").isToggled();
		super.onClientTick(event);
	}
	
	@Override
	public void onRenderGameOverlay(Text event) {
		if(shadow.getValue()) {
			ScaledResolution sr = new ScaledResolution(Wrapper.INSTANCE.mc());
			RenderUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), ColorUtils.color(0.0F, 0.0F, 0.0F, 0.5F));
		}
		super.onRenderGameOverlay(event);
	}

}
