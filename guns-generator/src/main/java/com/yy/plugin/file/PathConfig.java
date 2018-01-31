package com.yy.plugin.file;

import java.io.File;

public class PathConfig {

	/**
	 * pdm 文件位置
	 */
	private String pdmFile;

	/**
	 * 项目位置
	 * 
	 */
	private String projectPath;

	/**
	 * 公司名称
	 * 
	 */
	private String govName;

	/**
	 * 应用名称
	 * 
	 */
	private String appName;

	/**
	 * 模块名称
	 * 
	 */
	private String moduleName;

	public PathConfig() {
	}


	/*************************************************************************************************/
	public String getBpoPackage() {
		return "com." + getGovName() + "." + appName + "." + moduleName + ".model.bpo";
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getPdmFile() {
		return pdmFile;
	}

	public void setPdmFile(String pdmFile) {
		this.pdmFile = pdmFile;
	}

	public String getGovName() {
		return govName;
	}

	public void setGovName(String govName) {
		this.govName = govName;
	}

	public String getExamplePackage() {
		return "com." + govName + "." + appName + ".example";
	}
	
	
	public String getActionPath() {
		return projectPath + File.separator + "src" + File.separator + getActionPackage().replaceAll("\\.", "\\\\") + File.separator;
	}

	public String getAceActionPath() {
		return projectPath + File.separator + "src" + File.separator + getAceActionPackage().replaceAll("\\.", "\\\\") + File.separator;
	}
	
	public String getApiActionPath() {
		return projectPath + File.separator + "src" + File.separator + getApiActionPackage().replaceAll("\\.", "\\\\") + File.separator;
	}

	public String getDaoPath() {
		return projectPath + File.separator + "src" + File.separator + getDaoPackage().replaceAll("\\.", "\\\\") + File.separator;
	}
	
	public String getTestPath() {
		return projectPath + File.separator + "src" + File.separator + getTestPackage().replaceAll("\\.", "\\\\") + File.separator;
	}

	public String getMapperPath() {
		return projectPath + File.separator + "src" + File.separator + getMapperPackage().replaceAll("\\.", "\\\\") + File.separator;
	}

	public String getEntityPath() {
		return projectPath + File.separator + "src" + File.separator + getEntityPackage().replaceAll("\\.", "\\\\") + File.separator;
	}

	public String getBeanPath() {
		return projectPath + File.separator + "src" + File.separator + getBeanPackage().replaceAll("\\.", "\\\\") + File.separator;
	}
	
	public String getVoPath() {
		return projectPath + File.separator + "src" + File.separator + getVoPackage().replaceAll("\\.", "\\\\") + File.separator;
	}

	public String getServicePath() {
		return projectPath + File.separator + "src" + File.separator + getServicePackage().replaceAll("\\.", "\\\\") + File.separator;
	}

	public String getServiceImpPath() {
		return projectPath + File.separator + "src" + File.separator + getServiceImpPackage().replaceAll("\\.", "\\\\") + File.separator;
	}

	public String getJspPath() {
		return projectPath + File.separator + "jsp" + File.separator;
	}
	
	public String getAceEditJspPath() {
		return projectPath + File.separator + "ace" + File.separator;
	}
	
	public String getAceListJspPath() {
		return projectPath + File.separator + "ace" + File.separator;
	}
	
	public String getSqlPath() {
		return projectPath + File.separator + "src" + File.separator;
	}

	/*************************************************************************************************/
	public String getActionPackage() {
		return "com." + govName + ".cms.controller";

	}
	
	public String getAceActionPackage() {
		return "com." + govName + ".cms.controller.ace";

	}

	public String getApiActionPackage() {
		return "com." + govName + ".cms.api";

	}

	public String getEntityPackage() {
		return "com." + govName + ".persistence.beans";
	}

	public String getBeanPackage() {
		return "com." + govName + ".cms.po";
	}
	
	public String getVoPackage() {
		return "com." + govName + ".cms.vo";
	}

	public String getDaoPackage() {
		return "com." + govName + ".cms.dao";
	}

	public String getTestPackage() {
		return "com.gochinatv.api";
	}
	
	public String getMapperPackage() {
		return "com." + govName + ".mapper";
	}

	public String getServiceImpPackage() {
		return "com." + govName + "." + appName + ".serviceImpl";
	}

	public String getServicePackage() {
		return "com." + govName + "." + appName + ".service";
	}

	public String getExamplePath() {
		return projectPath + File.separator + "src" + File.separator + getExamplePackage().replaceAll("\\.", "\\\\") + File.separator;
	}

}
