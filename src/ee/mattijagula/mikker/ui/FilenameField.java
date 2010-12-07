package ee.mattijagula.mikker.ui;

import ee.mattijagula.mikker.Configuration;
import ee.mattijagula.mikker.recorder.RecordingEvent;
import ee.mattijagula.mikker.recorder.RecordingListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class FilenameField extends JTextField implements RecordingListener, UploadButton.UploadListener {
    private final Configuration ctx;
    private static final int MAX_LENGTH = 100;
    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyABCDEFGHIJKLMNOPQRSTUVWXY0123456789-_+()[],! ";

    public FilenameField(final Configuration conf) {
        this.ctx = conf;
        this.setDocument(new LengthAndCharFilteringDocument());

        this.setText(conf.getFilename());
        this.getDocument().addDocumentListener(new OnChangeListener());
        this.setEnabled(false);
    }

    public static String filterIllegalChars(String str) {
        String result = "";
        for (char c : str.toCharArray()) {
            if (ALLOWED_CHARS.contains(Character.toString(c)))
                result += c;
        }
        return result;
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
        public void insertUpdate(DocumentEvent documentEvent) {
            ctx.updateFilename(getText());
        }

        public void removeUpdate(DocumentEvent documentEvent) {
        }

        public void changedUpdate(DocumentEvent documentEvent) {
        }
    }

    private static class LengthAndCharFilteringDocument extends PlainDocument {
        @Override
        public void insertString(int pos, String str, AttributeSet attributeSet) throws BadLocationException {
            String onlyLegalCharacters = filterIllegalChars(str);
            if (this.getLength() > MAX_LENGTH)
                return;

            int maxAppendLength = Math.min(MAX_LENGTH - getLength(), onlyLegalCharacters.length());
            String cutToMaxFittingSize = onlyLegalCharacters.substring(0, maxAppendLength);
            super.insertString(pos, cutToMaxFittingSize, attributeSet);
        }
    }
}
