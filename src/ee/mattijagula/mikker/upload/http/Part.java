package ee.mattijagula.mikker.upload.http;

/**
 * A part of the multipart form upload.
 */
public interface Part {
    long length();
    byte[] header();
    byte[] content();
    byte[] footer();
}
