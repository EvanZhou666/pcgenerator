package cn.pconline;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TableContext {
	/**
     * 表名为key，表结构为value
     */
    public static Map<String,TableInfo> tables=new HashMap<String,TableInfo>();

    private TableContext(){}

    static {
        try {
            Connection con=DBManager.getConn();
            DatabaseMetaData dbmd=con.getMetaData();
            //获取所有表名
            ResultSet tableRet=dbmd.getTables(null,"%","%",new String[]{"TABLE"});
            while (tableRet.next()){
                String tableName= (String) tableRet.getObject("TABLE_NAME");
                Statement statement = con.createStatement();

//               根据前缀过滤表名
                String tableprefix = DBManager.getConf().getTableprefix();
                if (tableName.indexOf(tableprefix) == -1) {
					continue ;
				}
                
                TableInfo ti=new TableInfo(tableName,new HashMap<String,ColumnInfo>(),new ArrayList<ColumnInfo>());
                tables.put(tableName,ti);
                
                //获取注释
                System.out.println("表名字："+tableName);
                ResultSet rs = statement.executeQuery("show full columns from " + tableName);
                ColumnCommentUtils commentUtils = new ColumnCommentUtils();
                while (rs.next()) {   
//    			    System.out.println("字段名称：" + rs.getString("Field") + "\t"+ "字段注释：" + rs.getString("Comment") );
    			   // System.out.println(rs.getString("Field") + "\t:\t"+  rs.getString("Comment") );
    			    commentUtils.put(rs.getString("Field").trim(), rs.getString("Comment").trim());
    			} 
                
                
                //获取多个字段和类型
                ResultSet set=dbmd.getColumns(null,"%",tableName,"%");
                while (set.next()){
                    ColumnInfo ci=new ColumnInfo(set.getString("COLUMN_NAME"),set.getString("TYPE_NAME"),0);
                    //把注释和字段对应起来
                    commentUtils.comment2ColumnInfo(ci);
//                   String remark = set.getString("REMARKS");
                    ti.getColumns().put(set.getString("COLUMN_NAME"),ci);
                }
                
                commentUtils.destroy();
               
                
                
                //获取多个主键
                ResultSet set2=dbmd.getPrimaryKeys(null,"%",tableName);
                while (set2.next()){
                    String columnName=set2.getString("COLUMN_NAME");
                    System.out.println(columnName);
                    ColumnInfo ci2=ti.getColumns().get(columnName);
                    ci2.setKeyType(1);//设为主键
                    ti.getPriKeys().add(ci2);
                }
                if (ti.getPriKeys().size()>0){//取唯一主键，如果是联合主键，则为空
                    ti.setOnlyPriKey(ti.getPriKeys().get(0));
                }
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
