package com.jg.workflow.process;

import com.jg.common.persistence.Persistence;
import com.jg.identification.Company;
import com.jg.identification.User;
import com.jg.workflow.process.definition.ProcessDefinition;
import com.jg.workflow.task.Task;

import java.util.List;
import java.util.Map;

/**
 * 流程实例接口.
 *
 * @author yimin
 */
public interface Process extends Persistence {
	/**
	 * 返回此流程的发起者
	 *
	 * @return 流程发起者的登录名.
	 */
	String getStartUser();

	/**
	 * 返回关注此流程的所有人
	 *
	 * @return 当前关注此流程的所有人
	 */
	List<User> getAttentionUsers();

	/**
	 * 为本流程增加一个关注者
	 *
	 * @param user 需要增加的关注人
	 */
	void addAttentionUsers(User user);

	/**
	 * 从关注者列表当中删除一个关注者
	 *
	 * @param user 需要删除的关注者
	 */
	void removeAttentionUsers(User user);

	/**
	 * 将图形化的流程定义图以SVG形式返回.
	 * <p>
	 * 将当前流程保存的svg图形模式
	 *
	 * @return svg格式的流程定义图形
	 */
	String getSVGXML();


	/**
	 * 返回此流程对应的流程定义对象.
	 *
	 * @return 流程定义对象.
	 */
	ProcessDefinition getDefinition();

	/**
	 * 得到当前流程的Id.
	 * <p>
	 * ID做为一个流程的唯一标识。
	 *
	 * @return ID
	 */
	int getId();

	/**
	 * 此流程是否被挂起
	 *
	 * @return true:是被挂起
	 */
	boolean isSuspended();

	/**
	 * 指明此流程是否已经结束.
	 *
	 * @return true: 已经结束
	 */
	boolean isEnded();

	/**
	 * 得到流程的变量集合.
	 * <p>
	 * 流程可将此变量进行修改和使用。
	 * 此变量集合将长期保存。
	 * 此变量内仅能保存简单类型。
	 *
	 * @return 集合对象
	 */
	Map<String, Object> getProcessVariables();

	/**
	 * 返回当前流程名称.
	 *
	 * @return 流程名称
	 */
	String getName();

	/**
	 * 返回当前流程对象的企业对象.
	 *
	 * @return 企业对象
	 */
	Company getCompany();

	/**
	 * 设置流程变量的值.
	 * <p>
	 * 如果值为null,则将它从变量当中删除。
	 *
	 * @param name  变量的名称
	 * @param value 变量的值
	 */
	void setVariable(String name, Object value);

	/**
	 * 得到流程变量的值.
	 *
	 * @param name 变量的名称
	 * @return 变量的值，如未找到返回null
	 */
	Object getVariable(String name);

	/**
	 * 返回当前流程当中可被执行的任务列表.
	 *
	 * @return 任务列表
	 */
	List<Task> getActivityTasks();

	void setModuleValueId(Integer moduleId, Integer id);

	Integer getModuleInstanceId(Integer moduleId);
}
