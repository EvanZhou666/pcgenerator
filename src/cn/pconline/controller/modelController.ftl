package cn.pconline.activity.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.gelivable.dao.GeliOrm;
import org.gelivable.dao.GeliUtils;
import org.gelivable.web.Env;
import org.gelivable.web.EnvUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import cn.pconline.activity.entity.*;
import cn.pconline.activity.service.*;
import cn.pconline.activity.util.*;


@Controller
@RequestMapping("/admin/${EntityName}")
public class ${tableName}Controller {

	private String navTabId = "list-${EntityName}";

	@Autowired
	${tableName}Service service;
	
	/**
	 * 清理列表缓存
	 */ 
	public void clearMc(Object obj){ 
		CacheClient cc = CacheClient.instance(); 
		String prefix = (String)BaseConfig.get("tablePreFix"); 
	}
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception{

		Env env = EnvUtils.getEnv();
		long id = env.paramLong("id", -1l);
		int numPerPage = env.paramInt("numPerPage", 20);
		String orderField = env.param("orderField","createAt");
		String orderDirection = env.param("orderDirection","DESC");
		Pager page = service.page(pageNum, numPerPage, orderField, orderDirection);

		request.setAttribute("${EntityName}", ${EntityName}.class);
		request.setAttribute("pager", page);

		return "${EntityName}/list";
	}
	
	@RequestMapping(value = "/create",method=RequestMethod.POST)
	public void create(HttpServletRequest request, HttpServletResponse response){

		Env env = EnvUtils.getEnv();
		${tableName} entity = env.bind(${tableName}.class);

		long result = service.create(entity);

		if(result > 0) {
			clearMc(entity);
			WebUtils.setResponseContext(response, 200, "操作成功", navTabId, "closeCurrent");
		} else {
			WebUtils.setResponseContext(response, 300, "操作失败", null, null);
		}
	}
	
	@RequestMapping(value="/update.do", method = RequestMethod.GET)
	public String updateGet(HttpServletRequest req, HttpServletResponse res) {
		Env env = EnvUtils.getEnv();
		long id = env.paramLong("id", -1);
		
		if (id == -1) {
			WebUtils.setResponseContext(res, 300, "错误ID！", null, null);
            return null;
		}
		
		User user = service.find(id);
		if (user == null) {
			WebUtils.setResponseContext(res, 300, "找不到专题！", null, null);
            return null;
		}
		req.setAttribute("user", user);
        return "${EntityName}/detail";
	}
	
	@RequestMapping(value = "/update",method=RequestMethod.POST)
	public void update(HttpServletRequest request, HttpServletResponse response){

		Env env = EnvUtils.getEnv();
		long id = env.paramLong("${prikey}", 0);
		${tableName} entity = service.find(id);
		${tableName} newEntity = env.bind(${tableName}.class,entity);
		long result = service.update(newEntity);
		if(result > 0) {
			clearMc(newEntity);
			WebUtils.setResponseContext(response, 200, "操作成功", navTabId, "closeCurrent");
		} else {
			WebUtils.setResponseContext(response, 300, "操作失败", null, null);
		}
	}
	
	@RequestMapping(value = "/batchDelete.do")
	public void batchDelete(HttpServletRequest request, HttpServletResponse response){
		String idstr = request.getParameter("ids");
		if(idstr==null || idstr.matches("//s*")){
			WebUtils.setResponseContext(response, 300, "删除的数据为空！", null, null);
			return;
		}
		int count = service.batchDelete(idstr);
		if(count>0){
			WebUtils.setResponseContext(response, 200, "操作成功，删除了数据："+count, navTabId, null);
		}else{
			WebUtils.setResponseContext(response, 300, "删除数据失败！", null, null);
		}
	}
	
	@RequestMapping(value="/import.do")
	public void importExcel(HttpServletRequest request,HttpServletResponse response) {
		//this.cfs.innitMap();

		Env env = EnvUtils.getEnv();
		CacheClient cc = (CacheClient) env.getBean(CacheClient.class);
		Map user = ((AuthFacade) env.getBean(AuthFacade.class)).getLoginUser();
		String key = "1";
		if (user != null) {
			key = "import_" + user.get("name");
		}

		String isRun = "";
		if (cc != null) {
			isRun = (String) cc.get(key);
		}
		if ((isRun != null) && (isRun.trim().equalsIgnoreCase("yes"))) {
			WebUtils.setResponseContext(response, 300, "导入正在进行中！", null, null);
			return;
		}
		cc.set(key, "yes", 30000L);

		ExcelUtils excelUtils = new ExcelUtils();
		String[] propertys = { 
		<#if columns?exists>  
	    <#list columns?keys as key> 
	   	 "${key}",
	    </#list>
	    </#if>};
		List<Enroll> list = excelUtils.importExcel(request, Enroll.class, propertys, 1);
		if ((list != null) && (list.size() != 0)) {
			for (int i = 0; i < list.size(); i++) {
				//根据省市经销商的Id显示对应的名字
  				//list.get(i).setCreateAt(T.getNow());
  				//list.get(i).setIsSuccess(0); //待同步
  				//list.get(i).setComeFrom(2); //后台导入
			}

			this.service.batchCreate(list);
			WebUtils.setResponseContext(response, 200, "导入成功", this.navTabId,
					null);
		} else {
			WebUtils.setResponseContext(response, 300, "导入出错！", null, null);
		}
	}
	
	@RequestMapping(value = "/detail",method=RequestMethod.GET)
	public String detail(HttpServletRequest request, HttpServletResponse response){

		Env env = EnvUtils.getEnv();
		request.setAttribute("${EntityName}", null);
		return "${EntityName}/detail";
	}
	
	@RequestMapping(value = "export.do")
	public void export(HttpServletRequest request, HttpServletResponse response){

		Env env = EnvUtils.getEnv();
		int pageNum = env.paramInt("pageNum", 1);
		int numPerPage = env.paramInt("numPerPage", 20);
		String orderField = env.param("orderField","createAt");
		String orderDirection = env.param("orderDirection","DESC");

		Pager pager = service.page(pageNum, numPerPage, orderField, orderDirection);
		ExcelUtils eu = new ExcelUtils();
		String[] labels = { 
		<#if columns?exists>  
	    <#list columns?keys as key> 
	   	 "${key} : ${columns[key]}",
	    </#list>
	    </#if>};
	    GeliOrm orm = GeliUtils.getOrm();
		String entityLabel = orm.getEntityLabel(${tableName}.class);
		eu.exportExcel(request, response, pager.getResultList(), labels, entityLabel);
		}

	}
	