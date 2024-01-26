package io.sui.zklogin;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

import com.google.common.primitives.Bytes;
import com.loopring.poseidon.PoseidonHash;

import io.sui.crypto.SignatureScheme;

public class Nonce {
    private static final int NONCE_LENGTH = 27;

    public static BigInteger toBigIntBE(byte[] bytes) {
        byte[] positiveBytes = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, positiveBytes, 1, bytes.length);
        return new BigInteger(positiveBytes);
    }

    public static BigInteger generateRandomness() {
        byte[] randomBytes = new byte[16];
        new SecureRandom().nextBytes(randomBytes);
        return toBigIntBE(randomBytes);
    }


    public static String generateNonce(byte[] publicKey, int maxEpoch, BigInteger randomness) throws Exception {
        byte[] flaggedPublicKey = Bytes.concat(new byte[] {SignatureScheme.ED25519.getScheme()}, publicKey);
        BigInteger publicKeyBytes = toBigIntBE(flaggedPublicKey);
        BigInteger ephPublicKey0 = publicKeyBytes.shiftRight(128);
        BigInteger ephPublicKey1 = publicKeyBytes.and(BigInteger.ONE.shiftLeft(128).subtract(BigInteger.ONE));
        PoseidonHash.PoseidonParamsType params = PoseidonHash.DefaultParams;
        PoseidonHash poseidon = PoseidonHash.Digest.newInstance(params);
        poseidon.add(new BigInteger[]{ephPublicKey0, ephPublicKey1, BigInteger.valueOf(maxEpoch), randomness});
        BigInteger bigNum = poseidon.digest(false)[0];
        byte[] Z = Utils.toBigEndianBytes(bigNum, 20);
        String nonce = Base64.getUrlEncoder().encodeToString(Z).replaceFirst("=", "");

        if (nonce.length() != NONCE_LENGTH) {
            throw new Exception("Length of nonce " + nonce + " (" + nonce.length() + ") is not equal to " + NONCE_LENGTH);
        }

        return nonce;
    }
}
