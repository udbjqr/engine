package com.jg.workflow.expression;

import com.jg.common.util.StringUtil;
import com.jg.identification.Context;
import com.jg.workflow.process.Process;

/**
 * 返回系统指定的变量值.
 *
 * @author yimin
 */

public class SystemVarResolver implements Resolver {

	@Override
	public Object getValue(Process process, String name) {
		if (StringUtil.isEmpty(name)) {
			return null;
		}

		SystemVarName systemVarName = SystemVarName.valueOf(name.toUpperCase());

		switch (systemVarName) {
			case CURRENTOPERATOR:
				return Context.getCurrentOperatorUser();
		}

		return null;
	}


	enum SystemVarName {
		//当前操作人对象
		CURRENTOPERATOR
	}
}
