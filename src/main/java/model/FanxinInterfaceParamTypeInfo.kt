package model

/**
 * 用于记录使用泛型的Interface的信息
 *
 * statedFanxinTypeList: 记录声明的泛型类型 interface<T，X> -> 记录T和X
 */
data class FanxinInterfaceParamTypeInfo(var paramTypeList:List<ParamTypePair>, var statedFanxinTypeList:List<String>)
