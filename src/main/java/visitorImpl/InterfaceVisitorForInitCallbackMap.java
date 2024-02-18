package visitorImpl;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import constants.JniToProtoTypeMapKt;
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

    private HashMap<String, List<ParamTypePair>> interfacesMap; //InterfaceName : <paramType:paramName>

    private List<String> classNames;

    private HashSet<String> importItems;//哪些pb message需要从别的地方import进来

    public InterfaceVisitorForInitCallbackMap(List<String>classNames,HashMap<String, List<ParamTypePair>> map,HashSet<String> importItems) { //
        this.interfacesMap = map;
        this.classNames = classNames;
        this.importItems = importItems;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        if (n.isInterface()){

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

                String paramTypeConvert = JniToProtoTypeMapKt.Companion.convertJniTypeToProtoType(paramType);
                String mainType = JniToProtoTypeMapKt.Companion.getMainType(paramTypeConvert);

                if (!JniToProtoTypeMapKt.Companion.getJniType2ProtoTypeMap().containsKey(mainType)){
//                    importItems.add(JniToProtoTypeMapKt.Companion.extractSubstringAfterLastDot(mainType));//init callback 不需要把ImportedItem加进来，因为不一定会用
                }
                list.add(new ParamTypePair(paramTypeConvert,paramName));
            }

            interfacesMap.put(interfaceName,list);

        }else {

        }

    }




}
