package ee.mattijagula.mikker.ui;

import ee.mattijagula.mikker.Configuration;
import ee.mattijagula.mikker.recorder.SoundRecorder;

import javax.swing.*;

/**
 * Minimal swing window setup for testing the applet without launching it in a browser.
 */
public class MainWindow extends JFrame {
    public MainWindow(Configuration cfg) {
        super("Web Sound Recorder");

        SoundRecorder recorder = new SoundRecorder(cfg.getRecordingFilename());
        getContentPane().add(new RecorderPanel(cfg, recorder));
        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        System.out.println("dimensions: " + getWidth() + ":" + getHeight());

        recorder.initializeMicrophone();
    }
}
