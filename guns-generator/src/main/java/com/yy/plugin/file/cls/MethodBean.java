package com.yy.plugin.file.cls;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造方法
 * @author kuan
 *
 */
public class MethodBean {

	private String comments;
	private String ret;
	private String name;
	private String params;
	private String annotion;
	private String exceptions;
	private List codeLines;

	public MethodBean() {
		codeLines = new ArrayList();
	}

	public void addCodeLine(String line) {
		codeLines.add(line);
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List getCodeLines() {
		return codeLines;
	}

	public void setCodeLines(List codeLines) {
		this.codeLines = codeLines;
	}

	public String getExceptions() {
		return exceptions;
	}

	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getRet() {
		if (ret == null || "".equals(ret))
			return "void";
		else
			return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public String getAnnotion() {
		return annotion;
	}

	public void setAnnotion(String annotion) {
		this.annotion = annotion;
	}
}
