package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.SoundRecorder;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        super("Web Sound Recorder");

        setBackground(Color.WHITE);
        
        final SoundRecorder soundRecorder = new SoundRecorder();
        setLayout(new FlowLayout());

        RecordButton recordButton = new RecordButton(soundRecorder);
        soundRecorder.addListener(recordButton);
        add(recordButton);

        DeleteButton deleteButton = new DeleteButton(soundRecorder);
        deleteButton.setToolTipText("Clear the recorded clip");
        soundRecorder.addListener(deleteButton);
        add(deleteButton);

        SaveButton saveButton = new SaveButton(soundRecorder);
        saveButton.setToolTipText("Upload the recorded clip");
        soundRecorder.addListener(saveButton);
        add(saveButton);

        pack();

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        System.out.println("dimensions: " + getWidth() + ":" + getHeight());
    }
}
