
package com.bd.esellers.userAuth.userAuthModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Token implements Serializable {

    @SerializedName("headers")
    @Expose
    private Headers headers;
    @SerializedName("original")
    @Expose
    private Original original;
    @SerializedName("exception")
    @Expose
    private Object exception;

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }

    public Original getOriginal() {
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
    }

    public Object getException() {
        return exception;
    }

    public void setException(Object exception) {
        this.exception = exception;
    }

}
