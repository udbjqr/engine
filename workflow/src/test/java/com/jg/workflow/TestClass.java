package com.jg.workflow;

/**
 * create by 2017/4/10.
 *
 * @author yimin
 */

public class TestClass {
	public int ab = 999;

	public String getValue() {
		return "这是一个方法";
	}

	public int getb() {
		return ab;
	}

	public TestClass(int b) {
		this.ab = b;
	}

	public int getAb(){
		return ab;
	}

	public void setAb(int ab) {
		this.ab = ab;
	}

	@Override
	public String toString() {
		return "ab" + ab;
	}


}
