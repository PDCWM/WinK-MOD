package i.WinKcode.gui.click.theme.dark;

import i.WinKcode.gui.click.base.Component;
import i.WinKcode.gui.click.base.ComponentRenderer;
import i.WinKcode.gui.click.base.ComponentType;
import i.WinKcode.gui.click.elements.Dropdown;
import i.WinKcode.gui.click.theme.Theme;
import i.WinKcode.hack.hacks.visual.ClickGui;


public class DarkDropDown extends ComponentRenderer {

    public DarkDropDown(Theme theme) {

        super(ComponentType.DROPDOWN, theme);
    }

    @Override
    public void drawComponent(Component component, int mouseX, int mouseY) {

        Dropdown dropdown = (Dropdown) component;
        String text = dropdown.getText();

        theme.fontRenderer.drawString(text, dropdown.getX() + 5, dropdown.getY() + (dropdown.getDropdownHeight() / 2 - theme.fontRenderer.FONT_HEIGHT / 4), 
        		ClickGui.getColor());

        if (dropdown.isMaximized()) {
            dropdown.renderChildren(mouseX, mouseY);
        }
    }

    @Override
    public void doInteractions(Component component, int mouseX, int mouseY) {

    }
}
