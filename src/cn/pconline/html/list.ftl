<%@page contentType="text/html; charset=UTF-8" session="false" %>
<%@include  file="../../jspf/import.jspf"%>

		<form id="pagerForm" method="get">     
		<#if columns?exists>  
	    <#list columns?keys as key> 
	    <input type="hidden" name="${key}" value="${r'${param.'}${key}}" />
	    </#list>
	    </#if>
		<input type="hidden" name="_p" value="${r'${param._p}'}" />
		<input type="hidden" name="pageNum" value="${r'${param.pageNum}'}" />
		<input type="hidden" name="numPerPage" value="${r'${param.numPerPage}'}"/>
		<input type="hidden" name="orderField" value="${r'${param.orderField}'}"/>
		<input type="hidden" name="orderDirection" value="${r'${param.orderDirection}'}" />
		</form>
		
		<div class="pageHeader">
		<form method="get" class="pageForm required-validate" onsubmit="return navTabSearch(this);">
		<div class="searchBar">
			<table class="searchContent">
	    		<tr>
				<#if columns?exists>  
	    		<#list columns?keys as key> 
					<td>${columns[key]}<input type="text" name="${key}" value="${r'${param.'}${key}}"/></td>
	    		</#list>
	    		</#if>
	    			<td>
	    			
						<div class="subBar">
						<ul><li><div class="buttonActive"><div class="buttonContent"><button type="submit">检索</button></div></div></li></ul>
						</div>
					</td>
				</tr>
			</table>
		</div>
		</form>
	</div>

		<div class="pageContent">
		<div class="panelBar">
		<ul class="toolBar">
			<li><a title="确定要修改这些记录吗?" target="selectedTodo" rel="ids" href="${r'${ctx}'}/admin/${EntityName}/batchUpdateUStatus.do?st=0" postType="string" class="edit"><span>正常</span></a></li>
			<li class="line">line</li>
	    	<li><a title="确定要修改这些记录吗?" target="selectedTodo" rel="ids" href="${r'${ctx}'}/admin/${EntityName}/batchUpdateUStatus.do?st=1" postType="string" class="edit"><span>禁用</span></a></li>
			<li class="line">line</li>
			<li><a class="edit" href="${r'${ROOT}'}/admin/user/update.do?id=${r'{id}'}" target="navTab" title="修改用户" rel="edit-project"><span>修改</span></a></li>
			<li><a title="确实要删除这些记录吗?" target="selectedTodo" rel="ids" href="${r'${ctx}'}/admin/user/batchDelete.do" postType="string" class="delete"><span>批量删除</span></a></li>
			<li><a class="icon" href="${r'${ctx}'}/admin/user/export.do" target="dwzExport" title="确定要导出吗?"><span>导出excel</span></a></li>
		</ul>
	</div>
	
		<table class="list" width="100%" layoutH="115">
			<thead>
				<tr>
				<th width="22"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
				<#if columns?exists>  
	    		<#list columns?keys as key> 
					<th orderField="${key}" class="${r'${'}param.orderField=='${key}' ? param.orderDirection : ''}">${columns[key]}</th>
	    		</#list>
	    		</#if>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
				<c:forEach var="item" items="${'${'}pager.resultList }" varStatus="idx">
				<tr target="id" rel="${'${'}item.id }">
					<td><input name="ids" value="${'${'}item.${prikey} }" type="checkbox"></td>
				<#if columns?exists>  
	    		<#list columns?keys as key> 
					<td>${r'${'}item.${key}}</td>
	    		</#list>
	    		</#if>
				<%-- <td><fmt:formatDate value="${r'${'}item.createAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><fmt:formatDate value="${r'${'}item.updateAt}" pattern="yyyy-MM-dd HH:mm:ss"/></td> --%>	
				</tr>
				</c:forEach>
				</tbody>
				</table>
				
					<div class="panelBar">
					<div class="pages">
					<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
						<option ${r'${'}param.numPerPage == 20 ? "selected " : ""}value="20">20</option>
						<option ${r'${'}param.numPerPage == 50 ? "selected " : ""}value="50">50</option>
						<option ${r'${'}param.numPerPage == 100 ? "selected " : ""}value="100">100</option>
						<option ${r'${'}param.numPerPage == 200 ? "selected " : ""}value="200">200</option>
					</select>
					<span>共 ${r'${'}pager.total} 条&nbsp;&nbsp;|&nbsp;&nbsp;</span>
					</div>
					<div class="pagination" targetType="navTab" totalCount="${r'${'}pager.total}" numPerPage="${r'${'}pager.pageSize}" pageNumShown="10" currentPage="${r'${'}pager.pageNo}"></div>
		  </div>
	</div>
