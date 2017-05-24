package com.jg.explorer;

import com.jg.identification.Context;
import com.jg.identification.User;
import com.jg.workflow.util.TestIdentificationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Set;

public class WebConfig implements ServletContainerInitializer {
	private final Logger logger = LogManager.getLogger(WebConfig.class.getName());

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		logger.info("开始初始化动作！");
		init();
	}

	//FIXME 这个部分需要在正式使用时删除
	public static void init() {
		setTestValue();
	}


	private static void setTestValue() {
		TestIdentificationUtil.setEmployers1();

		User user = Context.getUserById(1, 3);
		Context.setCurrentOperatorUser(user);
	}
}
