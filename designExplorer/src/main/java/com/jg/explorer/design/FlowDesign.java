package com.jg.explorer.design;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.explorer.BaseServlet;
import com.jg.explorer.HttpRequestType;
import com.jg.identification.Company;
import com.jg.workflow.Context;
import com.jg.workflow.exception.ModelHasbeenDeployed;
import com.jg.workflow.exception.ModelNotFound;
import com.jg.workflow.process.definition.ProcessDefinition;
import com.jg.workflow.process.model.Model;
import com.jg.workflow.process.model.ModelManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;

import static com.jg.common.result.HttpResult.*;
import static com.jg.workflow.process.model.ModelFactory.MODEL_FACTORY;

/**
 * 提供流程设计WEB接口
 * <p>
 * create by 17/4/26.
 *
 * @author yimin
 */
@WebServlet(name = "FlowDesign", urlPatterns = "/FlowDesign")
public class FlowDesign extends BaseServlet {
	private static final Logger log = LogManager.getLogger(FormDesign.class.getName());

	@Override
	protected HttpResult execute(HttpRequestType type, ServletData servletData) {
		Company company = (Company) servletData.get(COMPANY);
		JSONObject jsonData = (JSONObject) servletData.get(JSON_DATA);
		ModelManager modelManager = Context.getModelManager(company);
		Model model;

		switch (type) {
			case list:
				return SUCCESS.clone().setListToData(RESULT_LIST, MODEL_FACTORY.getMultipleObjects("company_id",company.getId(),null),"id","name");
			case modify:
			case save:
				model = modelManager.getModel((Integer) jsonData.get(ID));
				if (model == null) {
					return NOT_FOUND_OBJECT.clone().put(ID, jsonData.get(ID));
				}
				return saveToModel(model, jsonData, company);
			case load:
				return loadModel(modelManager, jsonData);
			case create:
				model = modelManager.createModel();
				return saveToModel(model, jsonData, company);
			default:
				log.error("未找到此类型定义的操作。type:" + type.name());
				return UNKNOWN;
		}
	}

	private HttpResult loadModel(ModelManager modelManager, JSONObject jsonData) {
		Integer modelId = (Integer) jsonData.get("modelId");
		if (modelId == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：modelId");
		}

		Model model;
		try {
			model = modelManager.getModel(modelId);
		} catch (ModelNotFound e) {
			return NOT_FOUND_OBJECT.clone().put(ID, jsonData.get(ID));
		}

		return SUCCESS.clone().addInfoToValue("model", model, "id", "name", "category", "content", "view_information", "company_id", "description", "flag");
	}

	private HttpResult saveToModel(Model model, JSONObject jsonData, Company company) {
		JSONObject content = (JSONObject) jsonData.get(CONTENT);
		content.put("name", jsonData.get(NAME));

		model.set(NAME, jsonData.get(NAME));
		model.set("category", jsonData.get("category"));
		model.set("company_id", company.getId());
		model.set("description", jsonData.get("description"));
		model.set(CONTENT, content);
		model.set("view_information", jsonData.get("view_information"));

		model.flush();
		ProcessDefinition definition;
		try {
			definition = Context.getProcessManager(company).deploymentProcess(model.get(ID));
		} catch (ModelHasbeenDeployed e) {
			log.error("部署出现异常：", e);
			return UNKNOWN;
		}

		HttpResult httpResult = SUCCESS.clone().put(ID, model.get(ID));
		if (definition != null) {
			httpResult.put("process_definitionID", definition.getId());
		}

		return httpResult;
	}
}







