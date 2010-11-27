package ee.webmedia.mikker;

import ee.webmedia.mikker.ui.RecorderPanel;
import ee.webmedia.mikker.upload.RequestContext;
import netscape.javascript.JSObject;

import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;

public class RecorderApplet extends JApplet implements RequestContext.ConfigurationSource {
    public void init() {
        RequestContext ctx = new RequestContext(this);
        RecorderPanel recorderPanel = new RecorderPanel(ctx);
        getContentPane().add(recorderPanel);

        usePlatformLookAndFeel();

        System.out.println("memory: " + Runtime.getRuntime().maxMemory());
    }


    private void usePlatformLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public URI getBaseUri() {
        try {
            return getDocumentBase().toURI();
        } catch (URISyntaxException e) {
            // cannot happen
            throw new IllegalStateException("Applet.getDocumentBase() returns illegal URI: " + getDocumentBase());
        }
    }

    public String getCookies() {
        JSObject window = JSObject.getWindow(this);
        JSObject document = (JSObject) window.getMember("document");
        return (String) document.getMember("cookie");
    }
}
