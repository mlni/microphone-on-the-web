package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.recorder.Recorder;
import ee.webmedia.mikker.recorder.RecordingEvent;
import ee.webmedia.mikker.recorder.RecordingListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveButton extends JButton implements ActionListener, RecordingListener {
    private final Recorder recorder;

    public SaveButton(Recorder recorder) {
        super(new Icons().getSaveIcon());
        this.recorder = recorder;
        setToolTipText("Upload the recorded clip");
        setEnabled(false);

        addActionListener(this);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        try {
            FileOutputStream out = new FileOutputStream("result.au");
            recorder.writeResult(out);
            System.out.println("Wrote file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRecordingEvent(RecordingEvent event) {
        setEnabled(event.isRecordingAvailable());
    }
}
