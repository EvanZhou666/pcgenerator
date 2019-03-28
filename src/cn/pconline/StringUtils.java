package cn.pconline;

public class StringUtils {
	/**
     * 将下划线转为大驼峰
     * @param str   目标字符串
     * @return  变为大驼峰的字符串
     */
    public static String underlineToBigCamel(String str){
    	//substring 是左闭右开的
//    	先去掉表前缀
    	String tablename = str.substring(str.indexOf("_")+1);
        return underlineToSmallCamel(tablename.toUpperCase().substring(0,1)+tablename.substring(1));
    }

    /**
     * 将下划线转为小驼峰
     * @param str   目标字符串
     * @return  变为小驼峰的字符串
     */
    public static String underlineToSmallCamel(String str){
        if (str==null||"".equals(str.trim())){
            return "";
        }
        int len=str.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=str.charAt(i);
            if (c=='_'){
                if (++i<len){
                    sb.append(Character.toUpperCase(str.charAt(i)));
                }
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    /**
     * 字符串转换为java驼峰式  首字母小写 遇"_"转大写
     *  @param str
     * @return
     */
    public static String javaStringFormat (String str) {
    	String removeUnderLine = underlineToSmallCamel(underlineToBigCamel(str));
    	
    	return removeUnderLine.substring(0,1).toLowerCase() +removeUnderLine.substring(1);
    }
}
