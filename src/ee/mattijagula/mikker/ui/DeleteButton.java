package ee.mattijagula.mikker.ui;

import ee.mattijagula.mikker.recorder.RecordingEvent;
import ee.mattijagula.mikker.recorder.RecordingListener;

import javax.swing.*;

public class DeleteButton extends JButton implements RecordingListener {
    public DeleteButton() {
        super(new Icons().getDeleteIcon());

        setToolTipText("Clear the recorded clip");
        setEnabled(false);
    }
    
    public void onRecordingEvent(RecordingEvent event) {
        setEnabled(event.isRecordingAvailable());
    }
}