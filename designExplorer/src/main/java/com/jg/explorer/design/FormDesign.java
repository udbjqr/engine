package com.jg.explorer.design;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.explorer.BaseServlet;
import com.jg.explorer.HttpRequestType;
import com.jg.identification.Company;
import com.jg.identification.User;
import com.jg.workflow.process.module.Module;
import com.jg.workflow.process.module.ModuleImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;
import java.util.Date;
import java.util.List;

import static com.jg.common.result.HttpResult.*;
import static com.jg.workflow.process.module.ModuleFactory.MODULE_FACTORY;

/**
 * 提供流程设计WEB接口
 * <p>
 * create by 17/4/26.
 *
 * @author yimin
 */
@WebServlet(name = "FormDesign", urlPatterns = "/FormDesign")
public class FormDesign extends BaseServlet {
	private static final Logger log = LogManager.getLogger(FormDesign.class.getName());

	@Override
	protected HttpResult execute(HttpRequestType type, ServletData servletData) {
		Company company = (Company) servletData.get(COMPANY);
		JSONObject jsonData = (JSONObject) servletData.get(JSON_DATA);

		switch (type) {
			case delete:
				return delete(jsonData);
			case list:
				return list(servletData);
			case load:
				return load(jsonData);
			case modify:
			case save:
				return saveToModule(jsonData, company, servletData);
			default:
				log.error("未找到此类型定义的操作。type:" + type.name());
				return HttpResult.UNKNOWN;
		}
	}

	private HttpResult delete(JSONObject data) {
		Integer moduleId = data.getInteger("id");
		if (moduleId == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：id");
		}

		ModuleImpl module = MODULE_FACTORY.getObject("id", moduleId);
		if (module == null) {
			return NOT_FOUND_OBJECT;
		}

		if (module.delete()) {
			return SUCCESS;
		}

		return OBJECT_USED_NOT_DELETE;
	}

	private HttpResult list(ServletData servletData) {
		User user = (User) servletData.get(USER);

		List<ModuleImpl> list = MODULE_FACTORY.getAllObjects(user, " order by 1 asc");
		if (list != null) {
			return SUCCESS.clone().setListToData(LIST, list, "id", "module_name");
		}

		return UNKNOWN;
	}

	private HttpResult saveToModule(JSONObject jsonData, Company company, ServletData servletData) {
		ModuleImpl module;

		Integer moduleId = jsonData.getInteger("module_id");

		JSONObject structure = (JSONObject) jsonData.get(CONTENT);
		if (structure == null) {
			structure = new JSONObject();
		}

		if (moduleId != null) {
			module = MODULE_FACTORY.getObject(ID, moduleId);
		} else {
			module = MODULE_FACTORY.getNewObject((User) servletData.get(USER));
			module.setIdBySequence();
			module.set("company_id", company.getId());
			module.set("create_time", new Date(System.currentTimeMillis()));
			module.set("create_user", ((User) servletData.get(USER)).getId());

			structure.put("version", 0);
			structure.put("module_id", module.getId());
			structure.put("module_name", jsonData.get("module_name"));
			module.set("form_structure", structure);
		}

		JSONObject oldStru = module.get("form_structure");
		structure.put("version", oldStru.getInteger("version") + 1);

		String moduleName = jsonData.getString("module_name");
		if (moduleName != null)
			module.set("module_name", moduleName);
		module.set("update_time", new Date(System.currentTimeMillis()));
		module.set("update_user", ((User) servletData.get(USER)).getId());
		module.set("form_structure", structure);

		module.flush();

		return new HttpResult().put(ID, module.get(ID));
	}

	private HttpResult load(JSONObject jsonData) {
		Integer moduleId = jsonData.getInteger(ID);
		if (moduleId == null) {
			return NO_PARAMETER.clone().put("possible", ID);
		}

		Module module = MODULE_FACTORY.getObject(ID, moduleId);

		if (module == null) {
			return NOT_FOUND_OBJECT;
		}

		return SUCCESS.clone().setValue(module.get("form_structure").toString());
	}

}
