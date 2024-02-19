package visitorImpl;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import constants.JniToProtoTypeMapKt;
import model.FanxinInterfaceParamTypeInfo;
import model.MethodSourceInfoDetail;
import model.ParamTypePair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 处理Class中的函数
 */
public class MethodVisitor extends VoidVisitorAdapter<Void> {
    private HashMap<String,List<ParamTypePair>> callbackMap; //已经经过扫描得到的Callback函数Info集合  InterfaceName : <paramType:paramName>

    private HashMap<String, FanxinInterfaceParamTypeInfo> fanxinCallbackMap; //已经经过扫描得到的使用泛型的Interface Info集合

    private List<MethodSourceInfoDetail> methodSourceInfoDetailList;//由外面传入

    private HashSet<String> importItems;

    public MethodVisitor(HashMap<String, List<ParamTypePair>> callbackMap, List<MethodSourceInfoDetail> methodSourceInfoDetailList,HashSet<String> importItems,HashMap<String, FanxinInterfaceParamTypeInfo> fanxinCallbackMap) {
        this.callbackMap = callbackMap;
        this.methodSourceInfoDetailList = methodSourceInfoDetailList;
        this.importItems = importItems;
        this.fanxinCallbackMap = fanxinCallbackMap;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {

        if (n.isInterface()){
            return;
        }

        //todo 需要过滤到method中包含“native”的方法吗？ 已全部过滤
        // 遍历类中的所有方法
        for (MethodDeclaration method : n.getMethods()) {
            String methodName = method.getNameAsString();

            if (methodName.contains("native")||methodName.contains("Native")||methodName.contains("finalize")||methodName.equals("getService")){

                continue;
            }

            MethodSourceInfoDetail methodSourceInfoDetail = new MethodSourceInfoDetail();

            methodSourceInfoDetail.setMethodName(methodName);


            // 遍历方法的参数
            for (Parameter parameter : method.getParameters()) {
                String paramType = parameter.getTypeAsString();
                String paramName = parameter.getNameAsString();

                String paramTypeConvert = JniToProtoTypeMapKt.Companion.convertJniTypeToProtoType(paramType);

                String callbackType = JniToProtoTypeMapKt.Companion.getCallbackType(paramType);

                if (callbackMap.containsKey(callbackType)){
                    findImportItemInParamTypePairListOfCallback(callbackMap.get(paramType));
                    methodSourceInfoDetail.setResponseInfo(callbackMap.get(paramType));
                }else if (fanxinCallbackMap.containsKey(callbackType)){
                    List<String> appliedFanxinTypeList = JniToProtoTypeMapKt.Companion.extractAppliedFanxinType(paramType);//实际使用的泛型类型
                    if (appliedFanxinTypeList != null){
                        FanxinInterfaceParamTypeInfo info = fanxinCallbackMap.get(callbackType);
                        List<ParamTypePair> paramTypePairList = info.getParamTypeList();
                        List<String> callbackFanxinTypeList = info.getStatedFanxinTypeList();

                        if (appliedFanxinTypeList.size() != callbackFanxinTypeList.size()){
                            throw new RuntimeException("Error: Fanxin Interface TypeList Size not match!");
                        }

                        int size = appliedFanxinTypeList.size();


                        for (int i = 0;i<size;i++){
                            String statedFanxinType = callbackFanxinTypeList.get(i);
                            String appliedFanxinType = appliedFanxinTypeList.get(i);

                            ParamTypePair statedFanxinTypePair =  JniToProtoTypeMapKt.Companion.findCorrectStatedFanxinTypePair(paramTypePairList,statedFanxinType);

                            String convertTypeStr = JniToProtoTypeMapKt.Companion.convertJniTypeToProtoType(appliedFanxinType);


                            if (statedFanxinTypePair != null){
                                statedFanxinTypePair.setParamType(convertTypeStr);
                            }

                        }


                        findImportItemInParamTypePairListOfCallback(fanxinCallbackMap.get(callbackType).getParamTypeList());
                        methodSourceInfoDetail.setResponseInfo(fanxinCallbackMap.get(callbackType).getParamTypeList());


                    }



                }else {
                    String mainType = JniToProtoTypeMapKt.Companion.getMainType(paramTypeConvert);
                    if(!JniToProtoTypeMapKt.Companion.getBasicProtoTypeMap().containsKey(mainType)){
                        importItems.add(mainType);
                    }
                    methodSourceInfoDetail.getRequestInfo().add(new ParamTypePair(paramTypeConvert,paramName));
                }

            }

            methodSourceInfoDetailList.add(methodSourceInfoDetail);
        }


        super.visit(n, arg);
    }


    /**
     * 找出Callback的ParamTypePairList中需要Import的item
     */
    private void findImportItemInParamTypePairListOfCallback(List<ParamTypePair> list){
        if (list == null || list.isEmpty()){
            return;
        }

        for (ParamTypePair pair : list){
            String mainType = JniToProtoTypeMapKt.Companion.getMainType(pair.getParamType());

            if(!JniToProtoTypeMapKt.Companion.getBasicProtoTypeMap().containsKey(mainType)){
                importItems.add(mainType);
            }

        }


    }


}
