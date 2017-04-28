package com.jg.explorer.design;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.ResultCode;
import com.jg.explorer.BaseServlet;
import com.jg.explorer.HttpRequestType;
import com.jg.identification.Company;
import com.jg.workflow.Context;
import com.jg.workflow.process.model.Model;
import com.jg.workflow.process.model.ModelManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;

import static com.jg.common.result.ResultCode.SUCCESS;

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
	protected ResultCode execute(HttpRequestType type, ServletData servletData) {
		Company company = (Company) servletData.get(COMPANY);
		JSONObject jsonData = (JSONObject) servletData.get(JSON_DATA);
		ModelManager modelManager = Context.getModelManager(company);
		Model model;

		switch (type) {
			case modify:
				model = modelManager.getModel((Integer) jsonData.get(ID));
				if (model == null) {
					return ResultCode.NOT_FOUND_OBJECT.clone().put(ID, jsonData.get(ID));
				}
				return saveToModel(model, jsonData, company);
			case save:
				model = modelManager.createModel();
				return saveToModel(model, jsonData, company);
			default:
				log.error("未找到此类型定义的操作。type:" + type.name());
				return ResultCode.UNKNOWN;
		}
	}

	private ResultCode saveToModel(Model model, JSONObject jsonData, Company company) {
		model.set(NAME, jsonData.get(NAME));
		model.set("category", jsonData.get("category"));
		model.set("company_id", company.getId());
		model.set("description", jsonData.get("description"));
		model.set(CONTENT, jsonData.get(CONTENT));
		model.set("view_infomation", jsonData.get("view_infomation"));

		model.flush();


		return SUCCESS.clone().put(ID, model.get(ID));
	}
}
