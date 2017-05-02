package com.jg.workflow.process.definition;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.Persistence;
import com.jg.workflow.process.model.Model;

/**
 * 部署好的流程定义接口对象.
 *
 * @author yimin
 */
public interface ProcessDefinition extends Persistence {

	TaskDefinition getStartNode();

	TaskDefinition getEndNode();

	int getId();

	TaskDefinition getTaskDefinition(Integer taskId);

	ProcessDefinitionImpl deploy(Model model, int companyId, JSONObject content);

}
