package com.yy.plugin.pdm.bean;

public class Column {

	//code=NET_ADDR, comment=如 www.baidu.com等, datatype=java.lang.String, length=128, precision=0,  fildName=null
	private boolean genRefence;
	private String id;
	/**
	 * 字段名称
	 */
	private String code;
	
	private String name;
	/**
	 * 字段描述
	 */
	private String comment;
	/**
	 * 字段数据类型
	 */
	private String datatype;
	
	private String datatypeSource;
	
	//是否可为空
	private String isCanNull;
	/**
	 * 字段长度
	 */
	private String length;
	private int precision;
	private Table table;
	private String fildName;
	private String fildNameOriginal;//原始字段

	public Column() {
		genRefence = true;
	}

	public String getFildName() {
		return fildName;
	}

	public void setFildName(String fildName) {
		this.fildName = fildName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDatatype() {
		return datatype;
	}

	public String getCodeDatetype() {
		if ("org.springframework.orm.hibernate3.support.ClobStringType"
				.equals(datatype))
			return "java.lang.String";
		if ("org.springframework.orm.hibernate3.support.BlobByteArrayType"
				.equals(datatype))
			return "byte []";
		else
			return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public boolean isGenRefence() {
		return genRefence;
	}

	public void setGenRefence(boolean genRefence) {
		this.genRefence = genRefence;
	}

	public String getDatatypeSource() {
		return datatypeSource;
	}

	public void setDatatypeSource(String datatypeSource) {
		this.datatypeSource = datatypeSource;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsCanNull() {
		return isCanNull;
	}

	public void setIsCanNull(String isCanNull) {
		this.isCanNull = isCanNull;
	}

	public String getFildNameOriginal() {
		return fildNameOriginal;
	}

	public void setFildNameOriginal(String fildNameOriginal) {
		this.fildNameOriginal = fildNameOriginal;
	}

	@Override
	public String toString() {
		return "Column [code=" + code + ", comment=" + comment + ", datatype=" + datatype + ", datatypeSource=" + datatypeSource + ", fildName=" + fildName + ", fildNameOriginal=" + fildNameOriginal + ", genRefence=" + genRefence + ", id=" + id
				+ ", isCanNull=" + isCanNull + ", length=" + length + ", name=" + name + ", precision=" + precision + ", table=" + table + "]";
	} 
	
	
}
