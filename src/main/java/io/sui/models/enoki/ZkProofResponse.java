package io.sui.models.enoki;

import java.io.Serializable;

public class ZkProofResponse implements Serializable {

    private ProofPoints proofPoints;
    private IssBase64Details issBase64Details;
    private String headerBase64;
    private String addressSeed;

    public ZkProofResponse() {
    }

    public ZkProofResponse(ProofPoints proofPoints, IssBase64Details issBase64Details, String headerBase64, String addressSeed) {
        this.proofPoints = proofPoints;
        this.issBase64Details = issBase64Details;
        this.headerBase64 = headerBase64;
        this.addressSeed = addressSeed;
    }

    public ProofPoints getProofPoints() {
        return proofPoints;
    }

    public void setProofPoints(ProofPoints proofPoints) {
        this.proofPoints = proofPoints;
    }

    public IssBase64Details getIssBase64Details() {
        return issBase64Details;
    }

    public void setIssBase64Details(IssBase64Details issBase64Details) {
        this.issBase64Details = issBase64Details;
    }

    public String getHeaderBase64() {
        return headerBase64;
    }

    public void setHeaderBase64(String headerBase64) {
        this.headerBase64 = headerBase64;
    }

    public String getAddressSeed() {
        return addressSeed;
    }

    public void setAddressSeed(String addressSeed) {
        this.addressSeed = addressSeed;
    }
}
