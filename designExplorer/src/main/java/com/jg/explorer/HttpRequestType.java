package com.jg.explorer;

/**
 * 记录Http的请求类型.
 *
 * @author yimin
 */
public enum  HttpRequestType {
	query,
	insert,
	delete,
	save,
	modify,
	list, listName, start, loadmytasks, handle, create,
	startstru,//流程开始前的结构
	myprocesslist, //我发起的流程列表
	load
}

