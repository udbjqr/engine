package com.jg.explorer;

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

	public static void init() {

	}
}
