package io.sui.bcsgen;

import io.sui.models.enoki.ZkProofResponse;

import java.util.List;

public class ZkLoginSignature {

    public final ZkProof inputs;
    public final String maxEpoch;
    public final List<Byte> userSignature;

    public ZkLoginSignature(ZkProof proofResponse, String maxEpoch, List<Byte> userSignature) {
        this.inputs = proofResponse;
        this.maxEpoch = maxEpoch;
        this.userSignature = userSignature;
    }

    public void serialize(com.novi.serde.Serializer serializer) throws com.novi.serde.SerializationError {
        serializer.increase_container_depth();
        inputs.serialize(serializer);
        serializer.serialize_str(maxEpoch);
        TraitHelpers.serialize_vector_u8(userSignature, serializer);
        serializer.decrease_container_depth();
    }

    public byte[] bcsSerialize() throws com.novi.serde.SerializationError {
        com.novi.serde.Serializer serializer = new com.novi.bcs.BcsSerializer();
        serialize(serializer);
        return serializer.get_bytes();
    }
}
