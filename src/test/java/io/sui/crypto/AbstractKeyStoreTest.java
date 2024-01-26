package io.sui.crypto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class AbstractKeyStoreTest {

    @Test
    void generateSuiKeyPair() {
        var keypair = new AbstractKeyStore() {
            @Override
            public void addKey(String address, SuiKeyPair<?> keyPair) {

            }
        };
       var suiKeyPair = keypair.generateSuiKeyPair(SignatureScheme.ED25519);
       assertEquals(32, suiKeyPair.publicKeyBytes().length);
    }

    @Test
    void generateSuiKeypair_fromSecretKey() {

        var secretKey = Base64.getDecoder().decode("mdqVWeFekT7pqy5T49+tV12jO0m+ESW7ki4zSU9JiCg=");
        ED25519KeyPair ed25519KeyPair = ED25519KeyPair.decodeBase64(secretKey, 0);
        assertEquals(
                "Gy9JCW4+Xb0Pz6nAwM2S2as7IVRLNNXdSmXZi4eLmSI=",
                new String(Base64.getEncoder().encode(ed25519KeyPair.publicKeyBytes()))
        );
    }
}