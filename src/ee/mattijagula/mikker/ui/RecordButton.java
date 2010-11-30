package ee.mattijagula.mikker.ui;

import ee.mattijagula.mikker.recorder.AudioLevelListener;
import ee.mattijagula.mikker.recorder.Recorder;
import ee.mattijagula.mikker.recorder.RecordingEvent;
import ee.mattijagula.mikker.recorder.RecordingListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecordButton extends JButton implements RecordingListener {
    private static final Color LEVEL_COLOR = new Color(0xaa, 0, 0, 127);
    
    private final Recorder recorder;

    private final int maxDuration;
    private boolean recording = false;

    private boolean playing = false;

    private Timer timer;

    private ActionListener current = null;
    private final LevelDisplayingIcon levelDisplayingIcon;

    public RecordButton(Recorder recorder, int maxDuration) {
        super();
        this.recorder = recorder;
        this.maxDuration = maxDuration;

        this.levelDisplayingIcon = new LevelDisplayingIcon(new Icons().getStopIcon(), LEVEL_COLOR);

        setEnabled(false);

        resetRecordButton();
    }

    public void onRecordingEvent(RecordingEvent event) {
        if (playing) {
            if (!event.isPlaying()) {
                resetPlayButton();
                playing = false;
            }
            if (!event.isRecordingAvailable()) {
                recorder.stopPlaying();
                playing = false;
            }
        }
        if (!event.isRecordingAvailable()) {
            resetRecordButton();
        }
        setEnabled(true);
    }

    private void resetPlayButton() {
        setIcon(new Icons().getPlayIcon());
        setToolTipText("Replay the recorded clip");
    }

    private void resetRecordButton() {
        recording = false;
        playing = false;

        setIcon(new Icons().getRecordIcon());
        setToolTipText("Record a new clip");

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
                        displayLevel(level);
                    }
                });
                recording = true;
                setIcon(levelDisplayingIcon);
                
                timer = new Timer(maxDuration, this);
                timer.setRepeats(false);
                timer.start();
            } else {
                recorder.stopRecording();
                recording = false;
                resetPlayButton();
                replaceActionListener(new ReplayingActionListener());

                if (timer != null) {
                    timer.stop();
                    timer = null;
                }
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
