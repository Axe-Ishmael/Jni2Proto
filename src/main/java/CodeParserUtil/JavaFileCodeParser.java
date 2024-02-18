package CodeParserUtil;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import model.ClassSourceInfoDetail;
import model.MethodSourceInfoDetail;
import model.ParamTypePair;
import visitorImpl.InterfaceVisitorForBuildMap;
import visitorImpl.InterfaceVisitorForInitCallbackMap;
import visitorImpl.MethodVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class JavaFileCodeParser {
    private static final JavaFileCodeParser parser = new JavaFileCodeParser();

    private final JavaParser javaParser;
    public  final HashMap<String, List<ParamTypePair>> callbackMap = new HashMap<>();//InterfaceName : <paramType:paramName>
    public List<MethodSourceInfoDetail> methodSourceInfoDetailList = new ArrayList<>();

    public List<String> classNames = new ArrayList<>();

    public HashSet<String> importItems = new HashSet<>();

    public ClassSourceInfoDetail classSourceInfoDetail;


    public JavaFileCodeParser() {
        this.javaParser = new JavaParser();
    }


    /**
     * 确保 initCallbackMap 方法在之前已经被调用
     * @param filePath
     */
    public void buildParsedInfoModel(String filePath){
        if (filePath == null || filePath.isEmpty()){
            throw new RuntimeException("File path is null or empty");
        }

        try {
            File file = new File(filePath);
            ParseResult<CompilationUnit> parseResult = javaParser.parse(file);
            CompilationUnit cu = parseResult.getResult().get();
            cu.accept(new InterfaceVisitorForBuildMap(classNames,callbackMap,importItems),null);
            cu.accept(new MethodVisitor(callbackMap,methodSourceInfoDetailList,importItems),null);
            classSourceInfoDetail = new ClassSourceInfoDetail(classNames,methodSourceInfoDetailList,importItems);

            System.out.printf("");
        }catch (Exception e){

        }

    }


    /**
     * 必须在 buildParsedInfoModel 函数之前调用
     * @param commonCallbacksDirectoryPath
     * @throws IOException
     */
    public void initCallbackMap(String commonCallbacksDirectoryPath) throws IOException {
        try {
            Files.walk(Paths.get(commonCallbacksDirectoryPath))
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .map(Path::toFile)
                    .forEach(this::parseInterfaceFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private  void parseInterfaceFile(File file) {
        try {
            // Parse the file
            ParseResult<CompilationUnit> parseResult = javaParser.parse(file);

            if (parseResult.isSuccessful() && parseResult.getResult().isPresent()) {
                CompilationUnit compilationUnit = parseResult.getResult().get();

                compilationUnit.accept(new InterfaceVisitorForInitCallbackMap(classNames,callbackMap,importItems), null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
