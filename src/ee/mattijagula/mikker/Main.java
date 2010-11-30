package ee.mattijagula.mikker;

import ee.mattijagula.mikker.ui.MainWindow;

import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static void main(String args[]) throws Exception {
        Configuration.ParameterSource src = new Configuration.ParameterSource() {
            public String getParameter(String name) {
                if ("upload_url".equals(name))
                    return "upload.php";
                if ("post_parameters".equals(name))
                    return "MAX_FILE_SIZE=HARDCODED";
                if ("upload_field_name".equals(name))
                    return "fail";
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
                return "secret-santa=ChuckNorris; king-of-pop=is-dead";
            }
            public String determineUserAgent() {
                return "Mozilla Gopher/4.0";
            }
        };

        new MainWindow(new Configuration(src)).setVisible(true);
    }
}
