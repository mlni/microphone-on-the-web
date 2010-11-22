package ee.webmedia.mikker.recorder;

import java.io.IOException;
import java.io.OutputStream;

public interface Recorder {
    void startRecording(AudioLevelListener listener);
    void stopRecording();

    void startPlaying();
    void stopPlaying();

    void deleteRecording();

    void writeResult(OutputStream out) throws IOException;
}
