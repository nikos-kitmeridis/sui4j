package io.sui.models.enoki;

import java.io.Serializable;
import java.util.List;

public class SponsorRequest implements Serializable {
    private String network;
    private String transactionBlockKindBytes;
    private String sender;
    private List<String> allowedAddresses;
    private List<String> allowedMoveCallTargets;

    public SponsorRequest() {
    }

    public SponsorRequest(String network, String transactionBlockKindBytes, String sender, List<String> allowedAddresses, List<String> allowedMoveCallTargets) {
        this.network = network;
        this.transactionBlockKindBytes = transactionBlockKindBytes;
        this.sender = sender;
        this.allowedAddresses = allowedAddresses;
        this.allowedMoveCallTargets = allowedMoveCallTargets;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getTransactionBlockKindBytes() {
        return transactionBlockKindBytes;
    }

    public void setTransactionBlockKindBytes(String transactionBlockKindBytes) {
        this.transactionBlockKindBytes = transactionBlockKindBytes;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getAllowedAddresses() {
        return allowedAddresses;
    }

    public void setAllowedAddresses(List<String> allowedAddresses) {
        this.allowedAddresses = allowedAddresses;
    }

    public List<String> getAllowedMoveCallTargets() {
        return allowedMoveCallTargets;
    }

    public void setAllowedMoveCallTargets(List<String> allowedMoveCallTargets) {
        this.allowedMoveCallTargets = allowedMoveCallTargets;
    }

    @Override
    public String toString() {
        return "{" +
                "\"network\":\"" + network + "\"," +
                "\"transactionBlockKindBytes\":\"" + transactionBlockKindBytes + "\"," +
                "\"sender\":\"" + sender + "\"," +
                "\"allowedAddresses\":" + toJsonArray(allowedAddresses) + "," +
                "\"allowedMoveCallTargets\":" + toJsonArray(allowedMoveCallTargets) +
                "}";
    }

    private String toJsonArray(List<String> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        return list.stream()
                .map(item -> "\"" + item.replace("\"", "\\\"") + "\"") // Escape quotes in each string
                .reduce("[", (partial, item) -> partial + (partial.equals("[") ? "" : ",") + item) + "]";
    }
}