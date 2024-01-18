syntax = "proto3"
package jni_to_proto


message Request{

}

public interface ${jniCallback} {
void onResult(int errorCode<#list unFoldParams as param>, ${param.javaNativeType} arg${param.index}</#list>);
}
