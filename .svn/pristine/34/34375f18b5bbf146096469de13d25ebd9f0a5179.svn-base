package com.jg.workflow.task;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.Persistence;
import com.jg.common.result.ResultCode;
import com.jg.workflow.process.Process;

import java.util.Set;

/**
 * 任务接口.
 *
 * @author yimin
 */
public interface Task extends Persistence {
	Integer getId();

	String getName();

	/**
	 * 返回允许查看的列表.
	 * <p>
	 * 如果内容为空，则允许所有.
	 *
	 * @return 列表
	 */
	Set<String> getCanSeeColumn();

	/**
	 * 返回允许修改的列表.
	 * <p>
	 * 如果内容为空，则允许所有.
	 *
	 * @return 列表
	 */
	Set<String> getCanModifyColumn();


	/**
	 * 得到当前任务所对应的流程对象.
	 *
	 * @return 流程对象
	 */
	Process getProcess();

	/**
	 * 执行流程的动作.
	 */
	ResultCode run(JSONObject variables);

	/**
	 * 流程执行完毕.
	 */
	void complete();
}

