package com.jg.explorer;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.objects.Dict;
import com.jg.common.result.HttpResult;
import com.jg.identification.Company;
import com.jg.identification.User;

import javax.servlet.annotation.WebServlet;
import java.util.List;

import static com.jg.common.objects.DictFactory.DICT_FACTORY;
import static com.jg.common.result.HttpResult.*;
import static com.jg.common.sql.DBHelperFactory.DB_HELPER;

/**
 * create by 17/4/28.
 *
 * @author yimin
 */
@WebServlet(name = "dict", urlPatterns = "/dict")
public class DictServlet extends BaseServlet {

	@Override
	protected HttpResult execute(HttpRequestType type, ServletData servletData) {
		Company company = (Company) servletData.get(COMPANY);
		JSONObject jsonData = (JSONObject) servletData.get(JSON_DATA);

		switch (type) {
			case list:
			case listName:
				return list(company);
			case query:
			case load:
				return load(jsonData, company, servletData);
			case modify:
			case save:
				return saveTo(jsonData, company, servletData);
			case insert:

			default:
				return HttpResult.UNKNOWN;
		}
	}

	private HttpResult load(JSONObject jsonData, Company company, ServletData servletData) {
		String dictName = jsonData.getString("dict_name");
		User user = (User) servletData.get(USER);

		if (dictName == null) {
			return NO_SET_REQUEST_TYPE;
		}

		List<Dict> lists = DICT_FACTORY.getObjectsForString(" where company_id = " + company.getId() + " and dict_name = " + DB_HELPER.getString(jsonData.getString("dict_name")), user);

		return SUCCESS.clone().setListToData(RESULT_LIST, lists, "label", "value");
	}

	private HttpResult list(Company company) {
		final HttpResult[] httpResult = {null};

		DB_HELPER.selectWithSet("select distinct dict_name as value,dict_name as label from data_dictionary where company_id = " + company.getId(), set -> httpResult[0] = SUCCESS.clone().setListToData(RESULT_LIST, set, "label", "value"));

		return httpResult[0];
	}


	private HttpResult saveTo(JSONObject jsonData, Company company, ServletData servletData) {
		User user = (User) servletData.get(USER);
		Integer id = jsonData.getInteger(ID);
		Dict dict;

		if (id == null) {
			dict = DICT_FACTORY.getNewObject(user);
			dict.set(COMPANY_ID, company.getId());
			dict.set("dict_name", jsonData.getString("dict_name"));
		} else {
			dict = DICT_FACTORY.getObject(ID, id);
		}

		dict.set("label", jsonData.getString("label"));
		dict.set("value", jsonData.getString("value"));
		dict.set("flag", 0);

		if (dict.get("dict_name") == null || dict.get("label") == null || dict.get("value") == null) {
			return LACK_VARIABLE.clone().setValue("{\"last:\":\"dict_name or label or value\"}");
		}

		int count = DB_HELPER.selectOneValues(String.format("select count(*)::integer from  data_dictionary where company_id = %d and dict_name = %s and (label = %s or value = %s);", company.getId()
			, DB_HELPER.getString(dict.get("dict_name"))
			, DB_HELPER.getString(dict.get("label"))
			, DB_HELPER.getString(dict.get("value"))));

		if (count > 0) {
			return ALREADY_EXISTS;
		}

		dict.flush();

		return new HttpResult().put(ID, dict.get(ID));
	}
}
