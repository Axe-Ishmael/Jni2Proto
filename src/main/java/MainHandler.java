import CodeParserUtil.JavaFileCodeParser;
import CodeParserUtil.WriteProtoFileUtil;
import com.github.javaparser.JavaParser;
import model.MethodSourceInfoDetail;

import java.util.HashSet;
import java.util.List;

public class MainHandler {

    public static void main(String[] args) {

        String filePath = "/Users/axeishmael/AndroidStudioProjects/wxwork_ios/src/android_submodule/autotool/output/GrandProfileService/GrandProfileService.java";

        JavaFileCodeParser codeParser = new JavaFileCodeParser();

        codeParser.buildParsedInfoModel(filePath);

        WriteProtoFileUtil.writeToProtoFile(codeParser.classSourceInfoDetail);

        List<MethodSourceInfoDetail> funcList = codeParser.methodSourceInfoDetailList;
        List<String> classNames = codeParser.classNames;
        HashSet<String> set = codeParser.importItems;



        System.out.println("Size:"+codeParser.methodSourceInfoDetailList.size());
        System.out.printf("ClassName:"+codeParser.classNames);




    }


}
