package com.jg.workflow.exception;

/**
 * 流程已经部署异常.
 *
 * @author yimin
 */

public class ModelAlreadyDeploy extends RuntimeException {
	public ModelAlreadyDeploy(String name) {
		super("模型:" + name + "已经被部署过，当前无法再次部署。可进行修改后部署。");
	}
}
