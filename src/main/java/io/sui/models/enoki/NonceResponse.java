package io.sui.models.enoki;

import java.io.Serializable;

public class NonceResponse implements Serializable {
    private String nonce;
    private String randomness;
    private String epoch;
    private String maxEpoch;
    private String estimatedExpiration;

    public NonceResponse() {
    }

    public NonceResponse(String nonce, String randomness, String epoch, String maxEpoch, String estimatedExpiration) {
        this.nonce = nonce;
        this.randomness = randomness;
        this.epoch = epoch;
        this.maxEpoch = maxEpoch;
        this.estimatedExpiration = estimatedExpiration;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getRandomness() {
        return randomness;
    }

    public void setRandomness(String randomness) {
        this.randomness = randomness;
    }

    public String getEpoch() {
        return epoch;
    }

    public void setEpoch(String epoch) {
        this.epoch = epoch;
    }

    public String getMaxEpoch() {
        return maxEpoch;
    }

    public void setMaxEpoch(String maxEpoch) {
        this.maxEpoch = maxEpoch;
    }

    public String getEstimatedExpiration() {
        return estimatedExpiration;
    }

    public void setEstimatedExpiration(String estimatedExpiration) {
        this.estimatedExpiration = estimatedExpiration;
    }
}
