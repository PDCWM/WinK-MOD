package i.WinKcode.gui.click.theme.dark;

import i.WinKcode.gui.click.base.Component;
import i.WinKcode.gui.click.base.ComponentRenderer;
import i.WinKcode.gui.click.base.ComponentType;
import i.WinKcode.gui.click.elements.CheckButton;
import i.WinKcode.gui.click.theme.Theme;
import i.WinKcode.hack.hacks.visual.ClickGui;
import i.WinKcode.utils.MathUtils;
import i.WinKcode.utils.visual.ColorUtils;
import i.WinKcode.value.Mode;

public class DarkCheckButton extends ComponentRenderer {

    public DarkCheckButton(Theme theme) {

        super(ComponentType.CHECK_BUTTON, theme);
    }

    @Override
    public void drawComponent(Component component, int mouseX, int mouseY) {

        CheckButton button = (CheckButton) component;
        String text = button.getText();
        
        int mainColor = ClickGui.isLight ? ColorUtils.color(255, 255, 255, 255) : ColorUtils.color(0, 0, 0, 255);
        int mainColorInv = ClickGui.isLight ? ColorUtils.color(0, 0, 0, 255) : ColorUtils.color(255, 255, 255, 255);
        int strColor = ClickGui.isLight ? ColorUtils.color(0.3f, 0.3f, 0.3f, 1.0f) : ColorUtils.color(0.5f, 0.5f, 0.5f, 1.0f);
        
        if(button.getModeValue() == null) {
        	theme.fontRenderer.drawString("> " + text, button.getX() + 5, MathUtils.getMiddle(button.getY(), button.getY() + button.getDimension().height) - theme.fontRenderer.FONT_HEIGHT / 3 - 1, 
        			button.isEnabled() ? mainColorInv : strColor);
        	return;
        }
        
        for(Mode mode : button.getModeValue().getModes()) {
    		if(mode.getName().equals(text)) {
    			theme.fontRenderer.drawString("- " + text, button.getX() + 5, MathUtils.getMiddle(button.getY(), button.getY() + button.getDimension().height) - theme.fontRenderer.FONT_HEIGHT / 3 - 1, 
    					mode.isToggled() ? mainColorInv : strColor);
    		}
        }
    }

    @Override
    public void doInteractions(Component component, int mouseX, int mouseY) {

    }
}
