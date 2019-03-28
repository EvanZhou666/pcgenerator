package cn.pconline;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;




/**
 * @author zhouzixiang  
 * 下午5:12:19
 * 代码生成的工具
 */
public class JavaFileUtils {
	 /**
     * 根据字段信息生成java属性信息，如：var username-->private String username;相应的set和get方法源码
     * @param column    字段信息
     * @param convertor 类型转换器
     * @return  java属性的set/get方法
     */
    public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo column, TypeConvertor convertor){
        JavaFieldGetSet jfgs=new JavaFieldGetSet();
        //将字段转为java属性
        String javaFiledType= convertor.databaseType2JavaType(column.getDataType());
        String colunmName=StringUtils.underlineToSmallCamel(column.getName());
        //生成字段信息
        if (column.getKeyType() != 1) {
        	jfgs.setFieldInfo("\tprivate "+javaFiledType+" "+colunmName+";\n");
        } else {
        	jfgs.setFieldInfo("\t@Id\n\tprivate long "+colunmName+";\n");
        }
        
        //生成getter
        StringBuilder getSrc=new StringBuilder();
        if (column.getKeyType() != 1) {
        	getSrc.append("\tpublic "+javaFiledType+" get"+StringUtils.underlineToBigCamel(column.getName())+"(){\n");
        } else {
        	getSrc.append("\tpublic long get"+StringUtils.underlineToBigCamel(column.getName())+"(){\n");
        }
        
        getSrc.append("\t\treturn "+colunmName+";\n");
        getSrc.append("\t}\n");
        jfgs.setGetInfo(getSrc.toString());
        //生成setter
        StringBuilder setSrc=new StringBuilder();
        if (column.getKeyType() != 1) {
        	 setSrc.append("\tpublic void set"+StringUtils.underlineToBigCamel(column.getName())+"("+javaFiledType+" "+colunmName+"){\n");
        } else {
        	 setSrc.append("\tpublic void set"+StringUtils.underlineToBigCamel(column.getName())+"( long"+" "+colunmName+"){\n");
        }
       
        setSrc.append("\t\tthis."+colunmName+"="+colunmName+";\n");
        setSrc.append("\t}\n");
        jfgs.setSetInfo(setSrc.toString());
        return jfgs;
    }

    /**
     * 根据表信息生成java源码
     * @param tableInfo 表信息
     * @param convertor 数据类型转换器
     * @return  java类的源代码
     */
    public static String createJavaSrc(TableInfo tableInfo,TypeConvertor convertor){
        Map<String,ColumnInfo>columns=tableInfo.getColumns();
        List<JavaFieldGetSet> javaFields=new ArrayList<JavaFieldGetSet>();
        for (ColumnInfo columnInfo:columns.values()){
            JavaFieldGetSet javaFieldGetSet=createFieldGetSetSRC(columnInfo,convertor);
            javaFields.add(javaFieldGetSet);
        }
        StringBuilder src=new StringBuilder();
        //生成package语句
        src.append("package "+ DBManager.getConf().getPoPackage()+";\n");
        //生成import语句
        src.append("import java.sql.*;\n");
        src.append("import java.util.*;\n\n");
        //生成类声明语句
        src.append("\t@Entity(tableName=");
        src.append("\"");
        src.append(tableInfo.getTname());
        src.append("\")\n");
        src.append("\tpublic class "+StringUtils.underlineToBigCamel(tableInfo.getTname())+"{\n");
        //生成属性列表
        for (JavaFieldGetSet javaFieldGetSet:javaFields){
            src.append(javaFieldGetSet.getFieldInfo());
        }
        src.append("\n\n");
        //生成get方法列表
        for (JavaFieldGetSet javaFieldGetSet:javaFields){
            src.append(javaFieldGetSet.getGetInfo());
        }
        src.append("\n\n");
        //生成set方法列表
        for (JavaFieldGetSet javaFieldGetSet:javaFields){
            src.append(javaFieldGetSet.getSetInfo());
        }
        src.append("\n\n");
        
        String className = StringUtils.underlineToBigCamel(tableInfo.getTname());
        
        src.append("\tpublic static ");
        
        src.append(className);
        
        
        
        src.append(" find ( long "+tableInfo.getOnlyPriKey().getName());
        src.append(" ) {\n\t");
        src.append("\ttry {\n \t\t");
        src.append("\treturn GeliUtils.getDao().find(");
        src.append(className +".class,");
        src.append(tableInfo.getOnlyPriKey().getName());
        src.append(" ); \n");
        src.append("\t\t} catch (EmptyResultDataAccessException e) {\n");
        src.append("\t\treturn null;\n");
        src.append("\t\t}\n");
        src.append("\t}");
        src.append("\n");
        //生成结束符
        src.append("}");
        return src.toString();
    }

    /**
     * 生成java文件
     * @param tableInfo 表信息
     * @param convertor 类型转换器
     */
    public static void createJavaPoFile(TableInfo tableInfo,TypeConvertor convertor){
        //获取源码
        String src=createJavaSrc(tableInfo,convertor);
        String srcPath=DBManager.getConf().getSrcPath()+"/";
        //将包名转换为文件名，然后和srcPath拼接
        String packagePath=DBManager.getConf().getPoPackage().replace(".","/");
        File f=new File(srcPath+packagePath);
        if (!f.exists()){
            f.mkdirs();
        }
        BufferedWriter bw=null;
        try {
            //将源码写入文件
            bw=new BufferedWriter(new FileWriter(f.getAbsoluteFile()+"/"+StringUtils.underlineToBigCamel(tableInfo.getTname())+".java"));
            bw.write(src);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 生成service层代码
     * @param tableInfo
     * @param convertor
     */
    public static void createJavaServiceFile(TableInfo tableInfo,TypeConvertor convertor){
        Map<String,ColumnInfo>columns=tableInfo.getColumns();
        List<JavaFieldGetSet> javaFields=new ArrayList<JavaFieldGetSet>();
        TypeConvertor typeConvertor = new MySQLTypeConvertor() ;
        StringBuilder src=new StringBuilder();
        String EntityName =StringUtils.underlineToBigCamel(tableInfo.getTname());
        String ClassName = EntityName + "Service";
        //生成package语句
        src.append("package "+ DBManager.getConf().getServicePackage()+";\n\n");
        //生成import语句
        src.append("import org.apache.commons.logging.Log;\n");
        src.append("import org.apache.commons.logging.LogFactory;\n");
        src.append("import org.gelivable.dao.SqlBuilder;\n");
        src.append("import org.gelivable.web.GeliFunctions;\n");
        src.append("import org.springframework.jdbc.core.JdbcTemplate;\n");
        src.append("import org.springframework.stereotype.Service;\n");
        src.append("import org.springframework.util.StringUtils;\n");
        src.append("import cn.pconline.activity.entity.*;\n");
        src.append("import cn.pconline.activity.util.Pager;\n");
        
        src.append("@Service\n");
        src.append("public class "+EntityName+"Service extends AbstractService<");
        src.append(EntityName+">{\n");
//        public UserService() {
        src.append("\tpublic "+ClassName);
        src.append("() {\n");
        src.append("\t\t "+"super" +"("+EntityName+".class) ;\n");
        src.append("\t}\n");
        
        src.append("\tpublic Pager page( long "+tableInfo.getOnlyPriKey().getName());
        
        Map<String, ColumnInfo> columns2 = tableInfo.getColumns();
        columns2.remove(tableInfo.getOnlyPriKey().getName());
        for (Map.Entry<String, ColumnInfo> entry : columns2.entrySet()) {
        	ColumnInfo columnInfo = entry.getValue();
        	String javaType = typeConvertor.databaseType2JavaType(columnInfo.getDataType());
        	if ("Integer".equals(javaType)) {
        		src.append(",int "+columnInfo.getName());
        	}
        	
        	if ("String".equals(javaType)) {
        		src.append(",String "+columnInfo.getName());
        	}
        }
//        src.append(tableInfo.getOnlyPriKey().getName());
        src.append(",int pageNo,int pageSize,String orderField, String orderDirection){\n");
        src.append("\t\tSqlBuilder sqlBuilder = new SqlBuilder();\n");
//        sqlBuilder.appendSql("SELECT id FROM " + orm.getTableName(type) + " WHERE 1=1 ");
        src.append("\t\tsqlBuilder.appendSql(\"SELECT ");
        src.append(tableInfo.getOnlyPriKey().getName());
        src.append(" FROM \" + orm.getTableName(type) + \" where 1=1\" );\n");
        for (Map.Entry<String, ColumnInfo> entry : columns.entrySet()) {
        	ColumnInfo columnInfo = entry.getValue();
        	String javaType = typeConvertor.databaseType2JavaType(columnInfo.getDataType());
        	if ("Integer".equals(javaType)) {
        		src.append("\t\tif ("+columnInfo.getName()+" != -1) {\n");
//        		sqlBuilder.appendSql(" and id = ").appendValue(id);
        		src.append("\t\t\tsqlBuilder.appendSql(\" ");
        		src.append("and ");
        		src.append(columnInfo.getName());
        		src.append("= \").appendValue("+columnInfo.getName()+");\n");
        		src.append("\t\t}\n\n");
        	}
        	
        	if ("String".equals(javaType)) {
        		src.append("\t\tif (!StringUtils.isEmpty(");
        		src.append(columnInfo.getName());
        		src.append(")) {\n");
//        		sqlBuilder.appendSql(" and id = ").appendValue(id);
        		src.append("\t\t\tsqlBuilder.appendSql(\" ");
        		src.append("and ");
        		src.append(columnInfo.getName());
        		src.append("= \").appendValue("+columnInfo.getName()+");\n");
        		src.append("\t\t}\n\n");
        	}
        	/*src.append("\torderField = orm.ensureField(type, orderField);\n");
        	src.append("\t String orderSql = sqlBuilder.getSql();\n");
        	src.append("\t orderDirection = GeliFunctions.orderDirection(orderDirection);\n");
        	src.append("\t if (!StringUtils.isEmpty(orderField) && !StringUtils.isEmpty(orderSql)) {\n");
        	src.append("\t\t orderSql += \" order by \" + orm.getColumnByField(type, orderField) + \" \" + orderDirection;\n");
        	src.append("\t} else {\n");
        	src.append("\torderSql += \" order by createAt desc\";\n");
        	src.append("\t}\n");
        	src.append("\tPager pager = new Pager();\n");
        	src.append("\tif(pageNo==-1&&pageSize==-1){\n");
        	src.append("\tpager.setResultList(list(orderSql,sqlBuilder.getValues()));\n");
        	src.append("\treturn pager;\n");
        	src.append("\t}\n");
        	src.append("\t pager.setPageSize(pageSize);\n");
        	src.append("\tpager.setResultList(page(orderSql,pageNo,pageSize, sqlBuilder.getValues()));\n");
        	src.append("\tpager.setTotal(count(sqlBuilder.getSql().replaceFirst(\"id\", \"count(*)\"), sqlBuilder.getValues()));\n");
        	src.append("\t 	return pager;\n");*/
        	
        	
        
            
            
		}
        
      	src.append("\t\torderField = orm.ensureField(type, orderField);\n");
    	src.append("\t\torderDirection = GeliFunctions.orderDirection(orderDirection);\n");
    	src.append("\t\tString orderSql = sqlBuilder.getSql();\n");
    	src.append("\t\tif (!StringUtils.isEmpty(orderField) && !StringUtils.isEmpty(orderSql)) {\n");
    	src.append("\t\torderSql += \" order by \" + orm.getColumnByField(type, orderField) + \" \" + orderDirection;\n");
    	src.append("\t\t}\n");
    	src.append("\t\tPager pager = new Pager();\n");
    	src.append("\t\tif(pageNo==-1&&pageSize==-1){\n");
    	src.append("\t\t\tpager.setResultList(list(orderSql,sqlBuilder.getValues()));\n");
    	src.append("\t\t\treturn pager;\n");
    	src.append("\t\t}\n");
    	src.append("\t\tpager.setPageNo(pageNo);\n");
    	src.append("\t\tpager.setPageSize(pageSize);\n");
    	src.append("\t\tpager.setResultList(page(orderSql, pageNo,pageSize,sqlBuilder.getValues()));\n");
//    	pager.setTotal(count(sqlBuilder.getSql().replaceFirst("id", "count(*)"), sqlBuilder.getValues()));
    	src.append("\t\tpager.setTotal(count(sqlBuilder.getSql().replaceFirst(\"");
    	src.append(tableInfo.getOnlyPriKey().getName());
    	src.append("\", \"count(*)\"), sqlBuilder.getValues()));\n");
    	src.append("\t\treturn pager;\n");
    	src.append("\t\t}\n");
//    	System.out.println(columnInfo.dataType);
    	src.append("\t}\n");
//        System.out.println(src.toString());
    	
  	  //获取源码
        String serviceSrc =src.toString();
        String srcPath=DBManager.getConf().getSrcPath()+"/";
        //将包名转换为文件名，然后和srcPath拼接
        String packagePath=DBManager.getConf().getServicePackage().replace(".","/");
        File f=new File(srcPath+packagePath);
        if (!f.exists()){
            f.mkdirs();
        }
        BufferedWriter bw=null;
        try {
            //将源码写入文件
            bw=new BufferedWriter(new FileWriter(f.getAbsoluteFile()+"/"+StringUtils.underlineToBigCamel(tableInfo.getTname())+"Service.java"));
            bw.write(serviceSrc);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 生成POFile
     */
    public static void generateJavaPOFile(){
        Map<String,TableInfo>tables=TableContext.tables;
        for(TableInfo tableInfo:tables.values()){
            createJavaPoFile(tableInfo,new MySQLTypeConvertor());
        }
    }
    
    public static void generateServiceFile () {
    	Map<String,TableInfo>tables=TableContext.tables;
    	for(TableInfo tableInfo:tables.values()){
         JavaFileUtils.createJavaServiceFile(tableInfo, new MySQLTypeConvertor());
    	}
    }
}
