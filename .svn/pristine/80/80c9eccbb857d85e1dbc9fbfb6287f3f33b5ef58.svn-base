package com.jg.workflow;

import com.jg.workflow.expression.ExpressionControl;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * create by 17/4/20.
 *
 * @author yimin
 */

public class TestScript {
	public static final ScriptEngine SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("nashorn");

	@Test
	public void test() throws ScriptException {
		ExpressionControl expressionControl = new ExpressionControl(null);
		SCRIPT_ENGINE.eval("result.result = (null)", expressionControl);
		System.out.println(expressionControl.getResult());
	}


}
