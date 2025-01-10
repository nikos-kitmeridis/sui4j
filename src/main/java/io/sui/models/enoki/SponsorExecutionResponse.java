package io.sui.models.enoki;

import java.io.Serializable;

public class SponsorExecutionResponse implements Serializable {
    private String digest;

    public SponsorExecutionResponse() {
    }

    public SponsorExecutionResponse(String digest) {
        this.digest = digest;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    @Override
    public String toString() {
        return "SponsorExecutionResponse{" +
                "digest='" + digest + '\'' +
                '}';
    }
}