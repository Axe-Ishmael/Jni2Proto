import CodeParserUtil.FilePathFindUtil;
import CodeParserUtil.JavaFileCodeParser;
import CodeParserUtil.WriteProtoFileUtil;
import com.github.javaparser.JavaParser;
import model.MethodSourceInfoDetail;

import java.util.HashSet;
import java.util.List;

public class MainHandler {

    public static void main(String[] args) {

        String filePath = "/Users/axeishmael/AndroidStudioProjects/wxwork_ios/src/android_submodule/library/lib_weworkservice/src/main/java/com/tencent/wework/foundation/logic/GrandProfileService.java";

        String searchPath = "/Users/axeishmael/StudioProjects/api_proto/src/mobile_framework";
        JavaFileCodeParser codeParser = new JavaFileCodeParser();

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
