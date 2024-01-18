package CodeParserUtil;

import constants.ProtoFileContent;
import model.ClassSourceInfoDetail;
import model.MethodSourceInfoDetail;
import model.ParamTypePair;

import java.util.List;

public class WriteProtoFileUtil {

    public static void writeToProtoFile(ClassSourceInfoDetail classSourceInfoDetail){

        String className = "";

        if (classSourceInfoDetail.classNames != null && !classSourceInfoDetail.classNames.isEmpty()){
            className = classSourceInfoDetail.classNames.get(0);
        }

        List<MethodSourceInfoDetail> funcInfoList = classSourceInfoDetail.funcInfoList;

        ////////////////////////////////////////////////////////////////////////////////////////////

        String header = ProtoFileContent.getProtoHeader()+"\n";

        String bodyMessgaeContent = "";

        for (MethodSourceInfoDetail detail:funcInfoList){
            String requestMessage = generateRequestMessage(detail);
            String responseMessage = generateResponseMessage(detail);
            bodyMessgaeContent += requestMessage + responseMessage;
        }


        String rpcServiceHeader = ProtoFileContent.getRpcServiceHeader(className);

        StringBuilder rpcMethodContent = new StringBuilder();

        for (MethodSourceInfoDetail detail:funcInfoList){
            rpcMethodContent.append(generateRpcMethod(detail));
        }


        String rpcServiceEnd = " }\n";

        String WholeContent = header+bodyMessgaeContent+rpcServiceHeader+rpcMethodContent+rpcServiceEnd;

        //todo import





        System.out.printf("");



    }

    public static String generateRequestMessage(MethodSourceInfoDetail methodSourceInfoDetail){

        if (methodSourceInfoDetail.getRequestInfo().size() == 0){
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

        if (methodSourceInfoDetail.getResponseInfo().size() == 0){
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

        String content = "\trpc "+methodName+"("+methodName+"Request"+")"+" returns "+"("+methodName+"Response"+") "+"{}\n";

        return content;
    }
}
