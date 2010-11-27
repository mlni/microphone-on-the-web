package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.recorder.Recorder;
import ee.webmedia.mikker.upload.Configuration;
import ee.webmedia.mikker.upload.Uploader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecorderPanel extends JPanel {
    public RecorderPanel(Configuration ctx, final Recorder soundRecorder) {

        setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel buttonBar = new JPanel();
        buttonBar.setBackground(Color.WHITE);
        
        add(buttonBar, BorderLayout.NORTH);

        FilenameField filename = new FilenameField(ctx);
        soundRecorder.addListener(filename);

        add(filename, BorderLayout.SOUTH);

        RecordButton recordButton = new RecordButton(soundRecorder, ctx.getMaxRecordingDuration());
        soundRecorder.addListener(recordButton);
        buttonBar.add(recordButton);

        DeleteButton deleteButton = new DeleteButton();
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                soundRecorder.deleteRecording();
                RecorderPanel.this.transferFocus();
            }
        });

        soundRecorder.addListener(deleteButton);
        buttonBar.add(deleteButton);

        Uploader uploader = new Uploader(ctx);

        SaveButton saveButton = new SaveButton(soundRecorder, uploader, filename);
        soundRecorder.addListener(saveButton);
        buttonBar.add(saveButton);

    }
}
