package com.yy.plugin.getchange;

public class DiffBean {
	private int index;
	private String s1;
	private String s2;

	public DiffBean() {
	}

	public DiffBean(int index, String s1, String s2) {
		super();
		this.index = index;
		this.s1 = s1;
		this.s2 = s2;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getS1() {
		return s1;
	}

	public void setS1(String s1) {
		this.s1 = s1;
	}

	public String getS2() {
		return s2;
	}

	public void setS2(String s2) {
		this.s2 = s2;
	}

	@Override
	public String toString() {
		return "DiffBean [index=" + index + "\ts1=" + s1 + "\ts2=" + s2 + "]";
	}

}
