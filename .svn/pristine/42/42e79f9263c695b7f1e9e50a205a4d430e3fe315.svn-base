package com.jg.workflow.process.definition;


import com.alibaba.fastjson.JSONObject;
import com.jg.workflow.exception.ModuleNotContainHandle;
import com.jg.workflow.process.handle.Handle;
import com.jg.workflow.process.handle.HandleFactory;
import com.jg.workflow.process.module.Module;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.jg.workflow.process.module.ModuleFactory.MODULE_FACTORY;

/**
 * 流程定义使用的任务对象.
 * <p>
 * 此任务对象对应流程定义中每一个节点.
 *
 * @author yimin
 */

public class TaskDefinition {
	private final ProcessDefinitionImpl definition;

	private List<Link> fromLinks = new ArrayList<>();
	private List<Link> toLinks = new ArrayList<>();
	private final int id;
	private String name;
	private String description;
	private Module module;
	private Handle handle;
	private String assignees;
	//到期时间
	private Long durationtime;
	private Set<String> canSeeColumn;
	private Set<String> canModifyColumn;
	private TaskType taskType;

	public List<Link> getFromLinks() {
		return fromLinks;
	}

	public List<Link> getToLinks() {
		return toLinks;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Module getModule() {
		return module;
	}

	public Handle getHandle() {
		return handle;
	}

	public String getAssignees() {
		return assignees;
	}

	public Long getDurationtime() {
		return durationtime == null ? 0 : durationtime;
	}

	public Set<String> getCanSeeColumn() {
		return canSeeColumn;
	}

	public Set<String> getCanModifyColumn() {
		return canModifyColumn;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	TaskDefinition(ProcessDefinitionImpl definition, JSONObject structure) {
		this.definition = definition;
		this.id = structure.getInteger("id");

		this.name = structure.getString("name");
		this.description = structure.getString("description");
		this.module = MODULE_FACTORY.getObject("id", structure.getInteger("module"));
		this.handle = HandleFactory.getHandle(structure.getInteger("handle"));
		this.assignees = structure.getString("assignees");
		try {
			this.durationtime = Long.parseLong(structure.getString("durationTime"));
		} catch (NumberFormatException e) {
			this.durationtime = -1L;
		}

		this.canModifyColumn = getSet(structure.get("canModifyColumn"));
		this.canSeeColumn = getSet(structure.get("canSeeColumn"));
		this.taskType = TaskType.valueOf(structure.getString("type"));

		if (module != null && !module.containHandle(handle)) {
			throw new ModuleNotContainHandle(module, handle);
		}

	}

	void addFromLink(Link fromLink) {
		fromLinks.add(fromLink);
	}

	void addToLink(Link toLink) {
		toLinks.add(toLink);
	}

	private Set<String> getSet(Object object) {
		if (object != null && object instanceof String) {
			HashSet<String> set = new HashSet<>();

			for (String str : ((String) object).split(",")) {
				set.add(str);
			}
			return set;
		}
		return null;
	}


}