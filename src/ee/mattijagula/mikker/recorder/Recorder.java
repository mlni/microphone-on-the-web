package ee.mattijagula.mikker.recorder;

public interface Recorder {
    void startRecording(AudioLevelListener listener);
    void stopRecording();

    void startPlaying();
    void stopPlaying();

    void deleteRecording();

    byte[] getRecording();

    void addListener(RecordingListener recordButton);
}
