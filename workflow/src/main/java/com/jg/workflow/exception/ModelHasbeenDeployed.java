package com.jg.workflow.exception;

/**
 * 模型已经被部署异常.
 *
 * @author yimin
 */

public class ModelHasbeenDeployed extends Exception {
	public ModelHasbeenDeployed(String modelId, String companyName) {
		super("为企业" + companyName + "部署模型时出现异常:模型" + modelId + " 已经被部署.");
	}
}
