package ee.webmedia.mikker.events;

/**
 * Notify interested parties about events that happened during or after recording.
 */
public class RecordingEvent {
    private final boolean recorded;
    private final boolean playing;

    /* Private, use through factory methods */
    private RecordingEvent(boolean recordingAvailable, boolean playing) {
        this.recorded = recordingAvailable;
        this.playing = playing;
    }

    public static RecordingEvent recordingFinished() {
        return new RecordingEvent(true, false);
    }

    public static RecordingEvent playbackFinished() {
        return new RecordingEvent(true, false);
    }

    public static RecordingEvent recordingDeleted() {
        return new RecordingEvent(false, false);
    }

    public boolean isRecordingAvailable() {
        return this.recorded;
    }

    public boolean isPlaying() {
        return playing;
    }
    public String toString() {
        return "RecordingEvent(" + recorded + "," + playing + ")";
    }
}
