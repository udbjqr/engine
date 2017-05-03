package com.jg.workflow.expression;

import com.jg.common.util.StringUtil;
import com.jg.identification.Context;
import com.jg.workflow.process.Process;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 返回系统指定的变量值.
 * <p>
 * 定义此变量采用加前缀”S_“字符方式。用以区分名称.
 *
 * @author yimin
 */

public class SystemVarResolver implements Resolver {
	private final static Logger log = LogManager.getLogger(SystemVarResolver.class.getName());

	@Override
	public Object getValue(Process process, String name) {
		if (StringUtil.isEmpty(name)) {
			return null;
		}

		String s_name;

		if (("S_").equals(name.substring(0, 2))) {
			s_name = name.substring(2);
		} else {
			log.trace("系统变量解析未找到对应，名称：{}，只处理名称以'S_'开头数据。", name);
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
