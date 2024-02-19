import CodeParserUtil.FilePathFindUtil;
import CodeParserUtil.JavaFileCodeParser;
import CodeParserUtil.WriteProtoFileUtil;
import com.github.javaparser.JavaParser;
import model.MethodSourceInfoDetail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainHandler {

    public static void main(String[] args) throws IOException {

        //待翻译为Proto的JNI文件位置
        String filePath = "/Users/axeishmael/AndroidStudioProjects/wxwork_ios/src/android_submodule/library/lib_weworkservice/src/main/java/com/tencent/wework/foundation/logic/GovernService.java";

        //api_proto工程中
        String searchPath = "/Users/axeishmael/StudioProjects/api_proto/src/mobile_framework";

        String commonCallbacksDirectoryPath = "/Users/axeishmael/AndroidStudioProjects/wxwork_ios/src/android_submodule/library/lib_weworkservice/src/main/java/com/tencent/wework/foundation/callback";
        String generateCallbackDirectoryPath = "/Users/axeishmael/AndroidStudioProjects/wxwork_ios/src/android_submodule/library/lib_weworkservice/src/main/java/com/tencent/wework/foundation/generateCallback";

        List<String> initCallbacksDirectoryPath = new ArrayList<>();

        initCallbacksDirectoryPath.add(commonCallbacksDirectoryPath);
        initCallbacksDirectoryPath.add(generateCallbackDirectoryPath);

        JavaFileCodeParser codeParser = new JavaFileCodeParser();

        for (String callbackDirectoryPath: initCallbacksDirectoryPath){
            codeParser.initCallbackMap(callbackDirectoryPath);
        }
        codeParser.buildParsedInfoModel(filePath);

        WriteProtoFileUtil.writeToProtoFile(searchPath,codeParser.classSourceInfoDetail);

        List<MethodSourceInfoDetail> funcList = codeParser.methodSourceInfoDetailList;
        List<String> classNames = codeParser.classNames;
        HashSet<String> set = codeParser.importItems;

        FilePathFindUtil.findImportPathOfMessage(searchPath,"IDcardOcrReq");



        System.out.println("Size:"+codeParser.methodSourceInfoDetailList.size());
        System.out.printf("ClassName:"+codeParser.classNames);




    }


}
