package com.jg.workflow.expression;

import com.jg.workflow.process.Process;

/**
 * 表达式解析器的接口
 * <p>
 * 返回值可能是各种对象，看存放以及指定的方式来提供。
 *
 * @author yimin
 */

interface Resolver {

	Object getValue(Process process, String name);
}
