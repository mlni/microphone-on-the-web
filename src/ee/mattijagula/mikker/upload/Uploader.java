package ee.mattijagula.mikker.upload;

import ee.mattijagula.mikker.Configuration;
import ee.mattijagula.mikker.upload.http.SimpleHttpMultiformRequest;

import java.io.IOException;
import java.net.URL;

/**
 * Interface for the file upload subsystem.
 */
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

    private void doUpload(byte[] content, ProgressListener progressListener) throws IOException {
        URL url = new URL(ctx.getUploadUrl());
        SimpleHttpMultiformRequest post = new SimpleHttpMultiformRequest(url, progressListener);
        
        post.addHeader("User-Agent", ctx.getUserAgent());
        post.addHeader("Cookie", ctx.getCookies());

        for (KeyValuePairParser.Pair pair : ctx.getAdditionalPostParameters()) {
            post.addPostParameter(pair.key, pair.value);
        }

        post.send(ctx.getFileFieldName(), ctx.getUploadFilename(), ctx.getUploadMimeType(), content);
    }
}
