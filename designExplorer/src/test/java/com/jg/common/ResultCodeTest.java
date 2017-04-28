package com.jg.common;

import com.jg.common.result.ResultCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static com.jg.workflow.process.module.ModuleFactory.MODULE_FACTORY;


/**
 * create by 17/4/28.
 *
 * @author yimin
 */
public class ResultCodeTest {
	private static final Logger log = LogManager.getLogger(ResultCodeTest.class.getName());
	private ResultCode resultCode = new ResultCode();


	@Test
	public void isSuccess() throws Exception {
		log.info("isSuccess \t {}", resultCode.isSuccess());
	}

	@Test
	public void put() throws Exception {
	}

	@Test
	public void setValue() throws Exception {
	}


	@Test
	public void getCode() throws Exception {
		log.info("getCode \t {}", resultCode.getCode());
	}

	@Test
	public void setListToData() throws Exception {
		resultCode.setListToData("list",MODULE_FACTORY.getAllObjects(null),"id","module_name","remark","flag");
		log.info("setListToData \t {}", resultCode.toString());
	}


}