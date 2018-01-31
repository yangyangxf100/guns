package com.yy.plugin;

import java.util.Map;

import com.yy.plugin.file.CodeGenerator;
import com.yy.plugin.file.PathConfig;
import com.yy.plugin.pdm.bean.Table;
import com.yy.plugin.pdm.xml.PdmParse;

public class PrintDoc {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try { 
			
			//String pdmFile = "E:/kuanzheng/code/customer/docs/分类表.pdm";
			String pdmFile = "D:/杨洋/杨洋/32 active_2016/2016-03-07 医考通/db/考试.pdm";

			
			PdmParse parse = new PdmParse();
			Map<String, Table> tables;

			tables = parse.parse(pdmFile);
 
			
			PathConfig pc = new PathConfig();
			pc.setGovName("");
			pc.setAppName("");
			pc.setModuleName("");
			pc.setPdmFile(pdmFile);
			pc.setProjectPath("");

			
			CodeGenerator cg = new CodeGenerator();
			cg.printDoc(tables,pc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
	} 
}
