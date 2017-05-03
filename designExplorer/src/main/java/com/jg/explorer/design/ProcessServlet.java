package com.jg.explorer.design;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.ResultCode;
import com.jg.explorer.BaseServlet;
import com.jg.explorer.HttpRequestType;
import com.jg.identification.Company;
import com.jg.workflow.Context;
import com.jg.workflow.process.Process;
import com.jg.workflow.process.ProcessManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;

import static com.jg.common.result.ResultCode.SUCCESS;
import static com.jg.common.result.ResultCode.UNKNOWN;

/**
 * 流程控制servlet.
 *
 * @author yimin
 */

@WebServlet(name = "Process", urlPatterns = "/Process")
public class ProcessServlet extends BaseServlet {
	private static final Logger log = LogManager.getLogger(ProcessServlet.class.getName());

	@Override
	protected ResultCode execute(HttpRequestType type, ServletData servletData) {
		Company company = (Company) servletData.get(COMPANY);
		JSONObject jsonData = (JSONObject) servletData.get(JSON_DATA);
		ProcessManager processManager = Context.getProcessManager(company);

		switch (type) {
			case start:
				return startProcess(processManager, jsonData);
			default:
				log.error("未找到此类型定义的操作。type:" + type.name());
				return UNKNOWN;
		}
	}

	private ResultCode startProcess(ProcessManager processManager, JSONObject jsonData) {
		Integer definitionId = jsonData.getInteger("definitionId");

		Process process = processManager.startProcess(definitionId);

		if (process != null) {
			log.trace("成功生成流程：" + process);
			return SUCCESS.clone().put("processId", process.getId());
		}

		return UNKNOWN;
	}
}
