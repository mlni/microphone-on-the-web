package ee.mattijagula.mikker.recorder;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class InMemoryAudioStream extends OutputStream {
    private final ByteArrayOutputStream byteStream;
    private final ZipOutputStream zipStream;
    private long length = 0;

    public InMemoryAudioStream(String filename) throws IOException {
        byteStream = new ByteArrayOutputStream();
        zipStream = new ZipOutputStream(byteStream);
        zipStream.putNextEntry(new ZipEntry(filename));
    }

    @Override
    public void write(int i) throws IOException {
        zipStream.write(i);
        length++;
    }

    public void close() throws IOException {
        zipStream.close();
        byteStream.close();
    }

    @Override
    public void write(byte[] bytes, int offset, int len) throws IOException {
        length += len;
        zipStream.write(bytes, offset, len);
    }

    public InputStream toInputStream() throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        zipInputStream.getNextEntry();
        return zipInputStream;
    }

    public byte[] bytes() {
        return byteStream.toByteArray();
    }

    public long length() {
        return length;
    }
}
