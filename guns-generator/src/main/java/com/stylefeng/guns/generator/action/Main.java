package com.stylefeng.guns.generator.action;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.stylefeng.guns.core.CoreFlag;
import com.stylefeng.guns.generator.action.config.WebGeneratorConfig;
import com.stylefeng.guns.generator.action.model.GenQo;
import com.stylefeng.guns.generator.action.utils.LineNumberReaderUtils;
import com.yy.plugin.pdm.bean.Table;
import com.yy.plugin.pdm.xml.PdmParse;

public class Main {

	public static String url = "jdbc:mysql://127.0.0.1:3306/guns";
	public static String userName = "root";
	public static String password = "123";
	public static String projectPath = "/C:/Users/1/Desktop/guns/code/guns/guns-admin";

	public static void main(String[] args) throws Exception {
		String tableName = "cus_person";
		String bizName = "人员管理";
		gener(tableName, bizName);

//		String pdmFile = "D:/db.pdm";
//
//		PdmParse parse = new PdmParse();
//		Map<String, Table> tables;
//
//		tables = parse.parse(pdmFile);
//
//		Iterator it = tables.keySet().iterator(); // 产生字段与字段描述 it =
//		tables.keySet().iterator();
//		while (it.hasNext()) {
//			Table table = (Table) tables.get(it.next());
//			System.out.println(table.getCode() + "\t" + table.getComment());
//			String tableN = table.getCode();
//			String comment = table.getComment();
////			gener(tableN, comment); //
//			delete(tableN, comment);
//		}

		File[] fileList = new File(projectPath + "/src/main/java").listFiles();
		for (File f : fileList) {
			if (f.getAbsolutePath().endsWith("sql")) {
				List<String> list = LineNumberReaderUtils.getMsgWithUtf8(f.getAbsolutePath());
				for (String s : list) {
					System.out.println(s);
				}
			}
		}
		System.out.println("finish");
	}

	private static void gener(String tableName, String bizName) {
		GenQo genQo = new GenQo();
		genQo.setUrl(url);
		genQo.setUserName(userName);
		genQo.setPassword(password);
		genQo.setProjectPath(projectPath);// 项目地址
		genQo.setTableName(tableName);
		genQo.setBizName(bizName);// 业务名称

		genQo.setAuthor("格调先生");
		genQo.setProjectPackage("com.stylefeng.guns");// 项目的包
		genQo.setCorePackage(CoreFlag.class.getPackage().getName());// 核心模块的包
		genQo.setIgnoreTabelPrefix("cus_");
		genQo.setModuleName("cus");
		genQo.setParentMenuName("后台管理");

		WebGeneratorConfig webGeneratorConfig = new WebGeneratorConfig(genQo);
		webGeneratorConfig.doMpGeneration();
		webGeneratorConfig.doGunsGeneration();
	}

	private static void delete(String tableName, String bizName) {
		GenQo genQo = new GenQo();
		genQo.setUrl(url);
		genQo.setUserName(userName);
		genQo.setPassword(password);
		genQo.setProjectPath(projectPath);// 项目地址
		genQo.setTableName(tableName);
		genQo.setBizName(bizName);// 业务名称

		genQo.setAuthor("格调先生");
		genQo.setProjectPackage("com.stylefeng.guns");// 项目的包
		genQo.setCorePackage(CoreFlag.class.getPackage().getName());// 核心模块的包
		genQo.setIgnoreTabelPrefix("cus_");
		genQo.setModuleName("cus");
		genQo.setParentMenuName("后台管理");

		WebGeneratorConfig webGeneratorConfig = new WebGeneratorConfig(genQo);
		webGeneratorConfig.doMpGeneration();
		webGeneratorConfig.delete();
	}
}
