package ee.webmedia.mikker;

import ee.webmedia.mikker.ui.MainWindow;

import javax.sound.sampled.*;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String args[]) throws Exception {
        new MainWindow().setVisible(true);
    }

    private static void foo() throws Exception {
        AudioFormat format = new AudioFormat(8000f, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        System.out.println("Opening mic");

        final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        AudioFileFormat.Type fmt = AudioFileFormat.Type.WAVE;

        System.out.println("Opening line");
        line.open(format);

        System.out.println("Starting line");
        line.start();

        final OutputStream bout = new FileOutputStream("out.zip");
        
        final ZipOutputStream sout = new ZipOutputStream(bout);
        sout.putNextEntry(new ZipEntry("output.au"));

        System.out.println("Starting write");

        Thread thread = new Thread() {
            public void run() {
                try {
                    System.out.println("writing ...");
                    AudioSystem.write(new AudioInputStream(line), AudioFileFormat.Type.AU, sout);
                    System.out.println("... done writing");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("thread exiting");
            }
        };
        thread.start();

        System.out.println("sleeping ...");

        Thread.sleep(30 * 1000);

        System.out.println("Stopping ...");

        line.stop();
        line.close();
        thread.join();

        System.out.println("... joined");

        sout.close();
        bout.close();

        System.out.println("Done");
    }
}
