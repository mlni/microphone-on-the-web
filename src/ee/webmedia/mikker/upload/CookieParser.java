package ee.webmedia.mikker.upload;

import java.util.*;

public class CookieParser {
    private Map<String, Cookie> cookies;

    public CookieParser(String cookies) {
        this.cookies = parseCookies(cookies);
    }

    private HashMap<String, Cookie> parseCookies(String cookies) {
        KeyValuePairParser.Pair pairs[] = new KeyValuePairParser(cookies).getPairs();

        HashMap<String, Cookie> result = new HashMap<String, Cookie>();
        for (KeyValuePairParser.Pair keyValue : pairs)
            result.put(keyValue.key, new Cookie(keyValue));
        return result;
    }

    public Cookie getCookie(String name) {
        return cookies.get(name.toLowerCase());
    }

    public Cookie[] getCookies(String cookieNames) {
        String requestedNames[] = cookieNames.split(",");
        List<Cookie> result = new ArrayList<Cookie>();
        for (String name : requestedNames) {
            if (cookies.containsKey(name.trim().toLowerCase()))
                result.add(getCookie(name.trim()));
        }
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
        System.out.println("cookies: " + Arrays.asList(parser.getCookies("tz, JSESSION_ID")));
    }
}
