package io.sui.models.enoki;

import java.io.Serializable;

public class IssBase64Details implements Serializable {

    private String value;
    private int indexMod4;

    public IssBase64Details() {
    }

    public IssBase64Details(String value, int indexMod4) {
        this.value = value;
        this.indexMod4 = indexMod4;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndexMod4() {
        return indexMod4;
    }

    public void setIndexMod4(int indexMod4) {
        this.indexMod4 = indexMod4;
    }
}
