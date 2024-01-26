/*
 * Copyright 2022-2023 281165273grape@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.sui.crypto;


import com.google.common.primitives.Bytes;
import io.sui.zklogin.Utils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jcajce.provider.digest.Blake2b.Blake2b256;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import java.io.Serializable;
import java.security.SecureRandom;

/**
 * The type Secp256k1 key pair.
 *
 * @author grapebaba
 * @since 2022.11
 */
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class ED25519KeyPair extends SuiKeyPair<AsymmetricCipherKeyPair> implements Serializable {

  /**
   * Instantiates a new Ed 25519 key pair.
   *
   * @param privateKeyParameters the private key parameters
   * @param publicKeyParameters the public key parameters
   */
  public ED25519KeyPair(
      Ed25519PrivateKeyParameters privateKeyParameters,
      Ed25519PublicKeyParameters publicKeyParameters) {
    this.keyPair = new AsymmetricCipherKeyPair(publicKeyParameters, privateKeyParameters);
  }

  @Override
  public String address() {
    final Blake2b256 blake2b256 = new Blake2b256();
    final byte[] hash =
        blake2b256.digest(
            Arrays.prepend(
                ((Ed25519PublicKeyParameters) keyPair.getPublic()).getEncoded(),
                SignatureScheme.ED25519.getScheme()));
    return "0x" + StringUtils.substring(Hex.toHexString(hash), 0, 64);
  }

  @Override
  public byte[] publicKeyBytes() {
    return ((Ed25519PublicKeyParameters) keyPair.getPublic()).getEncoded();
  }

  @Override
  public SignatureScheme signatureScheme() {
    return SignatureScheme.ED25519;
  }

  @Override
  public byte[] sign(byte[] msg) throws SigningException {
    Signer signer = new Ed25519Signer();
    signer.init(true, keyPair.getPrivate());
    signer.update(msg, 0, msg.length);
    try {
      return signer.generateSignature();
    } catch (CryptoException e) {
      throw new SigningException(e);
    }
  }

  /**
   * Decode base 64 sui key pair.
   *
   * @param encoded the encoded
   * @return the sui key pair
   */
  public static ED25519KeyPair decodeBase64(byte[] encoded) {
    return decodeBase64(encoded, 1);
  }

  public static ED25519KeyPair decodeBase64(byte[] encoded, int offset) {
    Ed25519PrivateKeyParameters privateKeyParameters = new Ed25519PrivateKeyParameters(encoded, offset);
    Ed25519PublicKeyParameters publicKeyParameters = privateKeyParameters.generatePublicKey();
    return new ED25519KeyPair(privateKeyParameters, publicKeyParameters);
  }

  /**
   * Encode base 64 sui key.
   *
   * @return the sui key
   */
  public String encodePrivateKey() {
    Ed25519PrivateKeyParameters pair = (Ed25519PrivateKeyParameters) this.keyPair.getPrivate();
    byte[] data = Bytes.concat(new byte[] {SignatureScheme.ED25519.getScheme()}, pair.getEncoded());
    return Base64.toBase64String(data);
  }

  public static ED25519KeyPair generate() {
    Ed25519PrivateKeyParameters privateKey = new Ed25519PrivateKeyParameters(new SecureRandom());
    Ed25519PublicKeyParameters publicKey = privateKey.generatePublicKey();
    return new ED25519KeyPair(privateKey, publicKey);
  }

  public static String toSuiBytes(byte[] publicKey) {
    byte[] flaggedPublicKey = Bytes.concat(new byte[] {SignatureScheme.ED25519.getScheme()}, publicKey);
    return Utils.toB64(flaggedPublicKey);
  }
}