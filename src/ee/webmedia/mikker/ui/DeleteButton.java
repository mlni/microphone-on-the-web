package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.events.RecordingEvent;
import ee.webmedia.mikker.events.RecordingListener;

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