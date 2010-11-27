package ee.webmedia.mikker.upload;

import java.net.URI;
import java.util.Arrays;

/**
 * Configuration context for the recorder applet.
 */
public class Configuration {
    public interface ParameterSource {
        String getParameter(String name);
        URI getBaseUri();
        String getCookies();
    }

    private final String uploadUrl;
    private final String fileFieldName;
    private final CookieParser.Cookie[] cookies;
    private String filename;
    private KeyValuePairParser.Pair[] additionalPostParameters;

    public Configuration(String uploadUrl, String fieldName, String filename) {
        this(uploadUrl, fieldName, filename, new CookieParser.Cookie[0]);
    }

    public Configuration(String uploadUrl, String fieldName, String filename, CookieParser.Cookie[] cookies) {
        this.uploadUrl = uploadUrl;
        this.fileFieldName = fieldName;
        this.filename = filename;
        this.cookies = cookies;
        this.additionalPostParameters = new KeyValuePairParser.Pair[0];
    }

    public Configuration(ParameterSource cfg) {
        this.fileFieldName = arg(cfg, "upload_field_name", "file");
        this.filename = arg(cfg, "filename", "sound-" + System.currentTimeMillis());

        String relativePath = arg(cfg, "upload_url", "");
        this.uploadUrl = composeUploadUrl(cfg, relativePath);

        String cookieNames = arg(cfg, "cookie_names", "");
        this.cookies = parseCookies(cfg, cookieNames);

        String postParameters = arg(cfg, "post_parameters", "");
        this.additionalPostParameters = new KeyValuePairParser(postParameters).getPairs();

        System.out.println("Parameters: upload to = " + uploadUrl +
                ", upload field = " + fileFieldName +
                ", default filename = " + filename +
                ", cookie names = " + cookieNames +
                ", cookies = " + Arrays.asList(cookies) +
                ", post parameters = " + Arrays.asList(additionalPostParameters));
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public String getFileFieldName() {
        return fileFieldName;
    }

    public CookieParser.Cookie[] getCookies() {
        return cookies;
    }

    public String getFilename() {
        return filename;
    }

    public KeyValuePairParser.Pair[] getAdditionalPostParameters() {
        return additionalPostParameters;
    }

    public void updateFilename(String filename) {
        this.filename = filename;
    }
    
    public String getUploadMimeType() {
        return "application/zip";
    }

    public String getUploadFilename() {
        return filename + ".zip";
    }

    private String composeUploadUrl(ParameterSource cfg, String relativePath) {
        return cfg.getBaseUri().resolve(relativePath).toString();
    }

    private String arg(ParameterSource conf, String argName, String defaultValue) {
        String val = conf.getParameter(argName);
        if (val == null || "".equals(val.trim()))
            return defaultValue;
        return val.trim();
    }

    private CookieParser.Cookie[] parseCookies(ParameterSource cfg, String cookieNames) {
        if (cookieNames == null || "".equals(cookieNames))
            return new CookieParser.Cookie[0];

        String browserCookies = cfg.getCookies();

        CookieParser parser = new CookieParser(browserCookies);
        return parser.getCookies(cookieNames);
    }
}
