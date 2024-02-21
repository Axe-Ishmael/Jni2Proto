package CodeParserUtil;

import constants.ProtoFileContent;
import model.ClassSourceInfoDetail;
import model.MethodSourceInfoDetail;
import model.ParamTypePair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class WriteProtoFileUtil {

    public static void writeToProtoFile(String searchPath,ClassSourceInfoDetail classSourceInfoDetail){

        String className = "";

        if (classSourceInfoDetail.classNames != null && !classSourceInfoDetail.classNames.isEmpty()){
            className = classSourceInfoDetail.classNames.get(0);
        }

        List<MethodSourceInfoDetail> funcInfoList = classSourceInfoDetail.funcInfoList;

        ////////////////////////////////////////////////////////////////////////////////////////////

        String header = ProtoFileContent.getProtoHeader(className)+"\n";

        StringBuilder bodyMessgaeContent = new StringBuilder();

        for (MethodSourceInfoDetail detail:funcInfoList){
            String requestMessage = generateRequestMessage(detail);
            String responseMessage = generateResponseMessage(detail);
            bodyMessgaeContent.append(requestMessage).append(responseMessage).append("\n");
        }

        bodyMessgaeContent.append("message EmptyReq {\n" + "\n" + "}\n\n");
        bodyMessgaeContent.append("message EmptyRsp {\n" +
                "    optional uint32 errCode = 1;\n" +
                "    optional string errorMsg = 2;\n" +
                "}\n\n");


        String rpcServiceHeader = ProtoFileContent.getRpcServiceHeader(className);

        StringBuilder rpcMethodContent = new StringBuilder();

        for (MethodSourceInfoDetail detail:funcInfoList){
            rpcMethodContent.append(generateRpcMethod(detail));
        }


        String rpcServiceEnd = " }\n";


        //todo import

        HashSet<String> importItems = classSourceInfoDetail.importItems;

        StringBuilder importSentences = new StringBuilder();
        HashSet<String> importSentencesSet = new HashSet<>();//去重

        for (String item:importItems){
            List<String> sent = FilePathFindUtil.findImportPathOfMessage(searchPath,item);

            if (Objects.equals(sent.get(0), "")){
                throw new RuntimeException(item+" is not found in "+searchPath+".");
            }

            if (sent.size()>1 ){
                throw new RuntimeException(item+" is not found in "+searchPath+"." +"Error messgae: "+sent.get(1));
            }
            if (!sent.get(0).isEmpty()){
                String str = sent.get(0);
                String key = "mobile_framework/";

                int index = str.indexOf(key);
                if (index != -1) {
                    // 截取 "mobile_framework/" 之后的部分，包括 "mobile_framework/" 本身
                    str = str.substring(index+key.length());
                }
                importSentencesSet.add(str);
            }

        }

        for (String item:importSentencesSet){
            importSentences.append("import ").append("\"").append(item).append("\";").append(System.lineSeparator());
        }
        importSentences.append("\n");


        String WholeContent = header + importSentences +bodyMessgaeContent + rpcServiceHeader + rpcMethodContent + rpcServiceEnd;

        writeContentToFile(className,WholeContent);


        System.out.printf("");



    }

    public static void writeContentToFile(String className,String WholeContent){
        // 定义文件路径
        String directoryPath = "src/main/java/Output"; // 替换为实际的目录路径
        String fileName = className + ".proto";
        File file = new File(directoryPath, fileName);

        // 确保目录存在
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // 写入文件内容
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) { // false 表示不追加，覆盖文件
            writer.write(WholeContent);
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public static String generateRequestMessage(MethodSourceInfoDetail methodSourceInfoDetail){

        if (methodSourceInfoDetail.getRequestInfo().isEmpty()){
            return "";
        }
        String methodName = methodSourceInfoDetail.getMethodName();

        List<ParamTypePair> requestParamList = methodSourceInfoDetail.getRequestInfo();

        String requestMessageHeader = ProtoFileContent.getRequestMessageHeader(methodName);

        List<String> bodyContents = ProtoFileContent.getMessageBodyContent(requestParamList);

        StringBuilder bodyContent = new StringBuilder();

        for (String str:bodyContents){
            bodyContent.append(str);
        }

        String requestMessageEnd = "}\n";

        String ret = requestMessageHeader+bodyContent+requestMessageEnd;

        return ret;
    }


    public static String generateResponseMessage(MethodSourceInfoDetail methodSourceInfoDetail){

        if (methodSourceInfoDetail.getResponseInfo().isEmpty()){
            return "";
        }

        String methodName = methodSourceInfoDetail.getMethodName();

        List<ParamTypePair> responseParamList = methodSourceInfoDetail.getResponseInfo();

        String responseMessageHeader = ProtoFileContent.getResponseMessageHeader(methodName);

        List<String> bodyContents = ProtoFileContent.getMessageBodyContent(responseParamList);

        StringBuilder bodyContent = new StringBuilder();

        for (String str:bodyContents){
            bodyContent.append(str);
        }

        String responseMessageEnd = "}\n";

        String ret = responseMessageHeader+bodyContent+responseMessageEnd;

        return ret;
    }


    public static String generateRpcMethod(MethodSourceInfoDetail methodSourceInfoDetail){

        String methodName = methodSourceInfoDetail.getMethodName();

        String methodNameRequest = "";

        String methodNameResponse = "";

        if (!methodSourceInfoDetail.getRequestInfo().isEmpty()){
            methodNameRequest = methodName+"Request";
        }else {
            methodNameRequest = "EmptyReq";
        }

        if (!methodSourceInfoDetail.getResponseInfo().isEmpty()){
            methodNameResponse = methodName+"Response";
        }else {
            methodNameResponse = "EmptyRsp";
        }

        String content = "\trpc "+methodName+"("+methodNameRequest+")"+" returns "+"("+methodNameResponse+")"+";\n";

        return content;
    }
}
