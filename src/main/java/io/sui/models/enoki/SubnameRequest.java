package io.sui.models.enoki;

public class SubnameRequest {

    private String domain;
    private String network;
    private String subname;
    private String targetAddress;

    // Constructor
    public SubnameRequest(String domain, String network, String subname, String targetAddress) {
        this.domain = domain;
        this.network = network;
        this.subname = subname;
        this.targetAddress = targetAddress;
    }

    // Getters and Setters
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    @Override
    public String toString() {
        return String.format(
                "{\"domain\":\"%s\",\"network\":\"%s\",\"subname\":\"%s\",\"targetAddress\":\"%s\"}",
                domain, network, subname, targetAddress
        );
    }
}