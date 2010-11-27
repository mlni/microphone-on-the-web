package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.recorder.RecordingEvent;
import ee.webmedia.mikker.recorder.RecordingListener;
import ee.webmedia.mikker.upload.Configuration;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FilenameField extends JTextField implements RecordingListener {
    private Configuration ctx;

    public FilenameField(final Configuration conf) {
        this.ctx = conf;
        this.setText(conf.getFilename());
        this.getDocument().addDocumentListener(new OnChangeListener(conf));
        this.setEnabled(false);
    }

    public void onRecordingEvent(RecordingEvent event) {
        setEnabled(event.isRecordingAvailable());
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
