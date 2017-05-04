package com.jg.workflow.process.definition;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.AbstractPersistence;
import com.jg.common.persistence.AbstractPersistenceFactory;
import com.jg.common.persistence.Field;
import com.jg.common.persistence.WriteValueException;
import com.jg.workflow.exception.ModelAlreadyDeploy;
import com.jg.workflow.exception.ProcessDefinitionNotIntegrity;
import com.jg.workflow.process.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jg.common.sql.DBHelperFactory.DB_HELPER;


/**
 * 流程定义的实现对象.
 *
 * @author yimin
 */

public class ProcessDefinitionImpl extends AbstractPersistence implements ProcessDefinition {
	private Map<Integer, TaskDefinition> tasks = new HashMap<>();
	private List<Link> links = new ArrayList<>();
	private TaskDefinition startNode;
	private TaskDefinition endNode;
	private int id;

	public int getId() {
		return id;
	}

	ProcessDefinitionImpl(AbstractPersistenceFactory factory) {
		super(factory);
	}

	public ProcessDefinitionImpl deploy(Model model, int companyId, JSONObject content) {
		if ((int) model.get("flag") == 0) {
			set("content", content);
			set("view_information", model.get("view_information"));
			set("name", model.get("name"));
			set("version", DB_HELPER.selectOneValues("select nextval('seq_process_def_version')"));
			set("company_id", companyId);
			set("category", model.get("category"));
			set("description", model.get("description"));
			set("model_content",model.get("content"));
			set("flag", 1);

			model.set("flag", 1);
			model.flush();
		} else {
			throw new ModelAlreadyDeploy(model.get("name"));
		}

		return this;
	}

	private void buildStructure(JSONObject content) {
		links.clear();
		tasks.clear();
		startNode = null;
		endNode = null;

		JSONArray array = content.getJSONArray("nodes");
		array.forEach(t -> {
			JSONObject task = (JSONObject) t;
			tasks.put(task.getInteger("id"), new TaskDefinition(this, task));
		});

		array = content.getJSONArray("links");
		array.forEach(t -> links.add(new Link(this, (JSONObject) t)));

		checkDefinitionIntegrity();
	}

	private void checkDefinitionIntegrity() {
		tasks.forEach((k, node) -> {
			if (node.getFromLinks().isEmpty()) {
				if (node.getTaskType() == TaskType.START) {
					this.startNode = node;
				} else {
					throw new ProcessDefinitionNotIntegrity("流程节点定义有误，节点需要有接入对象。对象:" + node.getName());
				}
			}

			if (node.getToLinks().isEmpty()) {
				if (node.getTaskType() == TaskType.END) {
					this.endNode = node;
				} else {
					throw new ProcessDefinitionNotIntegrity("流程节点定义有误，节点需要有向下链接.对象:" + node.getName());
				}
			}

			if ((node.getTaskType() == TaskType.USERTASK || node.getTaskType() == TaskType.MULTITASK) && (node.getModule() == null || node.getHandle() == null)) {
				throw new ProcessDefinitionNotIntegrity("流程节点定义有误，节点必须指定模块与操作.节点对象:" + node.getName());
			}
		});

		links.forEach(link -> {
			if (link.getTo() == null || link.getFrom() == null) {
				throw new ProcessDefinitionNotIntegrity("流程连接线定义有误，连接线需要两头对接。线:" + link.getId());
			}
		});
		//		throw new ProcessDefinitionNotIntegrity("流程定义有误，请检查。对象:");
	}

	public TaskDefinition getTaskDefinition(Integer taskId) {
		return tasks.get(taskId);
	}

	public TaskDefinition getStartNode() {
		return startNode;
	}

	public TaskDefinition getEndNode() {
		return endNode;
	}


	@Override
	public synchronized void set(Field field, Object value) throws WriteValueException {
		super.set(field, value);

		if (field.name.equals("id")) {
			this.id = (Integer) value;
		} else if (field.name.equals("content")) {
			buildStructure(get("content"));
		}
	}
}
