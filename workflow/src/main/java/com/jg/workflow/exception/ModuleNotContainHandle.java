package com.jg.workflow.exception;

import com.jg.workflow.process.handle.Handle;
import com.jg.workflow.process.module.Module;

/**
 * 此异常指明指定的操作不属于当前模块.
 *
 * @author yimin
 */

public class ModuleNotContainHandle extends RuntimeException {
	public ModuleNotContainHandle(Module module, Handle handle) {
		super(String.format("模块%s未包含操作%s",
			module == null ? "NULL" : (String) module.get("module_name"),
			handle == null ? "NULL" : handle.getName()));
	}
}
