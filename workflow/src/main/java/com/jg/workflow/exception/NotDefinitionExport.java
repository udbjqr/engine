package com.jg.workflow.exception;

/**
 * 指明流程有部分参数未定义
 *
 * @author yimin
 */

public class NotDefinitionExport extends RuntimeException {

	public NotDefinitionExport(String message) {
		super(message);
	}
}
