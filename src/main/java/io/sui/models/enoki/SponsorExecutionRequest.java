package io.sui.models.enoki;

import java.io.Serializable;

public class SponsorExecutionRequest implements Serializable {
    private String signature;
    private String digest;

    public SponsorExecutionRequest(String signature, String digest) {
        this.signature = signature;
        this.digest = digest;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    @Override
    public String toString() {
        return "{ \"signature\": \"" + signature + "\" }";
    }
}