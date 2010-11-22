package ee.webmedia.mikker.recorder;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class SoundRecorder implements Recorder {
    private InMemoryAudioStream out;
    private Thread recordingThread;
    private TargetDataLine line;

    private AudioAnalyzer analyzer = null;

    private final List<RecordingListener> listeners = new ArrayList<RecordingListener>();
    private volatile boolean stopPlayback;

    private long start;

    public void startRecording(AudioLevelListener levelListener) {
        System.out.println("start");

        try {
            start = System.currentTimeMillis();

            AudioFormat format = audioFormat();

            analyzer = new AudioAnalyzer(format, levelListener);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);

            line.start();
            out = new InMemoryAudioStream();

            this.recordingThread = new RecordingThread();
            this.recordingThread.start();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AudioFormat audioFormat() {
        int sampleSize = 16;
        int channels = 1;
        float rate = 44100f;

        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, sampleSize, channels,
                (sampleSize / 8) * channels, rate, true);
    }

    public void stopRecording() {
        try {
            line.stop();
            line.close();
            recordingThread.join();
            recordingThread = null;

            out.close();
            System.out.println("stopped. duration: " + (System.currentTimeMillis() - start));
            System.out.println("length: " + out.length() + ", size: " + out.bytes().length);
            System.out.println("memory: " + Runtime.getRuntime().freeMemory());
            notifyListeners(RecordingEvent.recordingFinished());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopPlaying() {
        stopPlayback = true;
    }

    public void startPlaying() {
        System.out.println("replay");
        new ReplayThread().start();
        
    }

    public void deleteRecording() {
        this.out = null;
        notifyListeners(RecordingEvent.recordingDeleted());
    }

    public void addListener(RecordingListener deleteButton) {
        listeners.add(deleteButton);
    }

    private void notifyListeners(RecordingEvent event) {
        for (RecordingListener l : listeners) {
            l.onRecordingEvent(event);
        }
    }

    public void writeResult(OutputStream out) throws IOException {
        out.write(this.out.bytes());
    }

    private class ReplayThread extends Thread {
        public void run() {
            try {
                AudioInputStream audioInputStream = new AudioInputStream(out.toInputStream(), audioFormat(), out.length());
                AudioFormat audioFormat = audioInputStream.getFormat();

                DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

                SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);

                int bytesPerOneSecond = (int) (audioFormat.getFrameRate() * audioFormat.getFrameSize());
                byte tempBuffer[] = new byte[bytesPerOneSecond];
                
                int cnt;
                stopPlayback = false;
                boolean started = false;


                do {
                    cnt = audioInputStream.read(tempBuffer,0,tempBuffer.length);
                    if(cnt > 0){
                        sourceDataLine.write(tempBuffer, 0, cnt);
                        if (!started) {
                            sourceDataLine.open(audioFormat);
                            sourceDataLine.start();
                            started = true;
                        }
                    }
                } while(cnt != -1 && !stopPlayback);

                if (stopPlayback) {
                    sourceDataLine.stop();
                    sourceDataLine.flush();
                } else {
                    sourceDataLine.drain();
                }
                sourceDataLine.close();

                System.out.println("... finished");

                stopPlayback = false;
                notifyListeners(RecordingEvent.playbackFinished());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class RecordingThread extends Thread {
        public void run() {
            try {
                System.out.println("writing ...");
                // AudioSystem.write(new AudioInputStream(line), AudioFileFormat.Type.AU, out);
                int frameSizeInBytes = audioFormat().getFrameSize();
                int bufferLengthInFrames = line.getBufferSize() / 8;
                int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
                byte[] data = new byte[bufferLengthInBytes];
                int numBytesRead;

                while (line.isOpen()) {
                    if((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                        break;
                    }
                    out.write(data, 0, numBytesRead);

                    analyzer.analyze(data, numBytesRead);
                }
                System.out.println("... done writing");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("thread exiting");
        }
    }

}
