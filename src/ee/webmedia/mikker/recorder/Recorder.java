package ee.webmedia.mikker.recorder;

public interface Recorder {
    void startRecording(AudioLevelListener listener);
    void stopRecording();

    void startPlaying();
    void stopPlaying();

    void deleteRecording();

    byte[] getRecording();
}
