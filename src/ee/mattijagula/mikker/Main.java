package ee.mattijagula.mikker;

import ee.mattijagula.mikker.ui.MainWindow;

import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static void main(String args[]) throws Exception {
        Configuration.ParameterSource src = new Configuration.ParameterSource() {
            public String getParameter(String name) {
                if ("upload_url".equals(name))
                    return "/~matti/recorder/upload.php";
                if ("post_parameters".equals(name))
                    return "MAX_FILE_SIZE=20000000; tx_fileupload_pi1[do_upload]=Saada fail!";
                if ("upload_field_name".equals(name))
                    return "fail";
                return null;
            }
            public URI getBaseUri() {
                try {
                    return new URI("http://localhost/");
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            public String getCookies() {
                return "tracking=cookie";
            }
            public String determineUserAgent() {
                return "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_8; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.215 Safari/534.10";
            }
        };

        new MainWindow(new Configuration(src)).setVisible(true);
    }
}
