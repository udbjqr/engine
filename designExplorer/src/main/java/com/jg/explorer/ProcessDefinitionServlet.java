package com.jg.explorer;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.identification.Company;
import com.jg.workflow.Context;
import com.jg.workflow.exception.ModelNotFound;
import com.jg.workflow.process.ProcessManager;
import com.jg.workflow.process.definition.ProcessDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import java.util.List;

import static com.jg.common.result.HttpResult.*;
import static com.jg.workflow.process.definition.ProcessDefinitionFactory.PROCESS_DEFNITION_FACTORY;

/**
 * create by 17/5/9.
 *
 * @author yimin
 */
@WebServlet(name = "processDefinition", urlPatterns = "/processDefinition")
public class ProcessDefinitionServlet extends BaseServlet {
	private static final Logger log = LogManager.getLogger(ProcessServlet.class.getName());


	@Override
	protected HttpResult execute(HttpRequestType type, ServletData servletData) {
		Company company = (Company) servletData.get(COMPANY);
		JSONObject jsonData = (JSONObject) servletData.get(JSON_DATA);
		ProcessManager processManager = Context.getProcessManager(company);
		switch (type) {
			case list:
				return listProcessDef(processManager, company);
			case load:
				return loadProcessDef(jsonData);
			default:
				log.error("未找到此类型定义的操作。type:" + type.name());
				return UNKNOWN;
		}
	}

	private HttpResult loadProcessDef(JSONObject jsonData) {
		Integer definitionId = (Integer) jsonData.get("definitionId");

		if (definitionId == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：definitionId");
		}

		ProcessDefinition definition;
		try {
			definition = PROCESS_DEFNITION_FACTORY.getObject("id", definitionId);
			if (definition == null) {
				return NOT_FOUND_OBJECT.clone().put("notFound", "id");
			}
		} catch (ModelNotFound e) {
			return NOT_FOUND_OBJECT.clone().put("notFound", "id");
		}

		return SUCCESS.clone().addInfoToValue("definition", definition, "id", "name", "version", "content", "view_information", "category", "description", "model_content", "model_id", "flag");
	}

	private HttpResult listProcessDef(ProcessManager processManager, Company company) {
		List<ProcessDefinition> definitions;
		try {
			definitions = processManager.getCanStartProcessDefinitions(company.getId(), -1, -1);
		} catch (ModelNotFound e) {
			return NOT_FOUND_OBJECT.clone().put("notFound", "companyId");
		}

		return SUCCESS.clone().setListToData("list", definitions, "id", "name");
	}


}

