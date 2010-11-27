package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.upload.Configuration;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("Web Sound Recorder");


        getContentPane().add(new RecorderPanel(new Configuration("http://localhost:9999/record/upload", "file", "tund1_sepapoisid")));
        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        System.out.println("dimensions: " + getWidth() + ":" + getHeight());
    }
}
