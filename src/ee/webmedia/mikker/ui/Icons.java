package ee.webmedia.mikker.ui;

import javax.swing.*;

public class Icons {
    public ImageIcon getRecordIcon() {
        return new ImageIcon("icons/media-record.png", "Start recording");
    }
    public ImageIcon getStopIcon() {
        return new ImageIcon("icons/media-playback-stop.png", "Stop recording");
    }
    public ImageIcon getPlayIcon() {
        return new ImageIcon("icons/media-playback-start.png", "Replay recording");
    }
    public ImageIcon getDeleteIcon() {
        return new ImageIcon("icons/dialog-delete.png", "Delete");
    }
    public ImageIcon getSaveIcon() {
        return new ImageIcon("icons/document-save-as.png", "Save");
    }
}
