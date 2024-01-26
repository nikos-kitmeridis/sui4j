package io.sui.zklogin;

import java.math.BigInteger;
import java.util.Arrays;

public class Utils {

    public static int findFirstNonZeroIndex(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                return i;
            }
        }
        return -1;
    }

    public static byte[] toPaddedBigEndianBytes(BigInteger num, int width) {
        byte[] byteArray = num.toByteArray();
        if (byteArray.length < width) {
            byte[] paddedArray = new byte[width];
            System.arraycopy(byteArray, 0, paddedArray, width - byteArray.length, byteArray.length);
            return paddedArray;
        } else {
            return Arrays.copyOfRange(byteArray, byteArray.length - width, byteArray.length);
        }
    }

    public static byte[] toBigEndianBytes(BigInteger num, int width) {
        byte[] bytes = toPaddedBigEndianBytes(num, width);
        int firstNonZeroIndex = findFirstNonZeroIndex(bytes);
        return firstNonZeroIndex == -1 ? new byte[]{0} : Arrays.copyOfRange(bytes, firstNonZeroIndex, bytes.length);
    }

    private static char uint6ToB64(int nUint6) {
        return (char) (nUint6 < 26 ? nUint6 + 65
                : nUint6 < 52 ? nUint6 + 71
                : nUint6 < 62 ? nUint6 - 4
                : nUint6 == 62 ? 43
                : nUint6 == 63 ? 47
                : 65);
    }

    public static String toB64(byte[] aBytes) {
        int nMod3 = 2;
        StringBuilder sB64Enc = new StringBuilder();

        int nLen = aBytes.length;
        int nUint24 = 0;
        for (int nIdx = 0; nIdx < nLen; nIdx++) {
            nMod3 = nIdx % 3;
            nUint24 |= (aBytes[nIdx] & 0xFF) << ((16 >>> nMod3) & 24);
            if (nMod3 == 2 || aBytes.length - nIdx == 1) {
                sB64Enc.append(uint6ToB64((nUint24 >>> 18) & 63));
                sB64Enc.append(uint6ToB64((nUint24 >>> 12) & 63));
                sB64Enc.append(uint6ToB64((nUint24 >>> 6) & 63));
                sB64Enc.append(uint6ToB64(nUint24 & 63));
                nUint24 = 0;
            }
        }

        return sB64Enc.substring(0, sB64Enc.length() - 2 + nMod3) +
                (nMod3 == 2 ? "" : nMod3 == 1 ? "=" : "==");
    }

}

