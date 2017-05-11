package com.jg.workflow.process.handle;

/**
 * 操作的类型,为指明后续具体操作.
 * <p>
 * create by 17-3-27.
 *
 * @author yimin
 */
public enum HandleType {
	//新增加
	Add,
	//审核
	Audit,
	//列表
	List,
	//其他类型,此类型将指定无处理
	Other
}
