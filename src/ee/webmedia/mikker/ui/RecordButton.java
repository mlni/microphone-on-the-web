package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.AudioLevelListener;
import ee.webmedia.mikker.Recorder;
import ee.webmedia.mikker.events.RecordingEvent;
import ee.webmedia.mikker.events.RecordingListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecordButton extends JButton implements RecordingListener {
    private Recorder recorder;

    private boolean recording = false;
    private boolean playing = false;

    private ActionListener current = null;

    private volatile int level = 0;

    public RecordButton(Recorder recorder) {
        super();
        this.recorder = recorder;

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

    private class RecordingActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            if (!recording) {
                recorder.startRecording(new AudioLevelListener() {
                    public void onLevelChange(int level) {
                        System.out.println("level: " + level);
                        RecordButton.this.level = level;
                        RecordButton.this.repaint();
                    }
                });
                recording = true;
                setIcon(new LevelDisplayingIcon(new Icons().getStopIcon()));
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

    private class LevelDisplayingIcon implements Icon {
        private final ImageIcon icon;

        public LevelDisplayingIcon(ImageIcon icon) {
            this.icon = icon;
        }

        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            int iconHeight = icon.getIconHeight();
            int levelHeight = (int) ((level / 100f) * iconHeight);

            icon.paintIcon(component, graphics, x, y);

            graphics.setColor(new Color(0xaa, 0, 0, 127));

            graphics.fillRect(x, y + (iconHeight - levelHeight), icon.getIconWidth(), levelHeight);
        }

        public int getIconWidth() {
            return icon.getIconWidth();
        }

        public int getIconHeight() {
            return icon.getIconHeight();
        }
    }
}
