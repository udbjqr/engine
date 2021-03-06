package com.jg.workflow.process.handle;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.identification.User;
import com.jg.workflow.process.Process;
import com.jg.workflow.process.module.Module;
import com.jg.workflow.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * 操作类的虚类.
 * <p>
 * 从此类继承对象将无法继续重写run()方法，保证执行前后可以执行相关操作。
 * <p>
 * 此类的继承类构造的对象将被长期持有，因此此操作的继承类必须保证操作是线程安全。
 * <p>
 * 此类的继承类需要在构造函数中指定当前操作的类型，将会根据操作定义的类型操作。
 */
public abstract class AbstractHandle implements Handle {
	protected static final Logger logger = LogManager.getLogger(AbstractHandle.class.getPackage().getName());

	protected int id;
	protected HandleType type = null;
	protected String view_name;
	protected String prompt;
	protected int flag;
	protected List<AdditionalHandle> additionalHandles;

	protected AbstractHandle(List<AdditionalHandle> additionalHandles) {
		this.additionalHandles = additionalHandles;
	}

	protected AbstractHandle() {
		this(null);
	}

	@Override
	public List<AdditionalHandle> getAdditionalHandle() {
		return additionalHandles;
	}

	@Override
	public HttpResult run(User user, Task task, Module module, Process process, JSONObject jsonData) {
		HttpResult httpResult = beforeRun(user, task, module, process, jsonData);
		if (httpResult != null) {
			return httpResult;
		}

		httpResult = execute(user, task, module, process, jsonData);

		httpResult = afterRun(user, task, module, process, jsonData, httpResult);

		return httpResult;
	}

	/**
	 * 实际的执行动作.
	 */
	protected HttpResult execute(User user, Task task, Module module, Process processData, JSONObject jsonData) {
		return null;
	}

	/**
	 * 实际执行之前的操作.
	 */
	protected HttpResult beforeRun(User user, Task task, Module module, Process processData, JSONObject jsonData) {
		HttpResult httpResult = checkPara(user, task, module, processData, jsonData);

		if (httpResult != null) {
			return httpResult;
		}

		logger.debug("操作{}将被执行", id);

		return null;
	}

	private HttpResult checkPara(User user, Task task, Module module, Process processData, JSONObject jsonData) {
		if (task == null) {
			logger.error("任务为空，无法继续执行。");
		}

		if (module == null) {
			logger.error("功能模块为空，无法继续执行。");
		}

		if (jsonData == null) {
			logger.warn("操作数据为空，可能产生异常。");
		}

		return null;
	}

	/**
	 * 实际执行之后的操作.
	 */
	protected HttpResult afterRun(User user, Task task, Module module, Process processData, JSONObject jsonData, HttpResult httpResult) {
		logger.debug("操作{}执行结束", id);
		return httpResult;
	}

	/**
	 * 使用数据库数据，初始化操作对象.
	 * <p>
	 * 此方法应仅仅为工厂类调用.
	 *
	 * @param data 操作对象的数据对象
	 */
	Handle initValue(HandleData data) {
		this.id = data.get("id");
		this.view_name = data.get("view_name");
		this.prompt = data.get("prompt");
		this.flag = data.get("flag");
		return this;
	}

	@Override
	public final int getId() {
		return id;
	}

	@Override
	public void undo() {
		logger.error("未实现此方法。");
	}

	@Override
	public HandleType getType() {
		return type;
	}

	@Override
	public Object getFormValue(String name, int formId) {
		//默认直接返回null值.
		return null;
	}

	@Override
	public String getName() {
		return view_name;
	}
}
