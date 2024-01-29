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
        serializer.serialize_u8((byte) (indexMod4 & 0xFF));
        serializer.decrease_container_depth();
    }

    public byte[] bcsSerialize() throws com.novi.serde.SerializationError {
        com.novi.serde.Serializer serializer = new com.novi.bcs.BcsSerializer();
        serialize(serializer);
        return serializer.get_bytes();
    }

    public static ZkIssBase64Details deserialize(com.novi.serde.Deserializer deserializer) throws com.novi.serde.DeserializationError {
        deserializer.increase_container_depth();
        Builder builder = new Builder();
        builder.value = deserializer.deserialize_str();
        builder.indexMod4 = deserializer.deserialize_u8();
        deserializer.decrease_container_depth();
        return builder.build();
    }

    public static ZkIssBase64Details bcsDeserialize(byte[] input) throws com.novi.serde.DeserializationError {
        if (input == null) {
            throw new com.novi.serde.DeserializationError("Cannot deserialize null array");
        }
        com.novi.serde.Deserializer deserializer = new com.novi.bcs.BcsDeserializer(input);
        ZkIssBase64Details value = deserialize(deserializer);
        if (deserializer.get_buffer_offset() < input.length) {
            throw new com.novi.serde.DeserializationError("Some input bytes were not read");
        }
        return value;
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

    public static final class Builder {
        public String value;
        public int indexMod4;

        public ZkIssBase64Details build() {
            return new ZkIssBase64Details(value, indexMod4);
        }
    }
}
