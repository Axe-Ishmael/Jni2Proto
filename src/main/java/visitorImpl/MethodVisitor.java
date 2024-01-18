package visitorImpl;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import constants.JniToProtoTypeMapKt;
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

    private List<MethodSourceInfoDetail> methodSourceInfoDetailList;//由外面传入

    private  HashSet<String> importItems;

    public MethodVisitor(HashMap<String, List<ParamTypePair>> callbackMap, List<MethodSourceInfoDetail> methodSourceInfoDetailList,HashSet<String> importItems) {
        this.callbackMap = callbackMap;
        this.methodSourceInfoDetailList = methodSourceInfoDetailList;
        this.importItems = importItems;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {

        if (n.isInterface()){
            return;
        }

        //todo 需要过滤到method中包含“native”的方法吗？
        // 遍历类中的所有方法
        for (MethodDeclaration method : n.getMethods()) {
            String methodName = method.getNameAsString();

            MethodSourceInfoDetail methodSourceInfoDetail = new MethodSourceInfoDetail();

            methodSourceInfoDetail.setMethodName(methodName);


            // 遍历方法的参数
            for (Parameter parameter : method.getParameters()) {
                String paramType = parameter.getTypeAsString();
                String paramName = parameter.getNameAsString();

                String paramTypeConvert = JniToProtoTypeMapKt.Companion.convertToProtoType(paramType,null);

                if (callbackMap.containsKey(paramTypeConvert)){
                    methodSourceInfoDetail.setResponseInfo(callbackMap.get(paramTypeConvert));
                }else {
                    if(!JniToProtoTypeMapKt.Companion.getJni2ProtoMap().containsKey(paramType)){
                        importItems.add(paramTypeConvert);
                    }
                    methodSourceInfoDetail.getRequestInfo().add(new ParamTypePair(paramTypeConvert,paramName));
                }

            }

            methodSourceInfoDetailList.add(methodSourceInfoDetail);
        }


        super.visit(n, arg);
    }


}
