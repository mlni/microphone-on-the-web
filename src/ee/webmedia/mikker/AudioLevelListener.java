package ee.webmedia.mikker;

public interface AudioLevelListener {
    /** Audio level on a scale of 0 - 100 */
    void onLevelChange(int level);
}
