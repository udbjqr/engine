package com.jg.workflow.process.handle;


import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.identification.User;
import com.jg.workflow.process.Process;
import com.jg.workflow.process.module.Module;
import com.jg.workflow.task.Task;

/**
 * 操作接口.
 * <p>
 * 所有从此接口继承的实体类都必须有一个无参数构造函数。
 */
public interface Handle {

	//设置返回页面
	abstract void setUrl(String url);

	//得到返回页面
	abstract String getUrl();

	/**
	 * 由流程调用此方法,操作的入口.
	 *
	 * @param module      指定的模块
	 * @param task        指定的任务
	 * @param process 流程数据对象，此对象保存所有流程的数据
	 * @param jsonData    此次需要操作的数据
	 * @return 返回一个错误提示，正常结束返回
	 */
	HttpResult run(User user, Task task, Module module, Process process, JSONObject jsonData);

	/**
	 * 对操作做回退操作
	 */
	void undo();

	/**
	 * 返回此操作的ID号.
	 *
	 * @return id号码
	 */
	int getId();

	/**
	 * 返回当前操作的类型.
	 * <p>
	 * 每一个数据的操作类型均应该在对象被实例化时指定
	 *
	 * @return 操作类型
	 */
	HandleType getType();

	/**
	 * 根据名称和表单ID,给出对应的表单的值.
	 * <p>
	 * 此接口由所有实现类继承并实现,实现类自己知道数据存储位置以及存储方式.
	 *
	 * @param name   需要查找的表单项名称
	 * @param formId 表单的Id
	 * @return 实际的表单对象.
	 */
	Object getFormValue(String name, int formId);

	String getName();
}
