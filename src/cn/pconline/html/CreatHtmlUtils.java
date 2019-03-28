package cn.pconline.html;

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
import cn.pconline.StringUtils;
import cn.pconline.TableContext;
import cn.pconline.TableInfo;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class CreatHtmlUtils {
public static void crateHTML(Map<String,Object> data,String templatePath,String templateName,String targetHtmlPath){
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
	public static void generateHtml() {
		Map<String,Object> root = new HashMap<String,Object>(); 
        String templatePath = "E:\\workspace\\pcgenerator\\src\\cn\\pconline\\html";
        String list_templateName = "list.ftl";
        String detail_templateName = "detail.ftl";
        String targetHtmlPath ="C:\\Users\\pc\\Desktop\\java\\jsp\\";
        
		 Map<String,TableInfo>tables=TableContext.tables;
	        for(TableInfo tableInfo:tables.values()){
	        	String TableName = StringUtils.underlineToBigCamel(tableInfo.getTname());
	        	List list = new ArrayList() ;
	        	Map<String, String> columns =new HashMap<String, String>();
	        	Map<String, ColumnInfo> beforeColumns = tableInfo.getColumns();
	        	for ( Map.Entry<String, ColumnInfo> entity: beforeColumns.entrySet()) {
					columns.put(entity.getKey() , entity.getValue().getComment());
				}
	            root.put("columns", columns);
	            String Entity = StringUtils.underlineToBigCamel(tableInfo.getTname());
	            root.put("EntityName", Entity.toLowerCase());
	            String prikey = tableInfo.getOnlyPriKey().getName();
	            root.put("prikey", prikey);
	            crateHTML(root, templatePath, list_templateName, targetHtmlPath+TableName.toLowerCase()+"\\list.jsp");
	            crateHTML(root, templatePath, detail_templateName, targetHtmlPath+TableName.toLowerCase()+"\\detail.jsp");
	            
	        }
	       
	}

}
