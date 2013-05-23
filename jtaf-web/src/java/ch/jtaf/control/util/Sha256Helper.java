package ch.jtaf.control.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sha256Helper {

    public static String digest(String string) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(string.getBytes());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Sha256Helper.class.getName()).log(Level.SEVERE, null, ex);
            return string;
        }
    }
}
