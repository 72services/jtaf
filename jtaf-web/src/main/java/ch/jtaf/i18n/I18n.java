package ch.jtaf.i18n;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.jboss.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class I18n {

    private static I18n instance;
    private final Map<String, String> translationsDe = new HashMap<>();
    private final String path;
    private final static String MESSAGES_DE = "i18n/messages_de.json";
    private static final String DE = "DE";

    private I18n() {
        this.path = System.getProperty("jtaf.confirmation.url");
        fillDe();
    }

    public static I18n getInstance() {
        if (instance == null) {
            instance = new I18n();
        }
        return instance;
    }

    public String getString(Locale locale, String key) {
        if (locale.getLanguage().compareToIgnoreCase(DE) == 0) {
            String value = translationsDe.get(key);
            return value != null ? value : key;
        } else {
            return key;
        }
    }

    public Map<String, String> getMessages(Locale locale) {
        if (locale.getLanguage().compareToIgnoreCase(DE) == 0) {
            return translationsDe;
        } else {
            return new HashMap<>();
        }
    }

    private void fillDe() {
        try {
            URL url = new URL(path + MESSAGES_DE);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))) {
                fillMap(translationsDe, in);
            }
        } catch (IOException ex) {
            Logger.getLogger(I18n.class).error(ex.getMessage(), ex);
        }
    }

    private void fillMap(final Map<String, String> map, final BufferedReader in) throws IOException {
        JSONObject obj = (JSONObject) JSONValue.parse(convertInputStreamToString(in));
        Iterator iter = obj.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            map.put((String) entry.getKey(), (String) entry.getValue());
        }
    }

    private String convertInputStreamToString(final BufferedReader in) throws IOException {
        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        return sb.toString();
    }

}
