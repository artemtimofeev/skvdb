package org.skvdb.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HashUtilTest {
    private static final String ENCODED_HASH = "XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=";
    private static final String PASSWORD = "password";

    @Test
    void getEncodedHashTest() {
        Assertions.assertEquals(ENCODED_HASH, HashUtil.getEncodedHash(PASSWORD));
    }

    @Test
    void comparePasswordAndHash() {
        Assertions.assertTrue(HashUtil.comparePasswordAndHash(PASSWORD, ENCODED_HASH));
    }
}
