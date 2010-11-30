package ee.mattijagula.mikker.upload;

public interface ProgressListener {
    void transferred(long transferred, long total);
    void finished();
}
