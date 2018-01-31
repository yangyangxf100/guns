package com.yy.plugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.yy.plugin.file.CodeGenerator;
import com.yy.plugin.file.PathConfig;
import com.yy.plugin.pdm.bean.Table;
import com.yy.plugin.pdm.xml.PdmParse;

public class Main {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		String projectPath = "D:/gener/generMvc";
		try {
//			String pdmFile = "D:/杨洋/杨洋/32 active_2016/2016-03-07 医考通/db/考试.pdm";
//			String pdmFile = "D:/杨洋2016/OneDrive/work/798/doc/10 图书评分/db/db.pdm";
			String pdmFile = "D:/db.pdm";

//			
			String tablesArr = "lib_jie_books".toLowerCase();// 要生成的表，以，分隔，如果为空，则生成所有的
			PdmParse parse = new PdmParse();
			Map<String, Table> tables;

			tables = parse.parse(pdmFile);

			for (String key : tables.keySet()) {
				Table t = tables.get(key);
				Map<String, Table> tmpMap = new HashMap<String, Table>();
				tmpMap.put(key, t);
				String govName = "gochinatv";
				String appName = "cms";
				String moduleName = "web";

				PathConfig pc = new PathConfig();
				pc.setGovName(govName);
				pc.setAppName(appName);
				pc.setModuleName(moduleName);
				pc.setPdmFile(pdmFile);
				pc.setProjectPath(projectPath);

				CodeGenerator cg = new CodeGenerator();

				if (tablesArr.length() > 0) {
					if (tablesArr.indexOf(t.getCode().toLowerCase()) != -1) {
						cg.generator(tmpMap, pc);
					}
				} else {
					cg.generator(tmpMap, pc);
				}
			}

			PathConfig pc = new PathConfig();
			pc.setGovName("");
			pc.setAppName("");
			pc.setModuleName("");
			pc.setPdmFile(pdmFile);
			pc.setProjectPath("");

			CodeGenerator cg = new CodeGenerator();
			cg.printUrl(tables, pc);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 打开生成的文件目录
		try {
			String[] cmd = new String[5];
			cmd[0] = "cmd";
			cmd[1] = "/c";
			cmd[2] = "start";
			cmd[3] = " ";
			cmd[4] = projectPath;
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String parseTableName(String str) {
		if ((str.startsWith("T_")) || (str.startsWith("t_"))) {
			str = str.substring(2);
		}
		StringBuffer sb = new StringBuffer();

		if ((str != null) && (str.length() > 0)) {
			String[] s = str.toLowerCase().split("_");
			if (s.length > 1) {
				sb.append(s[0]);
				for (int i = 1; i < s.length; i++) {
					sb.append(upCaseFirstChar(s[i]));
				}
			} else {
				sb.append(s[0]);
			}
		}

		return sb.toString();
	}

	private static String upCaseFirstChar(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}
