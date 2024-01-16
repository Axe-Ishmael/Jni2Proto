package visitorImpl;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.HashMap;
import java.util.List;

public class InterfaceVisitorForBuildMap extends VoidVisitorAdapter<Void> {

    private HashMap<String, HashMap<String,String>> interfacesMap; //InterfaceName : <paramType:paramName>

    public InterfaceVisitorForBuildMap(HashMap<String, HashMap<String,String>> map) { //
        this.interfacesMap = map;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Void arg) {
        // 检查这是不是一个接口
        if (n.isInterface()) {


            String interfaceName = n.getNameAsString();
            List<MethodDeclaration> methodList =  n.getMethods();
            if (methodList == null || methodList.size() == 0){
                throw new RuntimeException("The method list of Interface is empty");
            }

            HashMap<String,String> paramInfo = new HashMap<>(); //  paramType : paramName


            MethodDeclaration methodDeclaration = methodList.get(0);//默认接口中只有一个方法

            for (Parameter parameter:methodDeclaration.getParameters()){
                String paramName = parameter.getNameAsString();
                String paramType = parameter.getTypeAsString();
                paramInfo.put(paramType,paramName);
            }

            interfacesMap.put(interfaceName,paramInfo);

        }

        super.visit(n,arg);
    }
}
