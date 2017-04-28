package com.jg.workflow.expression;

import com.alibaba.fastjson.JSONObject;
import com.jg.workflow.process.Process;

/**
 * create by 2017/4/7.
 *
 * @author yimin
 */

public class ProcessVarResolver implements Resolver {
	@Override
	public Object getValue(Process process, String name) {
		if (process == null) {
			return null;
		}

		JSONObject var = process.get("variable");
		if (var == null) {
			return null;
		}

		return var.get(name);
	}
}
