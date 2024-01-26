package io.sui.models.enoki;

public class BaseEnokiResponse<T> {

    private T data;

    public BaseEnokiResponse() {
    }

    public BaseEnokiResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
