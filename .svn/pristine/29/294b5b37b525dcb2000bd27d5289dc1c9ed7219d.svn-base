package com.jg.workflow.process;

import com.jg.identification.Company;
import com.jg.identification.User;
import com.jg.workflow.exception.ModelHasbeenDeployed;
import com.jg.workflow.process.definition.ProcessDefinition;
import com.jg.workflow.task.Task;

import java.util.List;

/**
 * 流程控制器接口.
 * <p>
 * 此控制器每个企业一个。所提供所有操作均操作企业内.
 *
 * @author yimin
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public interface ProcessManager {
	/**
	 * 得到此流程控制器对应的企业对象.
	 *
	 * @return 企业对象
	 */
	Company getCompany();

	/**
	 * 得到我正在运行的流程.
	 * <p>
	 * 此方法仅返回所有者为指定用户流程.
	 * <p>
	 * 如limit = -1 则返回所有数据，如需要所有所有值使用:{@link #getMyInProcesses(User, String)} 方法。
	 *
	 * @param user      用户对象
	 * @param offset    返回的数据从多少开始取
	 * @param limit     需要返回的条数
	 * @param condition 查询的条件，此处必须带有order by 字段，否则可能返回错乱
	 * @return 正在运行的流程列表
	 */
	List<Process> getMyInProcesses(User user, String condition, int offset, int limit);

	/**
	 * 返回所有我正在运行的流程
	 *
	 * @param condition 查询的条件
	 * @return 正在运行的流程列表
	 */
	default List<Process> getMyInProcesses(User user, String condition) {
		return getMyInProcesses(user, condition, 0, -1);
	}


	/**
	 * 得到我已完结的流程
	 * <p>
	 * 如limit = -1 则返回所有数据，如需要所有所有值使用:{@link #getMyFinishedProcesses(User, String)} 方法。
	 *
	 * @param offset    返回的数据从多少开始取
	 * @param limit     需要返回的条数
	 * @param condition 查询的条件，此处必须带有order by 字段，否则可能返回错乱
	 * @return 正在运行的流程列表
	 */
	List<Process> getMyFinishedProcesses(User user, String condition, int offset, int limit);

	/**
	 * 得到我已完结的流程
	 *
	 * @param condition 查询的条件
	 * @return 正在运行的流程列表
	 */
	default List<Process> getMyFinishedProcesses(User user, String condition) {
		return getMyFinishedProcesses(user, condition, 0, -1);
	}


	/**
	 * 返回当前企业所有流程定义对象.
	 * <p>
	 * 如limit = -1 则返回所有数据，如需要所有所有值使用:{@link #getAllProcessDefinitions(String condition, int companyId)} 方法。
	 *
	 * @param offset    返回的数据从多少开始取
	 * @param limit     需要返回的条数
	 * @param condition 查询的条件，此处必须带有order by 字段，否则可能返回错乱
	 * @param companyId 企业的Id
	 * @return 所有流定义对象
	 */
	List<ProcessDefinition> getAllProcessDefinitions(String condition, int companyId, int offset, int limit);

	/**
	 * 返回所有流程定义对象.
	 *
	 * @param condition 查询的条件
	 * @return 所有流程定义对象
	 */
	default List<ProcessDefinition> getAllProcessDefinitions(String condition, int companyId) {
		return getAllProcessDefinitions(condition, companyId, 0, -1);
	}

	/**
	 * 部署一个流程定义.
	 * <p>
	 * 此操作将一个模型部署至流程当中，做为一个可以启动的流程。
	 * <p>
	 * 部署的流程定义是一个流程的模板，可根据此模板启动流程.本身并非流程对象。
	 * <p>
	 * 部署流程将新增加一个流程定义，之前的流程定义将依然可以工作。
	 * <p>
	 * 在查看流程定义时，相同名称的流程名称只会显示最后一个，并将显示出版本号.
	 * <p>
	 * 已经被部署过的模型无法第二次部署.当一个模型被修改保存过将允许再次部署.
	 *
	 * @param modelId 模型对象的Id
	 * @return 部署之后的流程定义对象
	 * @throws ModelHasbeenDeployed 指明的模型已经被部署了,除非修改后保存才能再次被部署
	 * @throws NullPointerException 传入的参数model为Null
	 */
	ProcessDefinition deploymentProcess(int modelId) throws ModelHasbeenDeployed;

	/**
	 * 暂停一个流程定义.
	 * <p>
	 * 暂停的流程定义将无法新增加流程.
	 *
	 * @param deploymentId 流程部署Id
	 */
	void suspendProcessDefinition(int deploymentId);

	/**
	 * 激活一个流程定义.
	 * <p>
	 * 激活的流程定义将可继续新增加流程.
	 *
	 * @param deploymentId 流程部署Id
	 */
	void activateProcessDefinition(int deploymentId);

	/**
	 * 删除一个流程定义.
	 * <p>
	 * 删除的流程定义将无法再被激活.
	 * <p>
	 * 删除流程之前流程需要是被暂停状态
	 *
	 * @param deploymentId 流程部署Id
	 */
	void removeProcessDefinition(int deploymentId);

	/**
	 * 撤消一个流程.
	 *
	 * @param processId 需要撤消的流程
	 */
	void cancelProcess(int processId);

	/**
	 * 对一个流程进行催办操作.
	 * <p>
	 * 催办操作将对当前有操作权限的所有人进行催促消息，如有多个节点可操作，将发送所有对其所做操作进行催办.
	 * <p>
	 * 当前仅启动相关事件，具体催办操作需要在事件当中处理。
	 *
	 * @param processId 需要撤消的流程
	 */
	void urgeProcessing(int processId);

	/**
	 * 暂停一个流程的执行.
	 * <p>
	 *
	 * @param processId 流程的Id
	 */
	void suspendProcess(int processId);

	/**
	 * 激活一个流程.
	 * <p>
	 *
	 * @param processId 流程的Id
	 */
	void activateProcess(int processId);

	/**
	 * 启动一个流程
	 * <p>
	 * 使用当前登录的用户来启动此流程
	 *
	 * @param processDefinitionId 流程定义的Id
	 */
	void startProcess(int processDefinitionId);


	/**
	 * 启动一个流程
	 * <p>
	 * 使用当前登录的用户来启动此流程
	 *
	 * @param processDefinitionName 流程定义的名称
	 */
	void startProcessByName(String processDefinitionName);

	/**
	 * 从任务Id获得流程对象
	 * <p>
	 * 此方法将不进行权限控制的判断.
	 *
	 * @param taskId 任务Id
	 * @return 流程对象
	 */
	Process getProcessByTaskId(int taskId);


	/**
	 * 返回需要当前操作人员设置下一步操作对象的任务对象列表.
	 *
	 * @return 需要设置操作人的对象列表
	 */
	List<Task> getNeedSetOperatorsTask();

}





















