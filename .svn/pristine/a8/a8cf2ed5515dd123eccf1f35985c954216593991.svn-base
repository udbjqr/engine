package com.jg.workflow.expression;

import com.jg.common.util.StringUtil;
import com.jg.identification.Context;
import com.jg.workflow.process.Process;

/**
 * 返回系统指定的变量值.
 * <p>
 * 定义此变量采用加前缀”S_“字符方式。用以区分名称.
 *
 * @author yimin
 */

public class SystemVarResolver implements Resolver {

	@Override
	public Object getValue(Process process, String name) {
		if(StringUtil.isEmpty(name)){
			return null;
		}

		String s_name;

		if (("S_").equals(name.substring(0, 2))) {
			s_name = name.substring(2);
		}else{
			return null;
		}

		SystemVarName systemVarName = SystemVarName.valueOf(s_name.toUpperCase());

		switch (systemVarName) {
			case CURRENTOPERATOR:
				return Context.getCurrentOperatorUser().getId();
		}

		return null;
	}


	enum SystemVarName {
		//当前操作人对象
		CURRENTOPERATOR
	}
}
