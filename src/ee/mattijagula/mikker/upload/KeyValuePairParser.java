package ee.mattijagula.mikker.upload;

import java.util.ArrayList;
import java.util.List;

/**
 * String manipulator for parsing key-value pairs from semicolon-separated
 * strings.
 */
public class KeyValuePairParser {
    private Pair[] pairs;

    public KeyValuePairParser(String pairs) {
        this.pairs = parsePairs(pairs);
    }

    private Pair[] parsePairs(String cookies) {
        List<Pair> result = new ArrayList<Pair>();
        while (cookies != null && !"".equals(cookies.trim())) {
            int start = cookies.indexOf("=");
            int end = cookies.indexOf(";") != -1 ? cookies.indexOf(";") : cookies.length();

            String name = cookies.substring(0, start).trim();
            String value = cookies.substring(start + 1, end).trim();

            result.add(new Pair(name, value));

            cookies = cookies.length() > end ? cookies.substring(end + 1) : "";
        }
        return result.toArray(new Pair[0]);
    }

    public Pair[] getPairs() {
        return this.pairs;
    }
    
    public static class Pair {
        public final String key;
        public final String value;
        public Pair(String name, String value) {
            this.key = name;
            this.value = value;
        }
        public String toString() {
            return "[" + key + "=" + value + "]";
        }
    }
}
