package ee.mattijagula.mikker.upload;

import org.apache.commons.httpclient.methods.RequestEntity;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProgressReportingRequestEntity implements RequestEntity {
    private final RequestEntity delegate;
    private final ProgressListener listener;

    public ProgressReportingRequestEntity(RequestEntity actual, ProgressListener listener) {
        delegate = actual;
        this.listener = listener;
    }

    public boolean isRepeatable() {
        return delegate.isRepeatable();
    }

    public void writeRequest(OutputStream outputStream) throws IOException {
        final long len = getContentLength();
        delegate.writeRequest(new CountingOutputStream(outputStream, listener, len));
    }

    public long getContentLength() {
        return delegate.getContentLength();
    }

    public String getContentType() {
        return delegate.getContentType();
    }

    public static class CountingOutputStream extends FilterOutputStream {
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
            this.listener.transferred(this.transferred, this.totalLength);
        }

        public void write(int b) throws IOException {
            out.write(b);
            this.transferred++;
            this.listener.transferred(this.transferred, this.totalLength);
        }
    }}
