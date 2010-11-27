package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.recorder.SoundRecorder;
import ee.webmedia.mikker.upload.Configuration;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("Web Sound Recorder");

        SoundRecorder recorder = new SoundRecorder("filename");
        Configuration cfg = new Configuration("http://localhost:9999/record/upload", "file", "tund1_sepapoisid");
        getContentPane().add(new RecorderPanel(cfg, recorder));
        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        System.out.println("dimensions: " + getWidth() + ":" + getHeight());

        recorder.initializeMicrophone();
    }
}
