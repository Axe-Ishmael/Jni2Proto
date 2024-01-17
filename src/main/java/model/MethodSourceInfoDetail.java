package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MethodSourceInfoDetail {

    private String methodName = "";
    private List<ParamTypePair> requestInfo = new ArrayList<>();//  <protoType : paramName> ->  uint64:abc

    private List<ParamTypePair>  responseInfo = new ArrayList<>();

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<ParamTypePair> getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(List<ParamTypePair> requestInfo) {
        this.requestInfo = requestInfo;
    }

    public List<ParamTypePair> getResponseInfo() {
        return responseInfo;
    }

    public void setResponseInfo(List<ParamTypePair> responseInfo) {
        this.responseInfo = responseInfo;
    }
}
