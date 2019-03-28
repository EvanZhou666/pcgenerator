package cn.pconline.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pconline.ColumnInfo;
import cn.pconline.DBManager;
import cn.pconline.StringUtils;
import cn.pconline.TableContext;
import cn.pconline.TableInfo;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * @author zhouzixiang 2018年8月16日 下午3:55:01 
 * Controller层代码生成
 */
public class ControllerUtils {
	public static void generateControllFile (){
		generate("E:\\workspace\\pcgenerator\\src\\cn\\pconline\\controller\\", "C:\\Users\\pc\\Desktop\\java\\","modelController.ftl");
	}
		
	/**
	 * 生成源代码
	 * @return
	 */
	public static Map<String,Object> generate (String templatePath ,String targetHtmlPath ,String templateName) {
		 Map<String,TableInfo>tables=TableContext.tables;
		 Map <String ,Object> root  = new HashMap<String, Object>();
		 
	        String packagePath=DBManager.getConf().getControllerPackage().replace(".",File.separator);
	        /* File f=new File(targetHtmlPath+packagePath);
	        if (!f.exists()){
	            f.mkdirs();
	        }*/
	        
	        for(TableInfo tableInfo:tables.values()){
	        	
	        	String tableName = StringUtils.underlineToBigCamel(tableInfo.getTname());
//	        	javaBean 的字段名
	        	Map<String, String> columns =new HashMap<String, String>();
	        	
	        	Map<String, ColumnInfo> beforeColumns = tableInfo.getColumns();

	        	for ( Map.Entry<String, ColumnInfo> entity: beforeColumns.entrySet()) {
	        		//System.out.println("---"+entity.getValue().getComment());
					columns.put(entity.getKey() , entity.getValue().getComment());
				}
	            root.put("columns", columns);
	            String Entity = StringUtils.underlineToBigCamel(tableInfo.getTname());
	            root.put("EntityName", StringUtils.javaStringFormat(tableName));
	            String prikey = tableInfo.getOnlyPriKey().getName();
	            root.put("prikey", prikey);
	            root.put("tableName", tableName);
//	            System.out.println(templatePath);
//	            System.out.println(templateName);
//	            System.out.println(targetHtmlPath+packagePath+"\\"+tableName+"Controller"+".java");
	            createControllerSrc(root, templatePath, templateName, targetHtmlPath+packagePath+"\\"+tableName+"Controller"+".java");
	            
	        }
		
		return null;
	}
	
	/**
	 * 将数据渲染到模板中
	 * @param data
	 * @param templateName
	 * @param templatePath
	 * @param targetHtmlPath
	 */
	public static void createControllerSrc (Map data ,String templatePath ,String templateName,String targetHtmlPath ) {
		 Configuration freemarkerCfg = new Configuration();  
         //加载模版  
         Writer out = null;
         try {  
                 //设置要解析的模板所在的目录，并加载模板文件  
                 freemarkerCfg.setDirectoryForTemplateLoading(new File(templatePath));
                 //设置包装器，并将对象包装为数据模型  
                 freemarkerCfg.setObjectWrapper(new DefaultObjectWrapper());
             //指定模版路径  
             Template template = freemarkerCfg.getTemplate(templateName,"UTF-8");  
             template.setEncoding("UTF-8");  
             //静态页面路径  
             File Director = new File(targetHtmlPath.substring(0, targetHtmlPath.lastIndexOf(File.separator)));
             if (!Director.exists()) {
           	  Director.mkdirs();
             }
           	  
             File file = new File(targetHtmlPath) ;
             if (!file.exists()) {
           	  file.createNewFile();
             }
             FileOutputStream fos = new FileOutputStream(file);  
             out = new OutputStreamWriter(fos,"UTF-8");  
             //合并数据模型与模板  
         template.process(data, out);  
         } catch (Exception e) {  
             e.printStackTrace();  
         }finally{
                  try {
                          out.flush();
                          out.close();  
                 } catch (IOException e) {
                         e.printStackTrace();
                 }  
         }
	}
}
