package com.mpakbaz.accountManager.http.payloads;

public class ErrorPayload {
    private Object data;

    private String message;

    public ErrorPayload(Object data, String message) {
        this.data = data;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
