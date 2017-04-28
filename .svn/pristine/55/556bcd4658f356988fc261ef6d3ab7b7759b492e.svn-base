package com.jg.workflow.process.definition;

import com.alibaba.fastjson.JSONObject;
import com.jg.workflow.process.Process;
import com.jg.workflow.process.ProcessImpl;

/**
 * 表示流程定义当中连接线对象.
 *
 * @author yimin
 */

public class Link {
	private ProcessDefinitionImpl owner;
	private int id;
	private TaskDefinition from;
	private TaskDefinition to;
	private String condition;

	Link(ProcessDefinitionImpl definition, JSONObject structure) {
		this.owner = definition;

		this.id = structure.getInteger("id");
		this.from = definition.getTaskDefinition(structure.getInteger("from"));
		this.to = definition.getTaskDefinition(structure.getInteger("to"));
		this.condition = structure.getString("condition");

		this.from.addToLink(this);
		this.to.addFromLink(this);
	}

	public TaskDefinition getTo() {
		return to;
	}

	public TaskDefinition getFrom() {
		return from;
	}

	public String getCondition() {
		return condition;
	}

	public int getId() {
		return id;
	}

	public boolean isThrough(Process process) {
		Object value = ((ProcessImpl) process).calculateExpression("result.result = (" + condition + ")");
		return !Boolean.FALSE.equals(value);
	}

	public ProcessDefinitionImpl getOwner() {
		return owner;
	}


}
