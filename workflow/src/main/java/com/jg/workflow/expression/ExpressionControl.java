package com.jg.workflow.expression;

import com.jg.workflow.exception.ForbidCallThis;
import com.jg.workflow.process.Process;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.*;

/**
 * 针对变量的取值动作.
 * <p>
 * 此类将由流程自动调用。
 * <p>
 * 此动作将针对三个部分取值，
 * 一个是流程变量，在流程变量当前存储的值内部取值
 * 一个是系统变量
 * 一个是保存的模块内变量
 * <p>
 * 模块变量采用,moduleId.字段名方式.如 2.name
 * <p>
 * 将按顺序获取值，前一个得到数据将直接返回.
 * <p>
 * create by 2017/4/7.
 *
 * @author yimin
 */

public class ExpressionControl implements Bindings {
	private static final Logger log = LogManager.getLogger(ExpressionControl.class.getName());

	/**
	 * 脚本对象的管理器.
	 */
	public static final ScriptEngine SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("nashorn");
	private final Process process;

	private final List<Resolver> resolvers = new LinkedList<>();
	private final Result result = new Result();
	private final Map<String, Object> map = new HashMap<>();

	public ExpressionControl(Process process) {
		this.process = process;

		init();
	}

	private void init() {
		map.put("result", result);

		addResolver(new ProcessVarResolver());
		addResolver(new SystemVarResolver());
		addResolver(new ModuleVarResolver());
	}

	@Override
	public Object get(Object name) {
		if ("result".equals(name)) {
			return result;
		}

		checkKey(name);

		Object value;
		for (Resolver resolver : resolvers) {
			if ((value = resolver.getValue(process, (String) name)) != null) {
				log.trace("返回对象：" + value);
				return value;
			}
		}

		log.trace("从map内尝试获取。");
		return map.get(name);
	}

	private void addResolver(Resolver resolver) {
		resolvers.add(resolver);
	}

	@Override
	public Object put(String name, Object value) {
		return map.put(name, value);
	}

	@Override
	public void putAll(Map<? extends String, ?> toMerge) {
		throw new ForbidCallThis();
	}

	@Override
	public void clear() {
		throw new ForbidCallThis();
	}


	@Override
	public Set<String> keySet() {
		throw new ForbidCallThis();
	}


	@Override
	public Collection<Object> values() {
		throw new ForbidCallThis();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		throw new ForbidCallThis();
	}

	@Override
	public int size() {
		throw new ForbidCallThis();
	}

	@Override
	public boolean isEmpty() {
		throw new ForbidCallThis();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		throw new ForbidCallThis();
	}

	@Override
	public Object remove(Object key) {
		throw new ForbidCallThis();
	}

	private void checkKey(Object key) {
		if (key == null) {
			throw new NullPointerException("key can not be null");
		}
		if (!(key instanceof String)) {
			throw new ClassCastException("key should be a String");
		}
		if (key.equals("")) {
			throw new IllegalArgumentException("key can not be empty");
		}
	}

	public Object getResult() {
		return result.result;
	}

	public class Result {
		private Object result;

		public Object getResult() {
			return result;
		}

		public void setResult(Object result) {
			this.result = result;
		}
	}
}
