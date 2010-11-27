package ee.mattijagula.mikker.ui;

import ee.mattijagula.mikker.recorder.SoundRecorder;
import ee.mattijagula.mikker.upload.Configuration;

import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("Web Sound Recorder");

        Configuration cfg = new Configuration("http://localhost/~matti/recorder/upload.php", "fail", "tund1_sepapoisid");
        SoundRecorder recorder = new SoundRecorder(cfg.getRecordingFilename());
        getContentPane().add(new RecorderPanel(cfg, recorder));
        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        System.out.println("dimensions: " + getWidth() + ":" + getHeight());

        recorder.initializeMicrophone();
    }
}
