package com.jg.workflow.task;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.identification.Company;
import com.jg.identification.User;

import java.util.List;

/**
 * 任务管理器接口.
 *
 * @author yimin
 */
public interface TaskManager {
	/**
	 * 对一个任务进行操作.
	 * <p>
	 * 此方法会调用任务相对应的模块操作来执行，并且在操作完成后调用相对应事件处理。
	 * 同时将流程向下一个任务调度。
	 *
	 * @param taskId    要完成的任务ID
	 * @param variables 任务所使用参数
	 * @return 返回执行结果对象
	 */
	HttpResult handle(int taskId, JSONObject variables);

	/**
	 * 对一个任务进行完成操作.
	 * <p>
	 * 此方法调用任务的完成操作.
	 *
	 * @param taskId 需要完成的任务对象.
	 */
	void complete(int taskId);

	/**
	 * 得到一个任务对象
	 *
	 * @param taskId 任务Id
	 * @return 任务对象
	 */
	Task getTask(int taskId);
	/**
	 *
	 */

	/**
	 * 将任务指定给对应的操作人.
	 * <p>
	 * 此方法执行后流程依然保持在原位，仅更换此任务对应的操作人.
	 * <p>
	 * 根据传入的名称，返回用“，”分隔的用户的名称.
	 * <p>
	 * 此对象提供用户、部门、岗位之间的关系，并且以一个字段来获得所需要的用户身份。
	 * <p>
	 * 使用"_"来分隔，前缀大写字母为指明类型。
	 * 例：“USERE101_E23_D29_R2_SD20R44”
	 * E:用户对象，后续数字为用户对象ID
	 * D：部门，后续数字为部门ID
	 * R：岗位，后续数据为岗位ID
	 * S：部门与岗位，后续必须也只能带一个部门和一个岗位。
	 * 每一个分隔为指定单一ID，需要多个可以多写
	 * <p>
	 * 提供的数据将会根据当时数据库内数据，给出对应的用户名称。
	 * <p>
	 * 多个用户可以重复，转换后仅提供单一个用户名。
	 * <p>
	 * 注：指名此方法的对象应确定以“USER”为开头,以上所有字符必须大写
	 *
	 * @param taskId 任务id
	 * @param users  需要指定的操作人列表，必须以“USER”为开头
	 */
	Task setAssignee(int taskId, String users);


	/**
	 * 得到我当前任务列表.
	 * <p>
	 * 此列表即为用户的待办列表
	 *
	 * @return 任务列表
	 */
	List<Task> getMyTasks();

	/**
	 * 对一个审核操作进行加签操作.
	 * <p>
	 * 可指定多人，指定多人将按顺序要求操作.
	 * <p>
	 * 加签操作与任务节点所指定模块与操作完全一致
	 * <p>
	 * 加签操作将导致本流程永久性增加加签的顺序，即再一次走到此节点时，加签操作同样需要操作。
	 * <p>
	 * 加签仅允许一次加签操作。多次无效。
	 * <p>
	 * 当针对一个已经在加签的节点进行驳回等跳转操作时，无论此次加签是否已经走到哪里，再次进入加签操作环节加签操作再次生效。
	 * <p>
	 * 此操作仅审核操作能使用
	 *
	 * @param taskId 需要加签的任务Id
	 * @param users  指定的操作人,使用','进行分隔的操作人。
	 */
	void insertAudit(int taskId, String users);

	/**
	 * 对一个审核操作的代签操作.
	 * <p>
	 * 代签仅能指定单一一个人做此操作。
	 *
	 * @param taskId 任务Id
	 * @param users  指定代办的人列表.见{@link #setAssignee(int taskId, String users)}
	 */
	void replaceAudit(int taskId, String users);

	/**
	 * 将操作驳回至最初任务操作节点.
	 *
	 * @param taskId 从哪一个任务进行的驳回操作。
	 */
	void reject(int taskId);

	/**
	 * 将当前任务驳回至某一个指定的任务节点.
	 * <p>
	 * 驳回任务指定的点将由上次操作人员再次进行操作.
	 * <p>
	 * 加签的操作无法被驳回至指定。
	 *
	 * @param taskId       从哪一个任务开始驳回
	 * @param targetTaskId 需要驳回至的点.
	 */
	void backTo(int taskId, int targetTaskId);

	/**
	 * 返回此任务对应的流程对象.
	 *
	 * @param taskId 任务的Id
	 * @return 流程对象，此值必定非空
	 */
	Process getProcessInstance(int taskId);

	/**
	 * 增加一个抄送人.
	 *
	 * @param taskId 任务的Id
	 * @param user   需要增加的抄送人
	 */
	void addCopyTo(int taskId, User user);

	/**
	 * 得到当前任务的抄送人列表.
	 *
	 * @param taskId 任务的Id
	 * @return 抄送人列表
	 */
	List<User> getCopyToUsers(int taskId);

	/**
	 * 从抄送人列表当中移除一个抄送人
	 *
	 * @param taskId 任务的Id
	 * @param user   需要移除的抄送人列表
	 */
	void removeCopyTo(int taskId, User user);

	/**
	 * 返回当前的企业对象.
	 *
	 * @return 企业对象
	 */
	Company getCompany();

	/**
	 * 指定一个未设置操作人的节点的操作人.
	 * <p>
	 * 此操作必须为未设置操作人的节点。已经设置操作人节点使用{@link #setAssignee(int, String)}
	 *
	 * @param taskId   需要设置的任务对象
	 * @param operator 指定的操作人列表
	 */
	void setEmptyTaskOperator(int taskId, String operator);
}


