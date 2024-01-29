package io.sui.bcsgen;

import java.util.List;
import java.util.Objects;

public class ZkLoginSignatureData {

    public final ZkProof inputs;
    public final String maxEpoch;
    public final List<@com.novi.serde.Unsigned Byte> userSignature;

    public ZkLoginSignatureData(ZkProof proofResponse, String maxEpoch, List<@com.novi.serde.Unsigned Byte> userSignature) {
        this.inputs = proofResponse;
        this.maxEpoch = maxEpoch;
        this.userSignature = userSignature;
    }

    public void serialize(com.novi.serde.Serializer serializer) throws com.novi.serde.SerializationError {
        serializer.increase_container_depth();
        inputs.serialize(serializer);
        serializer.serialize_u64(Long.parseLong(maxEpoch));
        TraitHelpers.serialize_vector_u8(userSignature, serializer);
        serializer.decrease_container_depth();
    }

    public byte[] bcsSerialize() throws com.novi.serde.SerializationError {
        com.novi.serde.Serializer serializer = new com.novi.bcs.BcsSerializer();
        serialize(serializer);
        return serializer.get_bytes();
    }

    public static ZkLoginSignatureData deserialize(com.novi.serde.Deserializer deserializer) throws com.novi.serde.DeserializationError {
        deserializer.increase_container_depth();
        Builder builder = new Builder();
        builder.inputs = ZkProof.deserialize(deserializer);
        builder.maxEpoch = String.valueOf(deserializer.deserialize_u64());
        builder.userSignature = TraitHelpers.deserialize_vector_u8(deserializer);
        deserializer.decrease_container_depth();
        return builder.build();
    }

    public static ZkLoginSignatureData bcsDeserialize(byte[] input) throws com.novi.serde.DeserializationError {
        if (input == null) {
            throw new com.novi.serde.DeserializationError("Cannot deserialize null array");
        }
        com.novi.serde.Deserializer deserializer = new com.novi.bcs.BcsDeserializer(input);
        ZkLoginSignatureData value = deserialize(deserializer);
        if (deserializer.get_buffer_offset() < input.length) {
            throw new com.novi.serde.DeserializationError("Some input bytes were not read");
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZkLoginSignatureData that = (ZkLoginSignatureData) o;
        return Objects.equals(inputs, that.inputs) && Objects.equals(maxEpoch, that.maxEpoch) && Objects.equals(userSignature, that.userSignature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputs, maxEpoch, userSignature);
    }

    public static final class Builder {
        public ZkProof inputs;
        public String maxEpoch;
        public List<@com.novi.serde.Unsigned Byte> userSignature;

        public ZkLoginSignatureData build() {
            return new ZkLoginSignatureData(inputs, maxEpoch, userSignature);
        }
    }
}
