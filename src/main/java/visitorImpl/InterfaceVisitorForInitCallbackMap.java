package visitorImpl;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import constants.JniToProtoTypeMapKt;
import model.FanxinInterfaceParamTypeInfo;
import model.ParamTypePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 初始化callbackMap
 * 建立对library/lib_weworkservice/src/main/java/com/tencent/wework/foundation/callback目录下，所有Interface的索引
 */
public class InterfaceVisitorForInitCallbackMap extends VoidVisitorAdapter<Void> {

    private HashMap<String, List<ParamTypePair>> interfacesMap; //用于记录普通Interface(没使用泛型)  InterfaceName : <paramType:paramName>

    private HashMap<String, FanxinInterfaceParamTypeInfo> fanxinInterfacesMap; // 用于记录使用了泛型的Interface

    private List<String> classNames;

    private HashSet<String> importItems;//哪些pb message需要从别的地方import进来

    public InterfaceVisitorForInitCallbackMap(List<String>classNames,HashMap<String, List<ParamTypePair>> interfacesMap,HashSet<String> importItems,HashMap<String, FanxinInterfaceParamTypeInfo> fanxinInterfacesMap) { //
        this.classNames = classNames;
        this.interfacesMap = interfacesMap;
        this.importItems = importItems;
        this.fanxinInterfacesMap = fanxinInterfacesMap;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        if (n.isInterface()){


            if (!n.getTypeParameters().isEmpty()){//说明该Interface带泛型
                List<String> fanxinTypeList = new ArrayList<>();

                n.getTypeParameters().forEach(typeParameter -> {
                    System.out.printf(typeParameter.getNameAsString());
                    fanxinTypeList.add(typeParameter.getNameAsString());
                });


                String interfaceName = n.getNameAsString();
                List<MethodDeclaration> methodList =  n.getMethods();
                if (methodList == null || methodList.isEmpty()){
                    throw new RuntimeException("The method list of Interface is empty");
                }

                List<ParamTypePair> list = new ArrayList<>();
                MethodDeclaration methodDeclaration = methodList.get(0);//默认接口中只有一个方法


                for (Parameter parameter:methodDeclaration.getParameters()){
                    String paramName = parameter.getNameAsString();
                    String paramType = parameter.getTypeAsString();

                    List<String> paramTypeConvertList = JniToProtoTypeMapKt.Companion.convertJniTypeToProtoType(paramType);
                    for (String paramTypeConvert:paramTypeConvertList) {
                        String mainType = JniToProtoTypeMapKt.Companion.getMainType(paramTypeConvert);

                        if (!JniToProtoTypeMapKt.Companion.getJniType2ProtoTypeMap().containsKey(mainType)){
    //                    importItems.add(JniToProtoTypeMapKt.Companion.extractSubstringAfterLastDot(mainType));//init callback 不需要把ImportedItem加进来，因为不一定会用
                        }
                        list.add(new ParamTypePair(paramTypeConvert,paramName));
                    }
                }

                FanxinInterfaceParamTypeInfo info = new FanxinInterfaceParamTypeInfo(list,fanxinTypeList);

                fanxinInterfacesMap.put(interfaceName,info);


            }else {
                String interfaceName = n.getNameAsString();
                List<MethodDeclaration> methodList =  n.getMethods();
                if (methodList == null || methodList.isEmpty()){
                    throw new RuntimeException("The method list of Interface is empty");
                }

                List<ParamTypePair> list = new ArrayList<>();
                MethodDeclaration methodDeclaration = methodList.get(0);//默认接口中只有一个方法


                for (Parameter parameter:methodDeclaration.getParameters()){
                    String paramName = parameter.getNameAsString();
                    String paramType = parameter.getTypeAsString();

                    List<String> paramTypeConvertList = JniToProtoTypeMapKt.Companion.convertJniTypeToProtoType(paramType);
                    for (String paramTypeConvert : paramTypeConvertList){
                        String mainType = JniToProtoTypeMapKt.Companion.getMainType(paramTypeConvert);

                        if (!JniToProtoTypeMapKt.Companion.getJniType2ProtoTypeMap().containsKey(mainType)){
//                    importItems.add(JniToProtoTypeMapKt.Companion.extractSubstringAfterLastDot(mainType));//init callback 不需要把ImportedItem加进来，因为不一定会用
                        }
                        list.add(new ParamTypePair(paramTypeConvert,paramName));
                    }
                }

                interfacesMap.put(interfaceName,list);
            }



        }else {

        }

    }




}
