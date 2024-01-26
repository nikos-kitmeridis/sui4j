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
}
