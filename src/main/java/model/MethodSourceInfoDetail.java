package model;

import java.util.HashMap;

public class MethodSourceInfoDetail {
    private HashMap<String,String> requestInfo = new HashMap<>(); // protoType : paramName ->  uint64:abc

    private HashMap<String,String> responseInfo = new HashMap<>();

    public HashMap<String, String> getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(HashMap<String, String> requestInfo) {
        this.requestInfo = requestInfo;
    }

    public HashMap<String, String> getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(HashMap<String, String> responseInfo) {
        this.responseInfo = responseInfo;
    }
}
