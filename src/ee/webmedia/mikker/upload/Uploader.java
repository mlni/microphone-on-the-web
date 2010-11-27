package ee.webmedia.mikker.upload;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Uploader {
    private static final String BOUNDARY = "----WebKitFormBoundary8NHXoPOgtdmTKB7e";
    
    private Configuration ctx;

    public Uploader(Configuration ctx) {
        this.ctx = ctx;
    }

    public void upload(byte content[]) throws IOException {
        URL url = new URL(ctx.getUploadUrl());
        HttpURLConnection theUrlConnection = (HttpURLConnection) url.openConnection();
        theUrlConnection.setRequestMethod("POST");
        theUrlConnection.setDoOutput(true);
        theUrlConnection.setDoInput(true);
        theUrlConnection.setUseCaches(false);

        theUrlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        theUrlConnection.setRequestProperty("Cookie", CookieParser.toRequestHeader(ctx.getCookies()));
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
