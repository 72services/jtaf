package ch.jtaf.security.test;

import org.jboss.crypto.CryptoUtil;
import org.junit.Test;

public class CryptoUtilTest {

    @Test
    public void hashPassword() {
        String passwordHash = CryptoUtil.createPasswordHash("MD5", "BASE64", null, null, "password");
        System.out.println(passwordHash);
    }
}
