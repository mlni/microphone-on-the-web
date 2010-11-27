package ee.webmedia.mikker.upload;

import java.util.*;

public class CookieParser {
    private Cookie allCookies[];

    public CookieParser(String cookies) {
        this.allCookies = parseCookies(cookies);
    }

    public Cookie[] getAllCookies() {
        return allCookies;
    }

    public Cookie getCookie(String name) {
        for (Cookie c : allCookies)
            if (c.name.equals(name.toLowerCase()))
                return c;
        return null;
    }

    private Cookie[] parseCookies(String cookies) {
        KeyValuePairParser.Pair pairs[] = new KeyValuePairParser(cookies).getPairs();

        List<Cookie> result = new ArrayList<Cookie>();
        for (KeyValuePairParser.Pair keyValue : pairs)
            result.add(new Cookie(keyValue));
        return result.toArray(new Cookie[0]);
    }

    public static String toRequestHeader(Cookie[] cookies) {
        String result = "";
        for (Cookie c : cookies) {
            if (!"".equals(result))
                result += "; ";
            result += c.toString();
        }
        return result;
    }

    public static class Cookie {
        public final String name;
        public final String value;
        
        public Cookie(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public Cookie(KeyValuePairParser.Pair keyValue) {
            this.name = keyValue.key;
            this.value = keyValue.value;
        }

        public String toString() {
            return name + "=" + value;
        }
    }

    public static void main(String[] args) {
        CookieParser parser = new CookieParser(
                "JSESSION_ID=alsjkfrfq4r90fjkasoifj34089tj; TZ=-120; " +
                "rememberme=true; TZ=-120; " +
                "SID=DQAAAJ4A;");
        Cookie tz = parser.getCookie("TZ");
        System.out.println("tz: " + tz);
    }
}
