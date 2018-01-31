package com.stylefeng.guns.generator.action.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class LineNumberReaderUtils {
	public static void main(String[] args) throws Exception {

		List<String> list = getMsgWithUtf8("D:/1.txt");
		for (String s : list) {
			if (s.indexOf("http") != -1) {
				System.out.println(s);
			}
		}

		for (String s : list) {
			if (s.indexOf("http") != -1) {
				System.out.println(s.substring(0, s.indexOf(" ")));
			}
		}

	}

	public static List<String> getMsgWithUtf8(String filePath) throws Exception {
		List<String> list = new ArrayList();
		LineNumberReader lineNumberReader = new LineNumberReader(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8")));

		String tmp = "";
		while ((tmp = lineNumberReader.readLine()) != null) {
			list.add(tmp.trim());
		}

		lineNumberReader.close();

		return list;
	}

	public static List<String> getMsg(String filePath) throws Exception {
		List<String> list = new ArrayList();
		LineNumberReader lineNumberReader = new LineNumberReader(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "GBK")));

		String tmp = "";
		while ((tmp = lineNumberReader.readLine()) != null) {
			list.add(tmp.trim());
		}

		lineNumberReader.close();

		return list;
	}

	public static String getMsgForStr(String filePath) throws Exception {
		StringBuffer sb = new StringBuffer();
		LineNumberReader lineNumberReader = new LineNumberReader(new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "GBK")));

		String tmp = "";
		while ((tmp = lineNumberReader.readLine()) != null) {
			sb.append(tmp.trim());
		}

		lineNumberReader.close();

		return sb.toString();
	}

}
