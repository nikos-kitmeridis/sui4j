package io.sui.models.enoki;

public class SubnameResponse {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SubnameResponse{" +
                "name='" + name + '\'' +
                '}';
    }
}