package ee.webmedia.mikker.events;

public class RecordingEvent {
    private boolean recorded;
    private boolean playing;

    public RecordingEvent(boolean recordingAvailable, boolean playing) {
        this.recorded = recordingAvailable;
        this.playing = playing;
    }

    public boolean isRecordingAvailable() {
        return this.recorded;
    }

    public boolean isPlaying() {
        return playing;
    }
}
