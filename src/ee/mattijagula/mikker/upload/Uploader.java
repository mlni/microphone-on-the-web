package ee.mattijagula.mikker.upload;

import ee.mattijagula.mikker.Configuration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.PartSource;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Uploader {
    private static final String BOUNDARY = "----WebKitFormBoundary8NHXoPOgtdmTKB7e";
    
    private Configuration ctx;

    public Uploader(Configuration ctx) {
        this.ctx = ctx;
    }

    public void upload(byte content[], ProgressListener progressListener) throws UploadFailedException {
        try {
            doUpload(content, progressListener);
        } catch (IOException e) {
            throw new UploadFailedException("Upload failed with exception: " + e.getMessage(), e);
        }
    }

    private void doUpload(final byte content[], ProgressListener progressListener)
            throws IOException, UploadFailedException {
        HttpClient client = new HttpClient();

        // PostMethod post = new PostMethod("http://localhost/~matti/recorder/upload.php");
        PostMethod post = new PostMethod(ctx.getUploadUrl());

        post.addRequestHeader("User-Agent", ctx.getUserAgent());

        if (!"".equals(ctx.getCookies())) {
            post.addRequestHeader("Cookie", ctx.getCookies());
        }


        for (KeyValuePairParser.Pair pair : ctx.getAdditionalPostParameters()) {
            post.addParameter(pair.key, pair.value);
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

        Part[] parts = {
                new FilePart(ctx.getFileFieldName(), partSource, ctx.getUploadMimeType(), "utf-8")
        };

        MultipartRequestEntity requestEntity = new MultipartRequestEntity(parts, post.getParams());
        post.setRequestEntity(new ProgressReportingRequestEntity(requestEntity, progressListener));

        int status = client.executeMethod(post);
        if (status == HttpStatus.SC_OK) {
            progressListener.finished();
            System.out.println("Upload complete, response=" + post.getResponseBodyAsString());
        } else {
            throw new UploadFailedException("Upload failed with code " + HttpStatus.getStatusText(status));
        }
    }

    public void upload_old(byte content[]) throws IOException {
        URL url = new URL(ctx.getUploadUrl());
        HttpURLConnection theUrlConnection = (HttpURLConnection) url.openConnection();
        theUrlConnection.setRequestMethod("POST");
        theUrlConnection.setDoOutput(true);
        theUrlConnection.setDoInput(true);
        theUrlConnection.setUseCaches(false);

        theUrlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        if (!"".equals(ctx.getCookies()))
            theUrlConnection.setRequestProperty("Cookie", ctx.getCookies());
        theUrlConnection.setRequestProperty("User-Agent", ctx.getUserAgent());

        DataOutputStream httpOut = new DataOutputStream(theUrlConnection.getOutputStream());

        String str = "--" + BOUNDARY + "\r\n"
                   + "Content-Disposition: form-data; name=\"" + ctx.getFileFieldName() + "\"; filename=\"" + ctx.getUploadFilename() + "\"\r\n"
                   + "Content-Type: " + ctx.getUploadMimeType() + "\r\n"
                   + "\r\n";

        httpOut.write(str.getBytes());

        httpOut.write(content);

        KeyValuePairParser.Pair postParameters[] = ctx.getAdditionalPostParameters();
        for (KeyValuePairParser.Pair pair : postParameters) {
            String name = pair.key;
            String value = pair.value;
            httpOut.write(("\r\n--" + BOUNDARY + "\r\n").getBytes());
            httpOut.write(("Content-Disposition: form-data; name=\""+ name +"\"\r\n" +
                    "\r\n").getBytes());
            httpOut.write(value.getBytes());
        }

        // last one gets extra hypens at the end
        httpOut.write(("\r\n--" + BOUNDARY + "--\r\n").getBytes());
 
        httpOut.flush();
        httpOut.close();

        // read & parse the response
        InputStream is = theUrlConnection.getInputStream();
        StringBuilder response = new StringBuilder();
        byte[] respBuffer = new byte[4096];
        int len;
        while ((len = is.read(respBuffer)) >= 0) {
            response.append(new String(respBuffer, 0, len).trim());
        }
        is.close();
        System.out.println(response.toString());
    }
}
