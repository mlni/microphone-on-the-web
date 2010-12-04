package ee.mattijagula.mikker.upload;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An outputstream wrapper that keeps track of the current progress of writing to the
 * stream and reports it to a ProgressListener.
 */
public class CountingOutputStream extends FilterOutputStream {
    private final ProgressListener listener;
    private final long totalLength;
    private long transferred;

    public CountingOutputStream(final OutputStream out,
                                final ProgressListener listener, long len) {
        super(out);
        this.listener = listener;
        this.transferred = 0;
        this.totalLength = len;
    }

    public void write(byte[] b, int off, int len) throws IOException {
        out.write(b, off, len);
        this.transferred += len;
        this.listener.transferred(Math.min(this.transferred, this.totalLength), this.totalLength);
    }

    public void write(int b) throws IOException {
        out.write(b);
        this.transferred++;
        this.listener.transferred(Math.min(this.transferred, this.totalLength), this.totalLength);
    }
}
