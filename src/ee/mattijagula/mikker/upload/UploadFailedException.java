package ee.mattijagula.mikker.upload;

public class UploadFailedException extends Exception {
    public UploadFailedException(String s) {
        super(s);
    }

    public UploadFailedException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
