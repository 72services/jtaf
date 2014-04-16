package ch.jtaf.security;

import org.jboss.crypto.CryptoUtil;
import org.junit.Test;

public class CryptoUtilTest {

    @Test
    public void hashPassword() {
        String passwordHash = CryptoUtil.createPasswordHash("MD5", "BASE64", null, null, "linus");
        System.out.println(passwordHash);
    }
}
