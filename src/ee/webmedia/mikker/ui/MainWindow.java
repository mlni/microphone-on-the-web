package ee.webmedia.mikker.ui;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("Web Sound Recorder");

        add(new RecorderPanel());
        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        System.out.println("dimensions: " + getWidth() + ":" + getHeight());
    }
}
