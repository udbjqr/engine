package com.jg.workflow.expression;

import com.jg.workflow.process.Process;
import com.jg.workflow.process.handle.Handle;
import com.jg.workflow.process.module.ModuleFactory;
import com.jg.workflow.process.module.ModuleImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.jg.common.InternalConstant.MODULE_PREFIX;

/**
 * 模型的解析获得值的实际类.
 *
 * 模型变量的写法：
 * M模块定义Id_字段名称.
 * create by 2017/4/7.
 *
 * @author yimin
 */

public class ModuleVarResolver implements Resolver {
	private final static Logger log = LogManager.getLogger(ModuleVarResolver.class.getName());

	@Override
	public Object getValue(Process process, String name) {
		if (process == null) {
			return null;
		}

		String[] s = name.split("_");
		if (s.length != 2) {
			log.trace("模块变量解析未对应，名称：" + name);
			return null;
		}

		try {
			Integer moduleDefinitionId = Integer.parseInt(s[0].split(MODULE_PREFIX)[1]);
			Integer moduleInstanceId = process.getModuleInstanceId(moduleDefinitionId);

			ModuleImpl module = ModuleFactory.getModuleFactory().getObject("id", moduleDefinitionId);

			if (module == null) {
				log.trace("模块变量解析，未找到对应模块。" + moduleInstanceId);
				return null;
			}

			Handle handle = module.getQuestHandle();
			return handle.getFormValue(s[1], moduleInstanceId);
		} catch (NumberFormatException e) {
			log.trace("模块变量解析，无法解析模块名。退出" + name);
			return null;
		}

	}
}
