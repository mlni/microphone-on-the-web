package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.upload.RequestContext;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("Web Sound Recorder");


        add(new RecorderPanel(new RequestContext("http://localhost:9999/record/upload", "file", "audio-file.au")));
        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        System.out.println("dimensions: " + getWidth() + ":" + getHeight());
    }
}
