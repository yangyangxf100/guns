package com.yy.plugin.getchange;

public class 比较单词不同的部分 {

	/**
	 * 比较一个单词的不同地方
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String ss1 = "com.gochinatv.cms.po.KaoBannerEntity;";
		String ss2 = "com.gochinatv.cms.po.KaoDoctertypeEntity;";

		String s11 = "";
		String s22 = "";

		// 替换掉前面相同的部分
		for (int j = 0; j < ss1.length(); j++) {
			if (ss2.indexOf(ss1.substring(0, j)) == -1) {
				ss1 = ss1.substring(j - 1);
				ss2 = ss2.substring(j - 1);
				break;
			}
		}

		// 替换掉后面相同的步伐
		for (int j = ss1.length(); j >= 0; j--) {
			if (ss2.indexOf(ss1.substring(j)) == -1) {
				s11 = ss1.substring(0, j + 1);// 去掉相同的
				s22 = ss2.replaceAll(ss1.substring(j + 1), "");// 去掉相同的
				break;
			}
		}

		System.out.println(s11 + "\t" + s22);

	}

}
