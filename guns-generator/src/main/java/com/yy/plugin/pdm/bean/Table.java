package com.yy.plugin.pdm.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class Table {

	private boolean gen;
	private Map columns;
	private List columnsList;
	private Map parents;
	private Map childs;
	private Key pk;
	private String id;
	/**
	 * 描述
	 */
	private String comment;
	private String name;

	/**
	 * 表明
	 */
	private String code;
	private String packageName;
	private String className;

	public List getColumnsList() {
		return columnsList;
	}

	public void setColumnsList(List columnsList) {
		this.columnsList = columnsList;
	}

	public Table() {
		gen = true;
		columns = new HashMap();
		columnsList = new ArrayList();
		parents = new HashMap();
		childs = new HashMap();
	}

	public void setParent(String columnid, Table parent) {
		parents.put(columnid, parent);
	}

	public Table getParent(String columnid) {
		return (Table) parents.get(columnid);
	}

	public void setChild(String columnid, Table child) {
		childs.put(columnid, child);
	}

	public Table getChild(String columnid) {
		return (Table) childs.get(columnid);
	}

	public void setColumn(Column col) {
		columns.put(col.getId(), col);
	}

	public Column getColumn(String id) {
		return (Column) columns.get(id);
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Key getPk() {
		return pk;
	}

	public void setPk(Key pk) {
		this.pk = pk;
	}

	public boolean isGen() {
		return gen;
	}

	public String getFullName() {
		return packageName + "." + getDTOName();
	}

	public String getFieldName() {
		return "tb" + getDTOName();
	}

	public String getActionName() {
		return className + "Controller";
	}

	public String getJsonActionName() {
		return "Json" + className + "Action";
	}

	public String getJsonTestName() {
		return "Json" + className + "Test";
	}

	public String getDTOName() {
		return "T" + className + "DTO";
	}

	public String getClassName() {
		return className;
	}
	
	public String getClassNameField() {
		return className.substring(0,1).toLowerCase()+className.substring(1);
	}
	
	public String getEntityName() {
		return className+"Entity";
	}
	
	public String getQueryEntityName() {
		return className+"Query";
	}
	
	public String getTestName() {
		return "Test"+className;
	}
	
	public String getVoEntityName() {
		return className+"Vo";
	}
	
	public String getBeanName() {
		return className;
	}

	public String getDAOName() {
		return className + "Dao";
	}
	
	public String getMapperName() {
		return className + "Mapper";
	}

	public String getBPOName() {
		return className + "BPO";
	}

	public String getBaseServiceImp() {
		return className + "BaseServiceImpl";
	}
	
	public String getServiceImp() {
		return className + "ServiceImpl";
	}

	public String getBaseService() {
		return className + "BaseService";
	}
	
	public String getJsonBean() {
		return className + "JsonBean";
	}
	
	public String getService() {
		return className + "Service";
	}

	public String getHbmFileName() {
		return getDTOName() + ".hbm.xml";
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setGen(boolean gen) {
		this.gen = gen;
	}

	public Map getChilds() {
		return childs;
	}

	public void setChilds(Map childs) {
		this.childs = childs;
	}

	public Map getColumns1() {
		return columns;
	}

	public void setColumns(Map columns) {
		this.columns = columns;
	}

	public Map getParents() {
		return parents;
	}

	public void setParents(Map parents) {
		this.parents = parents;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public String getExampleName() {
		return "Json" + className + "Example";
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Table [childs=" + childs + ", className=" + className + ", code=" + code + ", columns=" + columns + ", columnsList=" + columnsList + ", comment=" + comment + ", gen=" + gen + ", id=" + id + ", name=" + name + ", packageName="
				+ packageName + ", parents=" + parents + ", pk=" + pk + "]";
	}
}
