package cn.pconline;

import java.util.HashMap;
import java.util.Map;

public class ColumnCommentUtils {
	Map <String ,String > colComent = null;
	
	public ColumnCommentUtils() {
		colComent = new HashMap<String, String> ();
	}

	public  void put(String column,String comment) {
		colComent.put(column,comment);
	}
	
	public void comment2ColumnInfo (ColumnInfo columnInfo){
		for (Map.Entry<String, String> entity: colComent.entrySet()) {
			if (entity.getKey().trim().equals(columnInfo.getName())) {
				columnInfo.setComment(entity.getValue());
			}
		}
	}
	
	public void destroy () {
		colComent =null ;
	}
}
