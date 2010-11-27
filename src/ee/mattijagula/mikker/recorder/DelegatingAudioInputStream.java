package ee.mattijagula.mikker.recorder;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;

/**
 * Purely delegating audio stream in order to simplify partially overriding the functionality.
 */
class DelegatingAudioInputStream extends AudioInputStream {
    private AudioInputStream actual;

    public DelegatingAudioInputStream(AudioInputStream actual, TargetDataLine targetDataLine) {
        super(targetDataLine);
        this.actual = actual;
    }
    @Override
    public AudioFormat getFormat() {
        return actual.getFormat();
    }

    @Override
    public long getFrameLength() {
        return actual.getFrameLength();
    }

    @Override
    public int read() throws IOException {
        return actual.read();
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return actual.read(bytes);
    }

    @Override
    public int read(byte[] bytes, int i, int i1) throws IOException {
        return actual.read(bytes, i, i1);
    }

    @Override
    public long skip(long l) throws IOException {
        return actual.skip(l);
    }

    @Override
    public int available() throws IOException {
        return actual.available();
    }

    @Override
    public void close() throws IOException {
        actual.close();
    }

    @Override
    public void mark(int i) {
        actual.mark(i);
    }

    @Override
    public void reset() throws IOException {
        actual.reset();
    }

    @Override
    public boolean markSupported() {
        return actual.markSupported();
    }
}
