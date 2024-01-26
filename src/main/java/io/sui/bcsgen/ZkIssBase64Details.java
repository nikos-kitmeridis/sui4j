package io.sui.bcsgen;

import java.util.Objects;

public class ZkIssBase64Details {

    public String value;
    public int indexMod4;

    public ZkIssBase64Details(String value, int indexMod4) {
        this.value = value;
        this.indexMod4 = indexMod4;
    }

    public void serialize(com.novi.serde.Serializer serializer) throws com.novi.serde.SerializationError {
        serializer.increase_container_depth();
        serializer.serialize_str(value);
        serializer.serialize_i32(indexMod4);
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
        ZkIssBase64Details that = (ZkIssBase64Details) o;
        return indexMod4 == that.indexMod4 && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, indexMod4);
    }
}
