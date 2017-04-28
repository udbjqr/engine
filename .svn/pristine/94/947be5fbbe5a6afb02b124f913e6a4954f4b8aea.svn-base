package com.jg.explorer;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.objects.Dict;
import com.jg.common.result.ResultCode;
import com.jg.identification.Company;
import com.jg.identification.User;

import javax.servlet.annotation.WebServlet;
import java.util.List;

import static com.jg.common.objects.DictFactory.DICT_FACTORY;
import static com.jg.common.result.ResultCode.*;
import static com.jg.common.sql.DBHelperFactory.DB_HELPER;

/**
 * create by 17/4/28.
 *
 * @author yimin
 */
@WebServlet(name = "dict", urlPatterns = "/dict")
public class DictServlet extends BaseServlet {

	@Override
	protected ResultCode execute(HttpRequestType type, ServletData servletData) {
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
				return ResultCode.UNKNOWN;
		}
	}

	private ResultCode load(JSONObject jsonData, Company company, ServletData servletData) {
		String dictName = jsonData.getString("dict_name");
		User user = (User) servletData.get(USER);

		if (dictName == null) {
			return NO_SET_REQUEST_TYPE;
		}

		List<Dict> lists = DICT_FACTORY.getObjectsForString(" where company_id = " + company.getId() + " and dict_name = " + DB_HELPER.getString(jsonData.getString("dict_name")), user);

		return SUCCESS.clone().setListToData(RESULTLIST, lists, "label", "value");
	}

	private ResultCode list(Company company) {
		final ResultCode[] resultCode = {null};

		DB_HELPER.selectWithSet("select distinct dict_name from data_dictionary where company_id = " + company.getId(), set -> resultCode[0] = SUCCESS.clone().setListToData(RESULTLIST, set, "dict_name"));

		return resultCode[0];
	}


	private ResultCode saveTo(JSONObject jsonData, Company company, ServletData servletData) {
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

		return new ResultCode().put(ID, dict.get(ID));
	}
}
