package ee.webmedia.mikker;

import javax.sound.sampled.AudioFormat;

public class AudioAnalyzer {
    private AudioFormat format;

    private int lastMin = Integer.MAX_VALUE;
    private int lastMax = Integer.MIN_VALUE;

    private int max = Integer.MIN_VALUE;
    private int min = Integer.MAX_VALUE;

    public AudioAnalyzer(AudioFormat format) {
        this.format = format;
    }

    public void analyze(byte[] audioBytes, int end) {
        int[] audioData = null;

        if (format.getSampleSizeInBits() == 16) {
             int lengthInSamples = end / 2;
             audioData = new int[lengthInSamples];
             if (format.isBigEndian()) {
                for (int i = 0; i < lengthInSamples; i++) {
                     /* First byte is MSB (high order) */
                     int MSB = (int) audioBytes[2*i];
                     /* Second byte is LSB (low order) */
                     int LSB = (int) audioBytes[2*i+1];
                     audioData[i] = MSB << 8 | (255 & LSB);
                 }
             } else {
                 for (int i = 0; i < lengthInSamples; i++) {
                     /* First byte is LSB (low order) */
                     int LSB = (int) audioBytes[2*i];
                     /* Second byte is MSB (high order) */
                     int MSB = (int) audioBytes[2*i+1];
                     audioData[i] = MSB << 8 | (255 & LSB);
                 }
             }
         } else if (format.getSampleSizeInBits() == 8) {
             audioData = new int[end];
             if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
                 for (int i = 0; i < end; i++) {
                     audioData[i] = audioBytes[i];
                 }
             } else {
                 for (int i = 0; i < end; i++) {
                     audioData[i] = audioBytes[i] - 128;
                 }
             }
        } else {
            return;
        }

        lastMin = Integer.MAX_VALUE;
        lastMax = Integer.MIN_VALUE;

        for (int anAudioData : audioData) {
            lastMin = Math.min(lastMin, anAudioData);
            lastMax = Math.max(lastMax, anAudioData);
        }

        max = Math.max(max, lastMax);
        min = Math.min(min, lastMin);
    }
}
