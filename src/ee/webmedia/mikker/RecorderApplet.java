package ee.webmedia.mikker;

import ee.webmedia.mikker.ui.RecorderPanel;

import javax.swing.*;

public class RecorderApplet extends JApplet {
    public void init() {
        add(new RecorderPanel());

        System.out.println("memory: " + Runtime.getRuntime().maxMemory());
    }
}
