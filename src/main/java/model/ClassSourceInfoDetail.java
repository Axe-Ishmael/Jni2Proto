package model;

import java.util.HashSet;
import java.util.List;

public class ClassSourceInfoDetail {

    public List<MethodSourceInfoDetail> funcInfoList;

    public List<String> classNames;

    public HashSet<String> importItems;


    public ClassSourceInfoDetail(List<String> classNames,List<MethodSourceInfoDetail> funcInfoList, HashSet<String> importItems) {
        this.funcInfoList = funcInfoList;
        this.classNames = classNames;
        this.importItems = importItems;
    }
}
