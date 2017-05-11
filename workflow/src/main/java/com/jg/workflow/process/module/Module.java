package com.jg.workflow.process.module;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.persistence.Persistence;
import com.jg.workflow.process.handle.Handle;

/**
 * 模块的接口
 *
 * @author yimin
 */
public interface Module extends Persistence {

	/**
	 * 重新从数据库获得功能模块所指定的操作对象，并创建对象
	 */
	void refreshHandles();

	/**
	 * 遍历模块内的表单对象,并执行指定操作.
	 *
	 * @param function 需要对表单对象进行的操作方法
	 */
	void steam(FormStructureNameFunction function);


	/**
	 * 得到一个模块的查询操作,一个模块应该有且只有一个查询操作.
	 * <p>
	 * 如果未找到对应的值将弹出NoSuchElementException异常
	 *
	 * @return 查询操作对象, 多定义随机返回一个
	 */
	Handle getQueryHandle();

	/**
	 * 得到一个模块的新增操作.
	 * <p>
	 * 如果有多个操作，返回查询到的第一个
	 * <p>
	 * 如果未找到对应的值将弹出NoSuchElementException异常
	 *
	 * @return 查询操作对象, 多定义随机返回一个
	 */
	Handle getAddHandle();

	/**
	 * 得到表单的json对象.
	 *
	 * @return 模块对应的表单json对象。
	 */
	JSONObject getFormStructure();

	/**
	 * 返回模块的Id
	 *
	 * @return 模块的Id
	 */
	int getId();

	/**
	 * 判断操作是否对应着此模块
	 *
	 * @param handle 需要对比的操作
	 * @return true 是存在此模块当中
	 */
	boolean containHandle(Handle handle);
}
