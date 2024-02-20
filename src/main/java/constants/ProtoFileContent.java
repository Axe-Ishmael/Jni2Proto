package constants;

import model.ParamTypePair;

import java.util.ArrayList;
import java.util.List;

public class ProtoFileContent {

    public static String getProtoHeader(String className){
        String header = "syntax = \"proto2\";\n" + "package "+className+";";
        return header;
    }

    public static String getRequestMessageHeader(String methodName){
        String requestHeader = "message "+methodName+"Request\n{\n" ;
        return requestHeader;
    }


    public static String getResponseMessageHeader(String methodName){
        String responseHeader = "message "+methodName+"Response\n{\n" ;
        return responseHeader;
    }


    public static List<String> getMessageBodyContent(List<ParamTypePair> pairs){
        ArrayList<String> bodyContent = new ArrayList<>();

        if (pairs.isEmpty()){
            return bodyContent;
        }

        int size = pairs.size();

        for (int index = 0; index < size; index++){

            bodyContent.add(getSingleLineMessageContent(pairs.get(index),index));

        }

        return bodyContent;
    }


    public static String getSingleLineMessageContent(ParamTypePair pair,int index){

        String content = "\t"+pair.getParamType()+" "+pair.getParamName()+" = "+(index+1) +";\n";

        return content;

    }


    public static String getRpcServiceHeader(String className){


        return "service "+className+" {"+"\n";
    }
}





















