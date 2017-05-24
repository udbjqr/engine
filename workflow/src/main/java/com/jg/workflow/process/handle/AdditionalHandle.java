package com.jg.workflow.process.handle;

/**
 * 此枚举为一个配置类,用以指明操作可以附加哪些其他操作.
 * create by 17/5/18.
 *
 * @author yimin
 */

public class AdditionalHandle {
	//驳回操作
	public static AdditionalHandle TurnDown = new AdditionalHandle("TurnDown");
	//审核通过操作
	public static AdditionalHandle Pass = new AdditionalHandle("Pass");

	private final String name;

	private AdditionalHandle(String name) {
		this.name = "\"" + name + "\"";
	}

	@Override
	public String toString() {
		return name;
	}
}
