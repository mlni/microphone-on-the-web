package ee.mattijagula.mikker.upload.http;

import ee.mattijagula.mikker.upload.CountingOutputStream;
import ee.mattijagula.mikker.upload.ProgressListener;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A minimalistic HTTP multipart form client for uploading a single file to a web server.
 * It doesn't do redirects, chunked connections, http proxies or anything fancy, but it does upload
 * file to a form in a way that allows tracking the upload progress in a more or less reasonable
 * way. 
 */
public class SimpleHttpMultiformRequest {
    static final String BOUNDARY = "----WebKitFormBoundary8NHXoPOgtdmTKB7e";
    static final String NEWLINE = "\r\n";
    
    private URL url;
    private ProgressListener listener;

    private Map<String,String> headers = new LinkedHashMap<String,String>();
    private Map<String,String> postParameters = new LinkedHashMap<String,String>();

    private Socket connection;
    private InputStream inputStream;
    private OutputStream outputStream;

    private boolean debugResponse;
    
    public SimpleHttpMultiformRequest(URL url, ProgressListener listener) {
        this.url = url;
        this.listener = listener;
        initHeaders();
    }

    private void initHeaders() {
        headers.put("Host", url.getHost() + ":" + getPort());
        headers.put("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        headers.put("UserAgent", "SimpleHttpMultiformRequest/1.0");
    }

    private int getPort() {
        return (url.getPort() != -1 ? url.getPort() : 80);
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public void addPostParameter(String name, String value) {
        postParameters.put(name, value);
    }

    public void send(String formFieldName, String filename, String mime, byte content[]) throws IOException {
        try {
            doSend(formFieldName, filename, mime, content);
        } finally {
            closeConnection();
        }
    }

    private void doSend(String formFieldName, String filename, String mime, byte content[]) throws IOException {
        openConnection();
        List<Part> parts = new ArrayList<Part>();
        for (String postParameter : this.postParameters.keySet()) {
            parts.add(new ContentPart(postParameter, postParameters.get(postParameter)));
        }
        // code assumes that file part is always the last
        parts.add(new FileContentPart(formFieldName, filename, mime, content));

        long totalLength = 0;
        for (Part part : parts) {
            totalLength += part.length();
        }

        headers.put("Content-Length", "" + totalLength);

        StringBuilder headerString = composeRequestLine();
        for (String headerName : headers.keySet()) {
            headerString.append(headerName)
                    .append(": ")
                    .append(headers.get(headerName))
                    .append(NEWLINE);
        }
        headerString.append(NEWLINE);

        OutputStream requestStream = openHttpConnection(totalLength);
        requestStream.write(headerString.toString().getBytes());

        for (Part part : parts) {
            writeChunked(requestStream, part.header());
            writeChunked(requestStream, part.content());
            writeChunked(requestStream, part.footer());
        }
        requestStream.flush();

        BufferedReader response = openResponse();
        String headerLine = response.readLine();

        int responseCode = parseResponse(headerLine);

        if (responseCode >= 200 && responseCode < 400) {
            System.out.println("Response: ok " + responseCode);
            listener.finished();
            
            consumeResponse(response);
        } else {
            throw new IOException("Error uploading: " + responseCode);
        }
    }

    private void writeChunked(OutputStream requestStream, byte[] bytes) throws IOException {
        final int BUFFER_SIZE = 1024;
        int index = 0;
        while (index < bytes.length - 1) {
            int writeSize = Math.min(BUFFER_SIZE, bytes.length - index);
            requestStream.write(bytes, index, writeSize);
            index += writeSize;
        }
    }

    public void debugResponse() {
        this.debugResponse = true;
    }

    private void closeConnection() throws IOException {
        if (inputStream != null)
            inputStream.close();
        if (outputStream != null)
            outputStream.close();
        if (connection != null)
            connection.close();
    }

    private void openConnection() throws IOException {
        this.connection = new Socket(url.getHost(), getPort());
        this.outputStream = connection.getOutputStream();
        this.inputStream = connection.getInputStream();
    }

    private int parseResponse(String headerLine) {
        System.out.println("response: " + headerLine);
        Pattern statusLine = Pattern.compile("HTTP/1.[01] ([0-9]+) (.*)");
        Matcher m = statusLine.matcher(headerLine);
        if (m.matches())
            return Integer.parseInt(m.group(1));

        System.out.println("cannot parse response code");
        return 0;
    }

    private void consumeResponse(BufferedReader response) throws IOException {
        String line;
        while ((line = response.readLine()) != null) {
            if (debugResponse)
                System.out.println(line);
        }
    }

    private BufferedReader openResponse() {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    private OutputStream openHttpConnection(long length) {
        return new CountingOutputStream(outputStream, listener, length);
    }

    private StringBuilder composeRequestLine() {
        return new StringBuilder("POST " + url.getPath() + " HTTP/1.1" + NEWLINE);
    }

}
