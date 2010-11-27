package ee.webmedia.mikker.ui;

import ee.webmedia.mikker.recorder.Recorder;
import ee.webmedia.mikker.recorder.RecordingEvent;
import ee.webmedia.mikker.recorder.RecordingListener;
import ee.webmedia.mikker.upload.Uploader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class SaveButton extends JButton implements ActionListener, RecordingListener {
    private final ImageIcon save = new Icons().getSaveIcon();
    private final ImageIcon ok = new Icons().getOkIcon();

    private final Recorder recorder;
    private final Uploader uploader;
    private UploadListener listener;

    public SaveButton(Recorder soundRecorder, Uploader uploader, UploadListener listener) {
        setIcon(save);

        this.recorder = soundRecorder;
        this.uploader = uploader;
        this.listener = listener;

        setToolTipText("Upload the recorded clip");
        setEnabled(false);

        addActionListener(this);
    }

    private Icon overlayIcon() {
        return new Icon() {
            public void paintIcon(Component component, Graphics graphics, int x, int y) {
                save.paintIcon(component, graphics, x, y);
                ok.paintIcon(component, graphics, x, y + (save.getIconHeight() - ok.getIconHeight()));
            }

            public int getIconWidth() {
                return save.getIconWidth();
            }

            public int getIconHeight() {
                return save.getIconHeight();
            }
        };
    }

    public void actionPerformed(ActionEvent actionEvent) {
        try {
            uploader.upload(recorder.getRecording());
            setIcon(overlayIcon());
            setEnabled(false);

            listener.onUploadCompleted();
            
            System.out.println("Uploaded file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onRecordingEvent(RecordingEvent event) {
        setEnabled(event.isRecordingAvailable());
        
        if (!event.isRecordingAvailable()) {
            setIcon(save);
        }
    }

    public interface UploadListener {
        void onUploadCompleted();
    }
}
