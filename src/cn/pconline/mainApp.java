package cn.pconline;

import cn.pconline.controller.ControllerUtils;
import cn.pconline.html.CreatHtmlUtils;

/**
 * @author zhouzixiang  
 * 下午5:14:33
 *代码生成启动类 
 */
public class mainApp {
	public static void main(String[] args) {
//		https://www.cnblogs.com/wucongyun/p/6746378.html
		JavaFileUtils.generateJavaPOFile();
		JavaFileUtils.generateServiceFile();
		CreatHtmlUtils.generateHtml();
		ControllerUtils.generateControllFile();
	}
}
