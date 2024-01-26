package io.sui.models.enoki;

import java.io.Serializable;

public class ZkLoginResponse implements Serializable {

    private String salt;
    private String address;

    public ZkLoginResponse() {
    }

    public ZkLoginResponse(String salt, String address) {
        this.salt = salt;
        this.address = address;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
