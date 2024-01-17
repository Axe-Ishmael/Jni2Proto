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
import java.util.List;

public class MethodVisitor extends VoidVisitorAdapter<Void> {
    private HashMap<String,List<ParamTypePair>> callbackMap; //已经经过扫描得到的Callback函数Info集合  InterfaceName : <paramType:paramName>

    private List<MethodSourceInfoDetail> methodSourceInfoDetailList;//由外面传入

    public MethodVisitor(HashMap<String, List<ParamTypePair>> callbackMap, List<MethodSourceInfoDetail> methodSourceInfoDetailList) {
        this.callbackMap = callbackMap;
        this.methodSourceInfoDetailList = methodSourceInfoDetailList;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {

        if (n.isInterface()){
            return;
        }

        // 遍历类中的所有方法
        for (MethodDeclaration method : n.getMethods()) {
            String methodName = method.getNameAsString();

            MethodSourceInfoDetail methodSourceInfoDetail = new MethodSourceInfoDetail();

            methodSourceInfoDetail.setMethodName(methodName);


            // 遍历方法的参数
            for (Parameter parameter : method.getParameters()) {
                String paramType = parameter.getTypeAsString();
                String paramName = parameter.getNameAsString();

                paramType = JniToProtoTypeMapKt.Companion.convertToProtoType(paramType);

                if (callbackMap.containsKey(paramType)){
                    methodSourceInfoDetail.setResponseInfo(callbackMap.get(paramType));
                }else {
                    methodSourceInfoDetail.getRequestInfo().add(new ParamTypePair(paramType,paramName));
                }

            }

            methodSourceInfoDetailList.add(methodSourceInfoDetail);
        }


        super.visit(n, arg);
    }


}
