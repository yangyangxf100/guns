package com.yy.plugin.file.cls;

import java.util.ArrayList;
import java.util.List;


public class ClassBean {

	private String pkgName;
	private List imps;
	private String className;
	private String classExt;
	private String classImp;
	private List fileds;
	private List methods;

	public ClassBean() {
		imps = new ArrayList();
		fileds = new ArrayList();
		methods = new ArrayList();
	}

	/**
	 * 
	 * @param name
	 * 		属性名称
	 * @param type
	 * 		属性类型
	 * @param comments
	 * 		属性描述
	 */
	public void addFiled(String name, String type, String comments) {
		FiledBean filed = new FiledBean();
		filed.setName(name);
		filed.setType(type);
		filed.setComment(comments);
		fileds.add(filed);
	}
	
	/**
	 * 
	 * @param name
	 * 		属性名称
	 * @param type
	 * 		属性类型
	 * @param comments
	 * 		属性描述
	 * @param annotion
	 * 		注解
	 */
	public void addFiled(String name, String type, String comments,String annotion) {
		FiledBean filed = new FiledBean();
		filed.setName(name);
		filed.setType(type);
		filed.setComment(comments);
		filed.setAnnotion(annotion);
		fileds.add(filed);
	}

	public void addMethod(MethodBean method) {
		methods.add(method);
	}

	public void addImps(String imp) {
		imps.add(imp);
	}

	public String getClassExt() {
		return classExt;
	}

	public void setClassExt(String classExt) {
		this.classExt = classExt;
	}

	public String getClassImp() {
		return classImp;
	}

	public void setClassImp(String classImp) {
		this.classImp = classImp;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List getImps() {
		return imps;
	}

	public void setImps(List imps) {
		this.imps = imps;
	}

	public List getMethods() {
		return methods;
	}

	public void setMethods(List methods) {
		this.methods = methods;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

	public List getFileds() {
		return fileds;
	}

	public void setFileds(List fileds) {
		this.fileds = fileds;
	}
}
