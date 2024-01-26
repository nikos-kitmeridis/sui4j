package io.sui.bcsgen;

import io.sui.models.enoki.IssBase64Details;
import io.sui.models.enoki.ProofPoints;

public class ZkProof {

    public ZkProofPoints proofPoints;
    public ZkIssBase64Details issBase64Details;
    public String headerBase64;
    public String addressSeed;

    public ZkProof(ZkProofPoints proofPoints, ZkIssBase64Details issBase64Details, String headerBase64, String addressSeed) {
        this.proofPoints = proofPoints;
        this.issBase64Details = issBase64Details;
        this.headerBase64 = headerBase64;
        this.addressSeed = addressSeed;
    }

    public void serialize(com.novi.serde.Serializer serializer) throws com.novi.serde.SerializationError {
        serializer.increase_container_depth();
        proofPoints.serialize(serializer);
        issBase64Details.serialize(serializer);
        serializer.serialize_str(headerBase64);
        serializer.serialize_str(addressSeed);
        serializer.decrease_container_depth();
    }

    public byte[] bcsSerialize() throws com.novi.serde.SerializationError {
        com.novi.serde.Serializer serializer = new com.novi.bcs.BcsSerializer();
        serialize(serializer);
        return serializer.get_bytes();
    }
}
