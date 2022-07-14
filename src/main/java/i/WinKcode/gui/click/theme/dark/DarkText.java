package i.WinKcode.gui.click.theme.dark;

import i.WinKcode.gui.click.base.Component;
import i.WinKcode.gui.click.base.ComponentRenderer;
import i.WinKcode.gui.click.base.ComponentType;
import i.WinKcode.gui.click.elements.Text;
import i.WinKcode.gui.click.theme.Theme;


public class DarkText extends ComponentRenderer {

    public DarkText(Theme theme) {
        super(ComponentType.TEXT, theme);
    }

    @Override
    public void drawComponent(Component component, int mouseX, int mouseY) {
        Text text = (Text) component;
        String[] message = text.getMessage();

        int y = text.getY();

        for (String s : message) {
            theme.fontRenderer.drawString(s, text.getX() - 4, y - 4, -1);
            y += 10;
        }
    }

    @Override
    public void doInteractions(Component component, int mouseX, int mouseY) {}
}
