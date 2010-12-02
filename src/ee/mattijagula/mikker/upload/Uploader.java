package ee.mattijagula.mikker.upload;

import ee.mattijagula.mikker.Configuration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Uploader {
    private Configuration ctx;

    public Uploader(Configuration ctx) {
        this.ctx = ctx;
    }

    public void upload(byte content[], ProgressListener progressListener) throws UploadFailedException {
        try {
            doUpload(content, progressListener);
        } catch (Throwable e) {
            throw new UploadFailedException("Upload failed with exception: " + e.getMessage(), e);
        }
    }

    private void doUpload(final byte content[], ProgressListener progressListener)
            throws IOException, UploadFailedException {
        HttpClient client = new HttpClient();

        PostMethod post = new PostMethod(ctx.getUploadUrl());
        try {
            post.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DontRetryHandler());

            post.addRequestHeader("User-Agent", ctx.getUserAgent());

            if (!"".equals(ctx.getCookies())) {
                post.addRequestHeader("Cookie", ctx.getCookies());
            }

            PartSource partSource = new PartSource() {
                public long getLength() {
                    return content.length;
                }
                public String getFileName() {
                    return ctx.getUploadFilename();
                }
                public InputStream createInputStream() throws IOException {
                    return new ByteArrayInputStream(content);
                }
            };

            Part[] partArray = composeParts(partSource);

            MultipartRequestEntity requestEntity = new MultipartRequestEntity(partArray, post.getParams());
            post.setRequestEntity(new ProgressReportingRequestEntity(requestEntity, progressListener));

            int status = client.executeMethod(post);
            if (status >= 200 && status < 400) {
                progressListener.finished();
                System.out.println("Upload complete, response: " + status);
                consumeResponse(post.getResponseBodyAsStream());
                System.out.println("consumed");
            } else {
                throw new UploadFailedException("Upload failed with code " + status
                        + " : " + HttpStatus.getStatusText(status));
            }
        } finally {
            post.releaseConnection();
        }
    }

    private void consumeResponse(InputStream response) {
        try {
            byte buffer[] = new byte[1024];
            while (response.read(buffer) > 0) {
                // do nothing
            }
        } catch (IOException e) {
            // ignore, we're not interested in the result
        }
    }

    private Part[] composeParts(PartSource partSource) {
        List<Part> parts = new ArrayList<Part>();
        
        parts.add(new FilePart(ctx.getFileFieldName(), partSource, ctx.getUploadMimeType(), "utf-8"));
        for (KeyValuePairParser.Pair pair : ctx.getAdditionalPostParameters()) {
            parts.add(new StringPart(pair.key, pair.value));
        }

        return parts.toArray(new Part[parts.size()]);
    }

    private static class DontRetryHandler implements HttpMethodRetryHandler {
        public boolean retryMethod(HttpMethod httpMethod, IOException e, int i) {
            System.out.println("error while uploading: " + e + ", not retrying");
            return false;
        }
    }
}
