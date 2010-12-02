package ee.mattijagula.mikker.ui;

import javax.swing.*;
import java.awt.*;

public class LevelDisplayingIcon implements Icon {
    private final ImageIcon icon;
    private volatile int currentLevel = 0;
    private Color overlayColor;

    public LevelDisplayingIcon(ImageIcon icon, Color overlayColor) {
        this.icon = icon;
        this.overlayColor = overlayColor;
    }

    public void displayLevel(int percentage) {
        this.currentLevel = percentage;
    }

    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        int iconHeight = icon.getIconHeight();
        int levelHeight = (int) ((currentLevel / 100f) * iconHeight);

        icon.paintIcon(component, graphics, x, y);
        graphics.setColor(overlayColor);
        graphics.fillRect(x, y + (iconHeight - levelHeight), icon.getIconWidth(), levelHeight);
    }

    public int getIconWidth() {
        return icon.getIconWidth();
    }

    public int getIconHeight() {
        return icon.getIconHeight();
    }
}
