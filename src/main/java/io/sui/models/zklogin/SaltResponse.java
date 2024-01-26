package io.sui.models.zklogin;

import java.util.Objects;

public class SaltResponse {

    private String salt;

    public SaltResponse() {
    }

    public SaltResponse(String salt) {
        this.salt = salt;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaltResponse that = (SaltResponse) o;
        return Objects.equals(salt, that.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salt);
    }

    @Override
    public String toString() {
        return "SaltResponse{" +
                "salt='" + salt + '\'' +
                '}';
    }
}
