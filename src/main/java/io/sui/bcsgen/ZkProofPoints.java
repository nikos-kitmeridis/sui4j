package io.sui.bcsgen;

import java.util.List;
import java.util.Objects;

public class ZkProofPoints {
    public List<String> a;
    public List<List<String>> b;
    public List<String> c;

    public ZkProofPoints() {
    }

    public ZkProofPoints(List<String> a, List<List<String>> b, List<String> c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public void serialize(com.novi.serde.Serializer serializer) throws com.novi.serde.SerializationError {
        serializer.increase_container_depth();
        TraitHelpers.serialize_vector_str(a, serializer);
        TraitHelpers.serialize_vector_vector_str(b, serializer);
        TraitHelpers.serialize_vector_str(c, serializer);
        serializer.decrease_container_depth();
    }

    public byte[] bcsSerialize() throws com.novi.serde.SerializationError {
        com.novi.serde.Serializer serializer = new com.novi.bcs.BcsSerializer();
        serialize(serializer);
        return serializer.get_bytes();
    }

    public static ZkProofPoints deserialize(com.novi.serde.Deserializer deserializer) throws com.novi.serde.DeserializationError {
        deserializer.increase_container_depth();
        Builder builder = new Builder();
        builder.a = TraitHelpers.deserialize_vector_str(deserializer);
        builder.b = TraitHelpers.deserialize_vector_vector_str(deserializer);
        builder.c = TraitHelpers.deserialize_vector_str(deserializer);
        deserializer.decrease_container_depth();
        return builder.build();
    }

    public static ZkProofPoints bcsDeserialize(byte[] input) throws com.novi.serde.DeserializationError {
        if (input == null) {
            throw new com.novi.serde.DeserializationError("Cannot deserialize null array");
        }
        com.novi.serde.Deserializer deserializer = new com.novi.bcs.BcsDeserializer(input);
        ZkProofPoints value = deserialize(deserializer);
        if (deserializer.get_buffer_offset() < input.length) {
            throw new com.novi.serde.DeserializationError("Some input bytes were not read");
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZkProofPoints that = (ZkProofPoints) o;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b) && Objects.equals(c, that.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }

    public static final class Builder {
        public List<String> a;
        public List<List<String>> b;
        public List<String> c;

        public ZkProofPoints build() {
            return new ZkProofPoints(a, b, c);
        }
    }
}
