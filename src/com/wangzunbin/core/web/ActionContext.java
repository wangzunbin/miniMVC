package com.wangzunbin.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *  这个类线程不安全, 不是action不安全
 * @author Administrator
 *
 */
public class ActionContext {

	
	private HttpServletRequest req;  // http请求
	private HttpServletResponse resp; // http响应
	
	private static ActionContext ctx;
	
	private static ThreadLocal<ActionContext> local = new ThreadLocal<>();
	
	
	public static ActionContext getCtx() {
		return local.get();
	}

	public static void setCtx(ActionContext ctx) {
		local.set(ctx);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ActionContext(HttpServletRequest req, HttpServletResponse resp) {
		super();
		this.req = req;
		this.resp = resp;
	}
	
	public HttpServletRequest getReq() {
		return req;
	}
	public void setReq(HttpServletRequest req) {
		this.req = req;
	}
	public HttpServletResponse getResp() {
		return resp;
	}
	public void setResp(HttpServletResponse resp) {
		this.resp = resp;
	}
	
	
}
