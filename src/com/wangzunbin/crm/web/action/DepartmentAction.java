package com.wangzunbin.crm.web.action;

import com.wangzunbin.core.web.ActionContext;

public class DepartmentAction {

	public String execute() {
		System.out.println("..获取的参数: + " + ActionContext.getCtx().getReq().getParameter("name"));
		return "list";
	}
}
