package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.Recorder;
import ee.webmedia.mikker.events.RecordingEvent;
import ee.webmedia.mikker.events.RecordingListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecordButton extends JButton implements RecordingListener {
    private Recorder recorder;

    private boolean recording = false;
    private boolean playing = false;

    private ActionListener current = null;

    public RecordButton(Recorder recorder) {
        super();
        this.recorder = recorder;

        resetRecordButton();
    }

    public void onRecordingEvent(RecordingEvent event) {
        if (!event.isRecordingAvailable()) {
            resetRecordButton();
        }
        if (playing && !event.isPlaying()) {
            resetPlayButton();
        }
    }

    private void resetPlayButton() {
        playing = false;
        setIcon(new Icons().getPlayIcon());
    }

    private void resetRecordButton() {
        recording = false;
        playing = false;
        setIcon(new Icons().getRecordIcon());
        replaceActionListener(new RecordingActionListener());
    }

    private void replaceActionListener(ActionListener listener) {
        if (current != null)
            removeActionListener(current);
        current = listener;
        addActionListener(current);
    }

    private class RecordingActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!recording) {
                recorder.startRecording();
                recording = true;
                setIcon(new Icons().getStopIcon());
            } else {
                recorder.stopRecording();
                recording = false;
                setIcon(new Icons().getPlayIcon());
                replaceActionListener(new ReplayingActionListener());
            }
        }
    }

    private class ReplayingActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (playing) {
                recorder.stopPlaying();
            } else {
                recorder.startPlaying();
                setIcon(new Icons().getStopIcon());
                playing = true;
            }
        }
    }
}
