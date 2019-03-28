<%@page contentType="text/html; charset=UTF-8" session="false" %>
<%@include  file="../../jspf/import.jspf"%>
<div class="pageContent">
<c:choose>
<c:when test="${'${'}empty ${EntityName}}">
<form id="Form" method="post" action="${'${'}ROOT}/admin/user/create.do" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
</c:when>
<c:otherwise>
<form id="Form" method="post" action="${r'${'}ROOT}/admin/user/update.do?id=${r'${'}user.${prikey}}" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
</c:otherwise>
</c:choose>
	<div class="pageFormContent" layoutH="56">
		<#if columns?exists>  
	    <#list columns?keys as key> 
	    <dl class="nowrap">
			<dt><label>${columns[key]}: </label></dt>
			<dd>
				<input class="textInput required digits" value="${r'${'}empty ${EntityName} ? 0 : user.${key}}" name="${key}" />
			</dd>
		</dl>
	    </#list>
	    </#if>
	</div>
	
	
	<div class="formBar">
			<ul>
				<li>
					<div class="buttonActive">
						<div class="buttonContent">
							<button type="button" onclick="javascript:${'$('}'#Form').submit();">保存</button>
						</div>
					</div>
				</li>
				<li>
					<div class="button">
						<div class="buttonContent">
							<button type="button" class="close">取消</button>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</form>
</div>