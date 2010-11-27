package ee.mattijagula.mikker.exploration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MemoryTest {
    public static void main(String[] args) throws Exception {
        System.out.println("Available memory: " + Runtime.getRuntime().maxMemory());
        new MemoryTest().runByteTests();
        new MemoryTest().runZipTests();
    }

    private void runZipTests() throws Exception {
        for (int i=0; i<5; i++) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zout = new ZipOutputStream(bos);
            zout.putNextEntry(new ZipEntry("out.au"));

            long maxSize = bytesUntilOOM(zout);
            System.out.println("zip: " + maxSize);
        }
    }
    private void runByteTests() throws Exception {
        for (int i=0; i<5; i++) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            long maxSize = bytesUntilOOM(bos);
            System.out.println("byte array: " + maxSize);
        }
    }

    private long bytesUntilOOM(OutputStream out) {
        long count = 0;
        try {
            while (true) {
                byte buf[] = new byte[1024];
                for (int i=0; i<buf.length; i++) {
                    buf[i] = (byte) (i % 10);
                }
                out.write(buf);
                out.flush();
                
                count += buf.length;

                if (count > (67108864L * 10)) {
                    return count;
                }
            }
        } catch (OutOfMemoryError oom) {
            return count;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
