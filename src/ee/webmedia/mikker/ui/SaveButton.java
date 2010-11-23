package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.recorder.Recorder;
import ee.webmedia.mikker.recorder.RecordingEvent;
import ee.webmedia.mikker.recorder.RecordingListener;
import ee.webmedia.mikker.recorder.SoundRecorder;
import ee.webmedia.mikker.upload.Uploader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SaveButton extends JButton implements ActionListener, RecordingListener {
    private final Recorder recorder;
    private final Uploader uploader;

    public SaveButton(SoundRecorder soundRecorder, Uploader uploader) {
        super(new Icons().getSaveIcon());

        this.recorder = soundRecorder;
        this.uploader = uploader;

        setToolTipText("Upload the recorded clip");
        setEnabled(false);

        addActionListener(this);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        try {
            uploader.upload("result.zip", "application/zip", recorder.getRecording());
            System.out.println("Uploaded file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRecordingEvent(RecordingEvent event) {
        setEnabled(event.isRecordingAvailable());
    }
}
