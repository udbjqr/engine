package com.jg.workflow.exception;

/**
 * 流程实例为空.
 *
 * 此异常发生一般在获得流程实例时,或指定的流程查询方式,或值不正确.
 *
 * @author yimin
 */

public class ProcessInstanceIsNULL extends RuntimeException {
	public ProcessInstanceIsNULL(String message) {
		super(message);
	}
}
