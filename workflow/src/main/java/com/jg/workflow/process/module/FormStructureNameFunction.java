package com.jg.workflow.process.module;


import com.alibaba.fastjson.JSONObject;

/**
 * 定义一个函数式接口
 */

@FunctionalInterface
public interface FormStructureNameFunction {
	void setDataByField(JSONObject json);
}
