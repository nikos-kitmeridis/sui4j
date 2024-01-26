package io.sui.zklogin;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class NonceTest {

    @Test
    void toBigIntBE() {
        byte[] byteArray = new byte[] {
            106, (byte) 242, 26, 113, (byte) 201, (byte) 239, 26, 12,
            11, 83, 72, (byte) 140, (byte) 164, 80, 32, (byte) 231
        };
        assertEquals(new BigInteger("142155239750894113117892913608487084263"), Nonce.toBigIntBE(byteArray));
    }

    @Test
    void generateRandomness() {
    }

    @Test
    void generateNonce() {
    }
}
