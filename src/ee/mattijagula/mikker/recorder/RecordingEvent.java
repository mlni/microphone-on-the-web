package ee.mattijagula.mikker.recorder;

/**
 * Notify interested parties about events that happened during or after recording.
 */
public class RecordingEvent {
    private final boolean recorded;
    private final boolean playing;
    private final boolean newRecording;

    /* Private, use through factory methods */
    private RecordingEvent(boolean recordingAvailable, boolean playing, boolean newRecordingFinished) {
        this.recorded = recordingAvailable;
        this.playing = playing;
        this.newRecording = newRecordingFinished;
    }

    public static RecordingEvent recordingFinished() {
        return new RecordingEvent(true, false, true);
    }

    public static RecordingEvent playbackFinished() {
        return new RecordingEvent(true, false, false);
    }

    public static RecordingEvent recordingDeleted() {
        return new RecordingEvent(false, false, false);
    }

    public static RecordingEvent microphoneInitialized() {
        return new RecordingEvent(false, false, false);
    }

    public boolean isRecordingAvailable() {
        return this.recorded;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isNewRecordingFinished() {
        return this.newRecording;
    }
    
    public String toString() {
        return "RecordingEvent(" + recorded + "," + playing + ")";
    }
}
