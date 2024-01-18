package model;

import java.util.List;

public class ClassSourceInfoDetail {

    public List<MethodSourceInfoDetail> funcInfoList;

    public List<String> classNames;

    public ClassSourceInfoDetail( List<String> className,List<MethodSourceInfoDetail> funcInfoList) {
        this.funcInfoList = funcInfoList;
        this.classNames = className;
    }
}
