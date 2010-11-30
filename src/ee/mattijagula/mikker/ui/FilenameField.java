package ee.mattijagula.mikker.ui;

import ee.mattijagula.mikker.Configuration;
import ee.mattijagula.mikker.recorder.RecordingEvent;
import ee.mattijagula.mikker.recorder.RecordingListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FilenameField extends JTextField implements RecordingListener, SaveButton.UploadListener {
    private Configuration ctx;

    public FilenameField(final Configuration conf) {
        this.ctx = conf;
        this.setText(conf.getFilename());
        this.getDocument().addDocumentListener(new OnChangeListener(conf));
        this.setEnabled(false);
    }

    public void onRecordingEvent(RecordingEvent event) {
        if (event.isNewRecordingFinished())
            setEnabled(true);
        if (!event.isRecordingAvailable())
            setEnabled(false);
    }

    public void onUploadStarted() {
        setEnabled(false);
    }

    private class OnChangeListener implements DocumentListener {
        private final Configuration conf;

        public OnChangeListener(Configuration conf) {
            this.conf = conf;
        }

        public void insertUpdate(DocumentEvent documentEvent) {
            conf.updateFilename(getText());
        }

        public void removeUpdate(DocumentEvent documentEvent) {
        }

        public void changedUpdate(DocumentEvent documentEvent) {
        }
    }
}
