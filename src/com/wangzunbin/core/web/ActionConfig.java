package com.wangzunbin.core.web;

public class ActionConfig {

	private String name; //路径名称
	private String className; // 全限定名
	private String method; // 方法名称
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public ActionConfig(String name, String className, String method) {
		super();
		this.name = name;
		this.className = className;
		this.method = method;
	}
	@Override
	public String toString() {
		return "ActionConfig [name=" + name + ", className=" + className + ", method=" + method + "]";
	}

	
}
