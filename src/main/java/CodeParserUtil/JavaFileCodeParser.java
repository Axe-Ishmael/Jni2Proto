package CodeParserUtil;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import visitorImpl.InterfaceVisitorForBuildMap;

import java.io.File;
import java.util.HashMap;

public class JavaFileCodeParser {
    private static final JavaFileCodeParser parser = new JavaFileCodeParser();

    private final JavaParser javaParser;
    public  final HashMap<String, HashMap<String,String>> callbackMap = new HashMap<>();//InterfaceName : <paramType:paramName>


    public JavaFileCodeParser() {
        this.javaParser = new JavaParser();
    }

    public static JavaFileCodeParser getInstance(){return parser;}

    public void buildParsedInfoModel(String filePath){
        if (filePath == null || filePath.isEmpty()){
            throw new RuntimeException("File path is null or empty");
        }

        try {
            File file = new File(filePath);
            ParseResult<CompilationUnit> parseResult = javaParser.parse(file);
            CompilationUnit cu = parseResult.getResult().get();
            cu.accept(new InterfaceVisitorForBuildMap(callbackMap),null);


        }catch (Exception e){

        }

    }









}
