package io.sui.bcsgen;

import java.util.Objects;

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

    public static ZkProof deserialize(com.novi.serde.Deserializer deserializer) throws com.novi.serde.DeserializationError {
        deserializer.increase_container_depth();
        Builder builder = new Builder();
        builder.proofPoints = ZkProofPoints.deserialize(deserializer);
        builder.issBase64Details = ZkIssBase64Details.deserialize(deserializer);
        builder.headerBase64 = deserializer.deserialize_str();
        builder.addressSeed = deserializer.deserialize_str();
        deserializer.decrease_container_depth();
        return builder.build();
    }

    public static ZkProof bcsDeserialize(byte[] input) throws com.novi.serde.DeserializationError {
        if (input == null) {
            throw new com.novi.serde.DeserializationError("Cannot deserialize null array");
        }
        com.novi.serde.Deserializer deserializer = new com.novi.bcs.BcsDeserializer(input);
        ZkProof value = deserialize(deserializer);
        if (deserializer.get_buffer_offset() < input.length) {
            throw new com.novi.serde.DeserializationError("Some input bytes were not read");
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZkProof zkProof = (ZkProof) o;
        return Objects.equals(proofPoints, zkProof.proofPoints) &&
                Objects.equals(issBase64Details, zkProof.issBase64Details) &&
                Objects.equals(headerBase64, zkProof.headerBase64) &&
                Objects.equals(addressSeed, zkProof.addressSeed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proofPoints, issBase64Details, headerBase64, addressSeed);
    }

    public static final class Builder {
        public ZkProofPoints proofPoints;
        public ZkIssBase64Details issBase64Details;
        public String headerBase64;
        public String addressSeed;

        public ZkProof build() {
            return new ZkProof(proofPoints, issBase64Details, headerBase64, addressSeed);
        }
    }
}
