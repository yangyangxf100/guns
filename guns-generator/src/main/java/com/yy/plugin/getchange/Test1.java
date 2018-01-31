package com.yy.plugin.getchange;

import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class Test1 {

	public static void main(String[] args) throws Exception {
		String s1 = getOutPut("E:/xx/KaoBannerController_V2.java");
		String s2 = getOutPut("E:/xx/KaoDoctertypeController_V2.java");

		List<DiffBean> list = getDiffToList(s1, s2);

		for (DiffBean d : list) {
			System.out.println(d);
		}

	}

	public static List<DiffBean> getDiffToList(String s1, String s2) {
		List<DiffBean> list = new ArrayList<DiffBean>();
		String[] s1Arr = s1.split(" ");
		String[] s2Arr = s2.split(" ");

		for (int i = 0; i < s1Arr.length; i++) {
			if (!s1Arr[i].equals(s2Arr[i])) {
				String ss1 = s1Arr[i];
				String ss2 = s2Arr[i];
				

				String s11 = "";
				String s22 = "";
				
				
				//替换掉前面相同的部分
				for (int j = 0; j < ss1.length(); j++) {
					if (ss2.indexOf(ss1.substring(0, j)) == -1) {
						ss1 = ss1.substring(j - 1);
						ss2 = ss2.substring(j - 1);
						break;
					}
				}

				//替换掉后面相同的步伐
				for (int j = ss1.length(); j >= 0; j--) {
					if (ss2.indexOf(ss1.substring(j)) == -1) {
						s11 = ss1.substring(0, j + 1);// 去掉相同的
						s22 = ss2.replaceAll(ss1.substring(j + 1), "");// 去掉相同的
						break;
					}
				}

//				list.add(new DiffBean(i, s1Arr[i], s2Arr[i]));
				list.add(new DiffBean(i, s11, s22));
			}
		}
		return list;
	}

	public static String getOutPut(String filePath) throws Exception {
		StringBuffer sb = new StringBuffer("");
		LineNumberReader lineNumberReader = new LineNumberReader(new FileReader(filePath));

		String tmp = "";
		while ((tmp = lineNumberReader.readLine()) != null) {
			sb.append(tmp.replaceAll(";", "").replaceAll("\\)", "").replaceAll("\\.", " ")+" ");
		}

		lineNumberReader.close();

		return sb.toString();
	}

}
