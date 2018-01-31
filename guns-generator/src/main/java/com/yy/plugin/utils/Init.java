package com.yy.plugin.utils;

import java.io.FileReader;
import java.io.LineNumberReader;

/**
 * 生成模板
 * 
 * @author gochinatv
 * 
 */
public class Init {
	public static void main(String[] args) throws Exception {
		String ret = generTemplet("D:/libStudentList.jsp");
		System.out.println(ret);
	}

	/**
	 * 获得文件内容
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String getContentFromFile(String filePath) throws Exception {
		StringBuffer sb = new StringBuffer();
		LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(filePath));

		String tmp = "";
		while ((tmp = lineNumberReader.readLine()) != null) {
			sb.append(tmp + "\n");
		}

		lineNumberReader.close();

		return sb.toString();
	}

	/**
	 * 生成 模板
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String generTemplet(String filePath) throws Exception {
		StringBuffer sb = new StringBuffer("ClassFile file = new ClassFile(path.getXx(), table.getClassName() + \"Controller.java\");\\n");
		LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(filePath));

		String tmp = "";
		while ((tmp = lineNumberReader.readLine()) != null) {
			sb.append("file.write(\"" + tmp.replaceAll("\"", "\\\\\"") + "\\n\");\n");
		}

		sb.append("\nfile.closeWriter();\n");
		lineNumberReader.close();

		return sb.toString();
	}
}
