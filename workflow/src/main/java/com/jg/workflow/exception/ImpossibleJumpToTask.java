package com.jg.workflow.exception;

/**
 * 流程中跳转任务出现异常.
 * create by 2017/4/10.
 *
 * @author yimin
 */

public class ImpossibleJumpToTask extends RuntimeException {
	public ImpossibleJumpToTask(String s) {
		super(s);
	}
}
