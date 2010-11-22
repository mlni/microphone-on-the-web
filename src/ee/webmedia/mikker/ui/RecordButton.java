package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.AudioLevelListener;
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

    private LevelDisplayingIcon levelDisplayingIcon;

    public RecordButton(Recorder recorder) {
        super();
        this.recorder = recorder;
        this.levelDisplayingIcon = new LevelDisplayingIcon(new Icons().getStopIcon());

        resetRecordButton();
    }

    public void onRecordingEvent(RecordingEvent event) {
        if (playing) {
            if (!event.isPlaying()) {
                resetPlayButton();
            }
            if (!event.isRecordingAvailable()) {
                recorder.stopPlaying();
                playing = false;
            }
        }
        if (!event.isRecordingAvailable()) {
            resetRecordButton();
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

    private void displayLevel(int level) {
        levelDisplayingIcon.displayLevel(level);
        repaint();
    }
    
    private class RecordingActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!recording) {
                recorder.startRecording(new AudioLevelListener() {
                    public void onLevelChange(int level) {
                        System.out.println("level: " + level);
                        displayLevel(level);
                    }
                });
                recording = true;
                setIcon(levelDisplayingIcon);
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
