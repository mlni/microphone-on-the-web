package ee.webmedia.mikker;

import java.io.IOException;
import java.io.OutputStream;

public interface Recorder {
    void startRecording();
    void stopRecording();

    void startPlaying();
    void stopPlaying();

    void deleteRecording();

    void writeResult(OutputStream out) throws IOException;
}
