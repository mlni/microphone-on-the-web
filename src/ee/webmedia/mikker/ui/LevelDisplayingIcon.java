package ee.webmedia.mikker.ui;

import javax.swing.*;
import java.awt.*;

public class LevelDisplayingIcon implements Icon {
    private final ImageIcon icon;
    private int currentLevel = 0;

    public LevelDisplayingIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public void displayLevel(int level) {
        this.currentLevel = level;
    }

    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        int iconHeight = icon.getIconHeight();
        int levelHeight = (int) ((currentLevel / 100f) * iconHeight);

        icon.paintIcon(component, graphics, x, y);

        graphics.setColor(new Color(0xaa, 0, 0, 127));

        graphics.fillRect(x, y + (iconHeight - levelHeight), icon.getIconWidth(), levelHeight);
    }

    public int getIconWidth() {
        return icon.getIconWidth();
    }

    public int getIconHeight() {
        return icon.getIconHeight();
    }
}
