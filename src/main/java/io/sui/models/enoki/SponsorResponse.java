package io.sui.models.enoki;

import java.io.Serializable;

public class SponsorResponse implements Serializable {
    private String digest;
    private String bytes;

    public SponsorResponse() {
    }

    public SponsorResponse(String digest, String bytes) {
        this.digest = digest;
        this.bytes = bytes;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    @Override
    public String toString() {
        return "SponsorResponse{" +
                "digest='" + digest + '\'' +
                ", bytes='" + bytes + '\'' +
                '}';
    }
}