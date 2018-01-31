package com.yy.plugin.file.cls;

/**
 * 字段
 * 
 * @author kuan
 *
 */
public class FiledBean {

	private String name;
	private String dbName;
	private String type;
	private String comment;
	private String annotion;

	public FiledBean() {
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAnnotion() {
		return annotion;
	}

	public void setAnnotion(String annotion) {
		this.annotion = annotion;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	} 
}
