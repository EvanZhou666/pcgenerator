package cn.pconline;

public class MySQLTypeConvertor implements TypeConvertor{
	
    public String databaseType2JavaType(String column) {
    	String lowerCase = column.toLowerCase();
    	if ("varchar".equals(lowerCase) || "char" .equals(lowerCase)) {
    		return "String" ;
    	} else if ("smallint".equals(lowerCase) || "int".equals(lowerCase) || "tinyint".equals(lowerCase)) {
    		return "Integer";
    	} else if ("bigint".equals(lowerCase)) {
    		return "Long" ;
    	} else if ("double".equals(lowerCase) || "float".equals(lowerCase)) {
    		return "Double";
    	} else if ("clob".equals(lowerCase) ){
    		return "java.sql.Clob";
    	} else if ("blob" .equals(lowerCase)) {
    		return "java.sql.Blob" ;
    	} else if ("date".equals(lowerCase) || "datetime".equals(lowerCase)) {
    		return "java.util.Date" ;
    	} else if ("time".equals(lowerCase)) {
    		return "java.sql.Time";
    	} else if ("timestamp".equals(lowerCase)) {
    		return "java.sql.Timestamp" ;
    	} else if ("decimal".equals(lowerCase)) {
    		return "java.math.BigDecimal";
    	} else {
			return null ;
		}
    }

	@Override
	public String javaType2DatabaseType(String column) {
		throw new UnsupportedOperationException("暂不支持该操作");
	}
}