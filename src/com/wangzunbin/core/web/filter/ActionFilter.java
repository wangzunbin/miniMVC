package com.wangzunbin.core.web.filter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.wangzunbin.core.web.ActionConfig;
import com.wangzunbin.core.web.ActionContext;
import com.wangzunbin.core.web.ActionResult;



/**
 * 使用filter实现简单分发
 * @author Administrator
 *
 */
public class ActionFilter implements Filter{
	
	private Map<String, ActionConfig> actionConfigs = new HashMap<>();
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		Document document = this.getDocument();
		System.out.println(document.toString());
		NodeList nodeList = document.getElementsByTagName("action");
		System.out.println(nodeList.getLength());
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element actionEl = (Element) nodeList.item(i);
			String name = actionEl.getAttribute("name");
			String className = actionEl.getAttribute("className");
			String method = actionEl.getAttribute("method");
			ActionConfig config = new ActionConfig(name, className, method);
			actionConfigs.put(name, config);
			NodeList resultNodeList = actionEl.getElementsByTagName("result");
			for (int j = 0; j < resultNodeList.getLength(); j++) {
				Element resultEl = (Element) resultNodeList.item(i);
				String resultName = resultEl.getAttribute("name");
				String type = resultEl.getAttribute("type");
				String path = resultEl.getTextContent();
				Map<String, ActionResult> map = new HashMap<>();
				map.put(resultName, new ActionResult(resultName, type, path));
				config.setMap(map);
			}
		}
		System.out.println(actionConfigs);
	}
	
	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//			System.out.println("通用操作");
		/****** 第一版本  start  ************/
		/*HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String requestURI = req.getRequestURI().replace("/", "");
		System.out.println(requestURI);
		if ("department".equals(requestURI)) {
			DepartmentAction action = new DepartmentAction();
			action.execute();
		} else if ("employee".equals(requestURI)) {
			EmployeeAction action = new EmployeeAction();
			action.execute();
		} else {
			System.out.println(".......其他路径.....");
			
		}*/
		/****** 第一版本  end  ************/
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		ActionContext ctx = new ActionContext(req, resp);
		ActionContext.setCtx(ctx);
		
		
		String requestURI = req.getRequestURI().substring(1);
		ActionConfig actionConfig = actionConfigs.get(requestURI);
		if (actionConfig == null) {
			System.out.println("没有");
			chain.doFilter(req, resp);
			return;
		}
		String className = actionConfig.getClassName();
		String method = actionConfig.getMethod();
		try {
			Class actionName = Class.forName(className);
			Object newInstance = actionName.newInstance();
			Method method2 = actionName.getMethod(method);
			String viewName = (String) method2.invoke(newInstance);
			Map<String, ActionResult> map = actionConfig.getMap();
			ActionResult actionResult = map.get(viewName);
			if (actionResult != null) {
				String name = actionResult.getName();
				String type = actionResult.getType();
				String path = actionResult.getPath();
				if ("dispatcher".equals(type)) {
					req.getRequestDispatcher(path).forward(req, resp);
				} else if("redirect".equals(type)) {
					resp.sendRedirect(req.getContextPath() + path);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	private Document getDocument() {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("actions.xml");
//		File in = new File("actions.xml");
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
		} catch (Exception e) {
			System.out.println("找不到资源...");
			e.printStackTrace();
		}
		System.out.println("获取得是null.....");
		return null;
	}
}
