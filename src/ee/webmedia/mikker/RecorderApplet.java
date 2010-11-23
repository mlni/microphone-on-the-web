package ee.webmedia.mikker;

import ee.webmedia.mikker.ui.RecorderPanel;

import javax.swing.*;

public class RecorderApplet extends JApplet {
    public void init() {
        String uploadUrl = getDocumentBase() + arg("upload_url", "upload.php");
        String fieldName = arg("upload_field_name", "file");
        String filename = arg("filename", "sound-" + System.currentTimeMillis());

        System.out.println("Parameters: upload to = " + uploadUrl +
                ", upload field = " + fieldName +
                ", default filename = " + filename);

        RecorderPanel recorderPanel = new RecorderPanel(uploadUrl, fieldName, filename);
        
        getContentPane().add(recorderPanel);

        System.out.println("memory: " + Runtime.getRuntime().maxMemory());
    }

    private String arg(String argName, String defaultValue) {
        String val = getParameter(argName);
        if (val == null || "".equals(val.trim()))
            return defaultValue;
        return val.trim();
    }
}
