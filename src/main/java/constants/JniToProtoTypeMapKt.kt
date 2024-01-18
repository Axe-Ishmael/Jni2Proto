package constants

class JniToProtoTypeMapKt {
    companion object{

        val convertMap = HashMap<String,String>().apply {
            this.put("String","string")
            this.put("int","uint32")
            this.put("long","uint64")
            this.put("boolean","bool")
            this.put("byte[]","bytes")
        }





        /**
         * JniType : ProtoType
         */
        fun getJni2ProtoMap():HashMap<String,String>{

            return convertMap

        }

        fun convertToProtoType(paramType: String): String {
            val hashMap = getJni2ProtoMap()
            val getValue = hashMap[paramType]

            if (getValue != null) {
                return getValue
            }

            return extractSubstringAfterLastDot(paramType)
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