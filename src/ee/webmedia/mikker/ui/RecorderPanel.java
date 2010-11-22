package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.SoundRecorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecorderPanel extends JPanel {
    public RecorderPanel() {
        setBackground(Color.WHITE);

        final SoundRecorder soundRecorder = new SoundRecorder();
        setLayout(new FlowLayout());

        RecordButton recordButton = new RecordButton(soundRecorder);
        soundRecorder.addListener(recordButton);
        add(recordButton);

        DeleteButton deleteButton = new DeleteButton();
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                soundRecorder.deleteRecording();
                RecorderPanel.this.transferFocus();
            }
        });

        soundRecorder.addListener(deleteButton);
        add(deleteButton);

        SaveButton saveButton = new SaveButton(soundRecorder);
        soundRecorder.addListener(saveButton);
        add(saveButton);
    }
}
