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
 * 处理Interface
 */
public class InterfaceVisitorForBuildMap extends VoidVisitorAdapter<Void> {

    private HashMap<String, List<ParamTypePair>> interfacesMap; //InterfaceName : <paramType:paramName>

    private List<String> classNames;

    private HashSet<String> importItems;//哪些pb message需要从别的地方import进来


    public InterfaceVisitorForBuildMap(List<String>classNames,HashMap<String, List<ParamTypePair>> map,HashSet<String> importItems) { //
        this.interfacesMap = map;
        this.classNames = classNames;
        this.importItems = importItems;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        // 检查这是不是一个接口
        if (n.isInterface()) {

            String interfaceName = n.getNameAsString();
            List<MethodDeclaration> methodList =  n.getMethods();
            if (methodList == null || methodList.isEmpty()){
                throw new RuntimeException("The method list of Interface is empty");
            }

            List<ParamTypePair> list = new ArrayList<>();

            //  paramType : paramName


            MethodDeclaration methodDeclaration = methodList.get(0);//默认接口中只有一个方法

            for (Parameter parameter:methodDeclaration.getParameters()){
                String paramName = parameter.getNameAsString();
                String paramType = parameter.getTypeAsString();
                if (!JniToProtoTypeMapKt.Companion.getJni2ProtoMap().containsKey(paramType)){
                    importItems.add(JniToProtoTypeMapKt.Companion.extractSubstringAfterLastDot(paramType,null));
                }
                paramType = JniToProtoTypeMapKt.Companion.convertToProtoType(paramType,null);
                list.add(new ParamTypePair(paramType,paramName));
            }

            interfacesMap.put(interfaceName,list);

        }else {
            classNames.add(n.getNameAsString());
        }

        super.visit(n,arg);
    }
}
