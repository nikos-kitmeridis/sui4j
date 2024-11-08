package io.sui.models.enoki;

public class ZkProofRequest {
    private String jwt;
    private String ephemeralPublicKey;
    private String maxEpoch;
    private String randomness;

    public ZkProofRequest() {
    }

    public ZkProofRequest(String jwt, String ephemeralPublicKey, String maxEpoch, String randomness) {
        this.jwt = jwt;
        this.ephemeralPublicKey = ephemeralPublicKey;
        this.maxEpoch = maxEpoch;
        this.randomness = randomness;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getEphemeralPublicKey() {
        return ephemeralPublicKey;
    }

    public void setEphemeralPublicKey(String ephemeralPublicKey) {
        this.ephemeralPublicKey = ephemeralPublicKey;
    }

    public String getMaxEpoch() {
        return maxEpoch;
    }

    public void setMaxEpoch(String maxEpoch) {
        this.maxEpoch = maxEpoch;
    }

    public String getRandomness() {
        return randomness;
    }

    public void setRandomness(String randomness) {
        this.randomness = randomness;
    }

    @Override
    public String toString() {
        return "{" +
                "\"ephemeralPublicKey\":\"" + escapeString(ephemeralPublicKey) + "\"," +
                "\"maxEpoch\":" + escapeString(maxEpoch) + "," +
                "\"randomness\":\"" + escapeString(randomness) + "\"" +
                "}";
    }

    private String escapeString(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
