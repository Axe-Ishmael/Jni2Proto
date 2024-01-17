import CodeParserUtil.JavaFileCodeParser;
import com.github.javaparser.JavaParser;
import model.MethodSourceInfoDetail;

import java.util.List;

public class MainHandler {

    public static void main(String[] args) {

        String filePath = "/Users/axeishmael/AndroidStudioProjects/wxwork_ios/src/android_submodule/autotool/output/GrandProfileService/GrandProfileService.java";

        JavaFileCodeParser codeParser = new JavaFileCodeParser();

        codeParser.buildParsedInfoModel(filePath);

        List<MethodSourceInfoDetail> list = codeParser.methodSourceInfoDetailList;



        System.out.println("Size:"+codeParser.methodSourceInfoDetailList.size());
        System.out.printf("ClassName:"+codeParser.classNames);




    }


}
