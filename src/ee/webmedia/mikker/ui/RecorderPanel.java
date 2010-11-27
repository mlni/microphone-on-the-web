package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.recorder.SoundRecorder;
import ee.webmedia.mikker.upload.RequestContext;
import ee.webmedia.mikker.upload.Uploader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecorderPanel extends JPanel {
    private final int MAX_RECORDING_DURATION = 2 * 60 * 1000;

    public RecorderPanel(RequestContext ctx) {
        setBackground(Color.WHITE);

        final SoundRecorder soundRecorder = new SoundRecorder(ctx.getFilename());
        setLayout(new FlowLayout());

        RecordButton recordButton = new RecordButton(soundRecorder, MAX_RECORDING_DURATION);
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


        Uploader uploader = new Uploader(ctx);

        SaveButton saveButton = new SaveButton(soundRecorder, uploader, ctx.getFilename());
        soundRecorder.addListener(saveButton);
        add(saveButton);
    }
}
