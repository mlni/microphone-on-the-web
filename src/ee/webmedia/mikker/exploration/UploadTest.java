package ee.webmedia.mikker.exploration;

import ee.webmedia.mikker.upload.Uploader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class UploadTest
{
    private static final String Boundary = "--7d021a37605f0";

    public void upload(URL url, List<File> files) throws Exception
    {
        HttpURLConnection theUrlConnection = (HttpURLConnection) url.openConnection();
        theUrlConnection.setRequestMethod("POST");
        theUrlConnection.setDoOutput(true);
        theUrlConnection.setDoInput(true);
        theUrlConnection.setUseCaches(false);
        theUrlConnection.setChunkedStreamingMode(1024);

        theUrlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                + Boundary);

        DataOutputStream httpOut = new DataOutputStream(theUrlConnection.getOutputStream());

        for (int i = 0; i < files.size(); i++)
        {
            File f = files.get(i);
            String str = "--" + Boundary + "\r\n"
                       + "Content-Disposition: form-data;name=\"file\"; filename=\"" + f.getName() + "\"\r\n"
                       + "Content-Type: image/png\r\n"
                       + "\r\n";

            httpOut.write(str.getBytes());

            FileInputStream uploadFileReader = new FileInputStream(f);
            int numBytesToRead = 1024;
            int availableBytesToRead;
            while ((availableBytesToRead = uploadFileReader.available()) > 0)
            {
                byte[] bufferBytesRead;
                bufferBytesRead = availableBytesToRead >= numBytesToRead ? new byte[numBytesToRead]
                        : new byte[availableBytesToRead];
                uploadFileReader.read(bufferBytesRead);
                httpOut.write(bufferBytesRead);
                httpOut.flush();
            }
            httpOut.write(("\r\n\r\n--" + Boundary + "--\r\n").getBytes());

        }

        httpOut.write(("--" + Boundary + "--\r\n").getBytes());

        httpOut.flush();
        httpOut.close();

        // read & parse the response
        InputStream is = theUrlConnection.getInputStream();
        StringBuilder response = new StringBuilder();
        byte[] respBuffer = new byte[4096];
        while (is.read(respBuffer) >= 0)
        {
            response.append(new String(respBuffer).trim());
        }
        is.close();
        System.out.println(response.toString());
    }

    public static void main(String[] args) throws Exception
    {
//        List<File> list = new ArrayList<File>();
//        list.add(new File("mikker.iml"));
//
//        UploadTest uploader = new UploadTest();
//        uploader.upload(new URL("http://localhost/~matti/recorder/upload.php"), list);
        Uploader u = new Uploader("http://localhost/~matti/recorder/upload.php");
        u.upload("uus.txt", "text/plain", "KALAMAJASURM".getBytes());
    }

}