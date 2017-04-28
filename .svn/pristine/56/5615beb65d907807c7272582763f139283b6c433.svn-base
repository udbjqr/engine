package com.jg.workflow.task;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.ResultCode;
import com.jg.common.util.StringUtil;
import com.jg.identification.Context;
import com.jg.identification.User;
import com.jg.workflow.event.TaskBeforeRun;
import com.jg.workflow.event.TaskCompleteEvent;
import com.jg.workflow.event.TaskEndRun;
import com.jg.workflow.event.TaskTurnIn;
import com.jg.workflow.exception.ProcessRunningException;
import com.jg.workflow.expression.ExpressionControl;
import com.jg.workflow.process.ProcessDetail;
import com.jg.workflow.process.ProcessImpl;
import com.jg.workflow.process.definition.Link;
import com.jg.workflow.process.definition.TaskDefinition;
import com.jg.workflow.util.UserExpressionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.jg.common.sql.DBHelperFactory.DB_HELPER;
import static com.jg.workflow.event.EventMangerImpl.EVENT_MANGER;

/**
 * 执行类的虚类
 *
 * @author yimin
 */

public abstract class AbstractPerformer {
	private static final Logger log = LogManager.getLogger(AbstractPerformer.class.getName());

	protected final TaskImpl task;
	protected final ProcessImpl process;
	protected final ProcessDetail processDetail;

	AbstractPerformer(TaskImpl task) {
		this.task = task;
		this.process = (ProcessImpl) task.getProcess();
		this.processDetail = process.getDetail(task.getId());
	}

	static AbstractPerformer getPerformer(TaskImpl task) {
		switch (task.getDefinition().getTaskType()) {
			case END:
				return new EndPerformer(task);
			case START:
				return new StartPerformer(task);
			case USERTASK:
				return new UserTaskPerformer(task);
			case MULTITASK:
				return new MultiTaskPerformer(task);
			case SERIALGATEWAY:
				return new SerialGatewayPerformer(task);
			case PARALLELGATEWAY:
				return new ParallelGatewayPerformer(task);
			default:
				return null;
		}
	}

	private void checkProcessRunning() {
		if (!process.IsNormal()) {
			throw new ProcessRunningException(process.getId(), "流程非正常运行状态。");
		}
	}

	final void turnIn(Link link) {
		checkProcessRunning();

		EVENT_MANGER.triggerEvent(new TaskTurnIn(task));
		turnInTask(link);
	}

	final ResultCode run(JSONObject variables) {
		checkProcessRunning();

		EVENT_MANGER.triggerEvent(new TaskBeforeRun(task, variables));
		ResultCode resultCode = execute(variables);
		EVENT_MANGER.triggerEvent(new TaskEndRun(task));

		return resultCode;
	}

	final void complete() {
		checkProcessRunning();

		if (handleInsertAuditComplete()) {
			return;
		}

		completeSelf();
		EVENT_MANGER.triggerEvent(new TaskCompleteEvent(task));
		transfer();

		process.closeTask(task);
	}

	/**
	 * 处理加签时的动作
	 *
	 * @return true 有加签
	 */
	private boolean handleInsertAuditComplete() {
		if (task.isInsterAudit()) {
			final int[] values = new int[]{0, 0, 0};

			DB_HELPER.selectWithRow("select id,sponsor,accumulative from process_insert_audit where flag = 1 and task_id = " + task.getId() + " order by accumulative desc limit 1", set -> {
				values[0] = set.getInt("id");
				values[1] = set.getInt("sponsor");
				values[2] = set.getInt("accumulative");
			});

			if (values[2] > 0) {
				ArrayList<String> strings = new ArrayList<>();
				strings.add("delete from process_run_control where task_id =" + task.getId());
				strings.add("update process_insert_audit set flag = 0 where id =  " + values[0]);
				strings.add(String.format("insert into process_run_control(execution_id,task_id,operator_id) values(%d,%d,%d)", process.getId(), task.getId(), values[1]));

				if (values[2] == 1) {
					strings.add("update task set flag = 0 where id = " + task.getId());
				}

				try {
					DB_HELPER.execBatchSql(strings);
				} catch (SQLException e) {
					log.error("发生未知异常:", e);
				}
			}

			return true;
		}

		return false;
	}

	protected ResultCode execute(JSONObject variables) {
		processDetail.set("result_data", ((JSONObject) process.get("variable")).clone());
		//noinspection ConstantConditions
		processDetail.set("employee_id", Context.getCurrentOperatorUser().getId());

		process.saveDetail(processDetail);

		return ResultCode.NORMAL;
	}

	protected void turnInTask(Link link) {
		setProcessDetail();

		setAlreadyEnter(link);

		if (!handleInsertAuditTrunIn()) {
			initTaskOperators();
		}
	}

	private boolean handleInsertAuditTrunIn() {
		String operators = DB_HELPER.selectOneValues("select operators from  process_insert_audit where flag = 0 and task_id = " + task.getId() + " order by accumulative desc limit 1");

		if (StringUtil.isEmpty(operators)) {
			return false;
		}

		setOperators(operators);
		DB_HELPER.update("update process_insert_audit set flag = 1 where task_id = " + task.getId());
		DB_HELPER.update("update task set flag = 2 where id = " + task.getId());

		return true;
	}

	void setProcessDetail() {
		processDetail.set("now_data", ((JSONObject) process.get("variable")).clone());
		processDetail.set("create_time", new Date(System.currentTimeMillis()));
		processDetail.set("flag", 0);

		process.saveDetail(processDetail);
	}

	/**
	 * 初始化一个任务的所有操作人.
	 */
	private void initTaskOperators() {
		int count = DB_HELPER.selectOneValues(String.format("select count(*)::integer from process_run_control where execution_id = %d and task_id = %d", process.getId(), task.getId()));

		//当未分配操作人时，设置操作人对象.
		if (count == 0) {
			setOperators(task.getDefinition().getAssignees());
		}
	}

	void setOperators(String operators) {
		int companyId = process.getCompany().getId(),
			processId = process.getId(),
			taskId = task.getId();

		//当设置的是系统级的关联关系操作人的时候，从系统对象当中获得，并直接设置操作人。
		ExpressionControl expressionControl = process.getExpressionControl();
		Integer userId = (Integer) expressionControl.get(operators);

		if (userId != null) {
			DB_HELPER.update(String.format("insert into process_run_control(execution_id,task_id,operator_id) values(%d,%d,%d)", processId, taskId, userId));
		} else {
			List<User> users = UserExpressionUtil.getlists(operators, companyId);

			//如果用户未设置操作人，将写需要设置操作人表，等待设置操作人.
			if (users.size() == 0) {
				//noinspection ConstantConditions
				DB_HELPER.update(String.format("insert into process_set_operator(execution_id,task_id,sponsor,flag) VALUES (%d,%d,%d,0);", processId, taskId, Context.getCurrentOperatorUser().getId()));
				return;
			}

			StringBuilder builder = new StringBuilder();

			builder.append("insert into process_run_control(execution_id,task_id,operator_id) values");
			for (User user : users) {
				builder.append("(").append(processId).append(",").append(taskId).append(",").append(user.getId()).append("),");
			}

			if (users.size() > 0) {
				builder.replace(builder.length() - 1, builder.length(), ";");
			}

			DB_HELPER.update(builder.toString());
		}
	}

	/**
	 * 记录从哪一个线进入.
	 *
	 * @param link 进入线的对象.
	 */
	void setAlreadyEnter(Link link) {
		if (link == null) {
			return;
		}

		boolean have = false;
		String already = task.get("already_enter");
		if (!StringUtil.isEmpty(already))
			for (String s : already.split(","))
				if (Integer.parseInt(s) == link.getId()) {
					have = true;
					break;
				}

		if (!have) {
			task.set("already_enter", (already == null ? "" : already + ",") + link.getId());
			task.flush();
		}
	}

	protected void completeSelf() {
		processDetail.set("flag", 1);

		DB_HELPER.update("update task set flag = 10 where id = " + task.getId());

		process.saveDetail(processDetail);
	}

	/**
	 * 向下层转移操作
	 */
	protected void transfer() {
		TaskDefinition definition = task.getDefinition();
		definition.getToLinks().forEach(link -> {
			if (link.isThrough(process)) {
				process.activateTask(link.getTo(), link);
			}
		});
	}
}
