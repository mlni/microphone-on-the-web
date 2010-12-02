package ee.mattijagula.mikker;

import ee.mattijagula.mikker.ui.MainWindow;

import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static void main(String args[]) throws Exception {
        Configuration.ParameterSource src = new Configuration.ParameterSource() {
            public String getParameter(String name) {
                if ("upload_url".equals(name))
                    return "http://localhost/~matti/recorder/upload.php";
                if ("post_parameters".equals(name))
                    return "MAX_FILE_SIZE=20000000; tx_fileupload_pi1[do_upload]=Saada fail!";
                if ("upload_field_name".equals(name))
                    return "tx_fileupload_pi1";
                return null;
            }
            public URI getBaseUri() {
                try {
                    return new URI("http://localhost/~matti/recorder/upload.php");
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            public String getCookies() {
                return "chuck=Norris";
            }
            public String determineUserAgent() {
                return "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_8; en-US) AppleWebKit/534.7 (KHTML, like Gecko) Chrome/7.0.517.44 Safari/534.7";
            }
        };

        new MainWindow(new Configuration(src)).setVisible(true);
    }
}
