package ee.mattijagula.mikker.recorder;

import javax.sound.sampled.AudioFormat;

public class AudioAnalyzer {
    private final AudioFormat format;
    private final AudioLevelListener listener;
    private final Converter converter;

    final private int max;
    final private int min;

    public AudioAnalyzer(AudioFormat format, AudioLevelListener listener) {
        this.format = format;
        this.listener = listener;

        if (format.getSampleSizeInBits() == 16) {
            max = 32768;
            min = -32768;
            if (format.isBigEndian())
                converter = new BigEndian16bitConverter();
            else
                converter = new SmallEndian16BitConverter();
        } else {
            if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
                max = 127;
                min = -127;
                converter = new SignedPCM8BitConverter();
            } else {
                max = 255;
                min = 0;
                converter = new UnsignedCM8BitConverter();
            }
        }
    }

    public void analyze(byte[] audioBytes, int offset, int count) {
        int[] audioData = null;
        if (offset == 0) {
            audioData = converter.convertToAudio(audioBytes, count);
        } else {
            byte bytes[] = new byte[(count - offset)];
            System.arraycopy(bytes, 0, audioBytes, offset, count);
            audioData = converter.convertToAudio(bytes, count - offset);
        }

        int lastMin = Integer.MAX_VALUE;
        int lastMax = Integer.MIN_VALUE;

        for (int anAudioData : audioData) {
            lastMin = Math.min(lastMin, anAudioData);
            lastMax = Math.max(lastMax, anAudioData);
        }

        int level = (int) 100.0f * lastMax / max; // FIXME: take min into account
        listener.onLevelChange(level);
    }

    private interface Converter {
        int[] convertToAudio(byte audioBytes[], int length);
    }

    private class BigEndian16bitConverter implements Converter {
        public int[] convertToAudio(byte[] audioBytes, int length) {
            int lengthInSamples = length / 2;
            int[] audioData = new int[lengthInSamples];
            if (format.isBigEndian()) {
               for (int i = 0; i < lengthInSamples; i++) {
                    /* First byte is MSB (high order) */
                    int MSB = (int) audioBytes[2*i];
                    /* Second byte is LSB (low order) */
                    int LSB = (int) audioBytes[2*i+1];
                    audioData[i] = MSB << 8 | (255 & LSB);
                }
            }
            return audioData;
        }
    }
    private class SmallEndian16BitConverter implements Converter {
        public int[] convertToAudio(byte[] audioBytes, int length) {
            int lengthInSamples = length / 2;
            int[] audioData = new int[lengthInSamples];
            for (int i = 0; i < lengthInSamples; i++) {
                /* First byte is LSB (low order) */
                int LSB = (int) audioBytes[2*i];
                /* Second byte is MSB (high order) */
                int MSB = (int) audioBytes[2*i+1];
                audioData[i] = MSB << 8 | (255 & LSB);
            }
            return audioData;
        }
    }
    private class SignedPCM8BitConverter implements Converter {
        public int[] convertToAudio(byte[] audioBytes, int end) {
            int[] audioData = new int[end];
            for (int i = 0; i < end; i++) {
                audioData[i] = audioBytes[i];
            }
            return audioData;
        }
    }
    private class UnsignedCM8BitConverter implements Converter {
        public int[] convertToAudio(byte[] audioBytes, int end) {
            int[] audioData = new int[end];
            for (int i = 0; i < end; i++) {
                audioData[i] = audioBytes[i] - 128;
            }
            return audioData;
        }
    }
}
