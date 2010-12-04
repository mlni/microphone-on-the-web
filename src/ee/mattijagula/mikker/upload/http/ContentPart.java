package ee.mattijagula.mikker.upload.http;

public class ContentPart implements Part {
    private byte bytes[];

    public ContentPart(String name, String value) {
        String contents = "";
        contents += "--" + SimpleHttpMultiformRequest.BOUNDARY + SimpleHttpMultiformRequest.NEWLINE;
        contents += "Content-Disposition: form-data; name=\""+ name +"\"" + SimpleHttpMultiformRequest.NEWLINE;
        contents += "Content-Type: text/plain; charset=US-ASCII" + SimpleHttpMultiformRequest.NEWLINE;
        contents += "Content-Transfer-Encoding: 8bit" + SimpleHttpMultiformRequest.NEWLINE;
        contents += SimpleHttpMultiformRequest.NEWLINE;
        contents += value;
        contents += SimpleHttpMultiformRequest.NEWLINE;

        this.bytes = contents.getBytes();
    }

    public long length() {
        return bytes.length;
    }

    public byte[] header() {
        return new byte[0];
    }

    public byte[] content() {
        return bytes;
    }

    public byte[] footer() {
        return new byte[0];
    }
}
