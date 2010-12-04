package ee.mattijagula.mikker.upload.http;

public class FileContentPart implements Part {
    private byte header[];
    private byte content[];
    private byte footer[];
    public FileContentPart(String formFieldName, String filename, String mimeType, byte[] content) {
        String prefix = "--" + SimpleHttpMultiformRequest.BOUNDARY + SimpleHttpMultiformRequest.NEWLINE;
        prefix += "Content-Disposition: form-data; name=\"" + formFieldName + "\"; " +
                "filename=\"" + filename + "\"" + SimpleHttpMultiformRequest.NEWLINE;
        prefix += "Content-Type: " + mimeType + SimpleHttpMultiformRequest.NEWLINE;
        prefix += "Content-Transfer-Encoding: binary" + SimpleHttpMultiformRequest.NEWLINE;
        prefix += SimpleHttpMultiformRequest.NEWLINE;
        this.header = prefix.getBytes();

        // TODO: assumes that there's one file content and adds final boundary
        String suffix = SimpleHttpMultiformRequest.NEWLINE;
        suffix += "--" + SimpleHttpMultiformRequest.BOUNDARY + "-- " + SimpleHttpMultiformRequest.NEWLINE;
        this.footer = suffix.getBytes();

        this.content = content;
    }

    public long length() {
        return header.length + content.length + footer.length;
    }

    public byte[] header() {
        return header;
    }

    public byte[] content() {
        return content;
    }

    public byte[] footer() {
        return footer;
    }
}
