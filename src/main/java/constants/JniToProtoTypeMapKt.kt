package constants

class JniToProtoTypeMapKt {
    companion object{

        val convertMap = HashMap<String,String>().apply {
            this.put("String","optional string")
            this.put("int","optional uint32")
            this.put("long","optional uint64")
            this.put("boolean","optional bool")
            this.put("float","optional float")
            this.put("double","optional double")
            this.put("char","optional string")
            this.put("byte[]","optional bytes")
        }

        val basicTypeMap = HashMap<String,String>().apply {
            this.put("String","string")
            this.put("int","uint32")
            this.put("long","uint64")
            this.put("boolean","bool")
            this.put("float","float")
            this.put("double","double")
            this.put("char","string")
        }





        /**
         * JniType : ProtoType
         */
        fun getJniType2ProtoTypeMap():HashMap<String,String>{

            return convertMap

        }

        /**
         * JNI type转化为对应的ProtoType
         * optional XXX 或者 repeated XXX
         */
        fun convertJniTypeToProtoType(paramType: String): String {
            val hashMap = getJniType2ProtoTypeMap()
            val getValue = hashMap[paramType]

            if (getValue != null) {
                return getValue
            }

            if (paramType.contains("List<")){
                val fanxin = extractListFanxinType(paramType)
                if (fanxin != ""){
                    return "repeated "+ checkTypeInProto(fanxin)
                }else{
                    throw RuntimeException("Error: List Fanxin Type is null!")
                }
            }else if (paramType.contains("[")){
                val arrayType = extractArrayType(paramType)
                if (arrayType != ""){
                    return "repeated "+ checkTypeInProto(arrayType)
                }else{
                    throw RuntimeException("Error: Array Type is null!")
                }
            }

            return "optional "+extractSubstringAfterLastDot(paramType)
        }

        /**
         * 找到Jni Type在 Proto中对应的Type
         */
        fun checkTypeInProto(type:String):String{

            if (basicTypeMap.containsKey(type)){
                return basicTypeMap[type]?:""
            }else{
                return extractSubstringAfterLastDot(type)
            }

        }


        fun extractListFanxinType(typeString:String):String{
            val start = typeString.indexOf('<') + 1
            val end = typeString.lastIndexOf('>')

            if (start != 0 && end != -1) {
                val genericType = typeString.substring(start, end)
                return genericType
            }

            return ""

        }


        fun extractArrayType(typeString:String):String{
            val end = typeString.indexOf('[')

            if (end != -1) {
                val baseType = typeString.substring(0, end)
                return baseType
            }

            return ""

        }



        /**
         * 提取字符串中最后一个点之后的部分。
         *
         * @param str 输入字符串
         * @return 最后一个点之后的字符串，如果没有点则返回 str
         */
        fun extractSubstringAfterLastDot(str: String): String {
            val lastDotIndex = str.lastIndexOf('.')
            val ret =  if (lastDotIndex != -1) {
                str.substring(lastDotIndex + 1)
            } else {
                str.toString()
            }


            return ret
        }
    }
}