package ch.jtaf.i18n;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class I18n {

    private static I18n instance;
    private Map<String, String> translations = new HashMap<String, String>();

    private I18n() {
        try {
            String urlString = System.getProperty("jtaf.confirmation.url");
            URL url = new URL(urlString + "i18n/messages_de.json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (!inputLine.contains("{") && !inputLine.contains("}")) {
                        inputLine = inputLine.replace(",", "");
                        inputLine = inputLine.replace("\"", "");
                        String[] tokens = inputLine.split(":");
                        translations.put(tokens[0].trim(), tokens[1].trim());
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(I18n.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static I18n getInstance() {
        if (instance == null) {
            instance = new I18n();
        }
        return instance;
    }
    
    public String getString(String key) {
        return translations.get(key);
    }
}
