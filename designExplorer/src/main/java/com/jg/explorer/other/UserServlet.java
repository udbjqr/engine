package com.jg.explorer.other;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.explorer.BaseServlet;
import com.jg.explorer.HttpRequestType;
import com.jg.identification.Company;
import com.jg.identification.Context;
import com.jg.identification.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebServlet;

import static com.jg.common.result.HttpResult.*;

/**
 * create by 17/5/18.
 *
 * @author yimin
 */
@WebServlet(name = "user", urlPatterns = "/user")
public class UserServlet extends BaseServlet {
	private final static Logger log = LogManager.getLogger(UserServlet.class.getName());

	private HttpResult setCurrentUser(JSONObject jsonData, Company company, ServletData servletData) {
		Integer userId = (Integer) jsonData.get("userId");
		if (userId == null) {
			return NO_SET_REQUEST_TYPE.clone().setMessage("未找到需要的参数：userId");
		}

		User user = company.getSubUser(userId);
		if (user == null) {
			return NOT_FOUND_OBJECT.clone().put("未指到指定Id的用户对象", userId);
		}

		Context.setCurrentOperatorUser(user);

		return SUCCESS;
	}

	private HttpResult listUsers(Company company) {
		JSONArray array = new JSONArray();

		for (User user : company.getUsers()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("label", user.getName());
			jsonObject.put("value", user.getId());

			array.add(jsonObject);
		}

		JSONObject data = new JSONObject();
		data.put("current", Context.getCurrentOperatorUser().getId());
		data.put(LIST, array);
		return SUCCESS.clone().setData(data);
	}

	@Override
	protected HttpResult execute(HttpRequestType type, ServletData servletData) {
		Company company = (Company) servletData.get(COMPANY);
		JSONObject jsonData = (JSONObject) servletData.get(JSON_DATA);

		switch (type) {
			case list:
				return listUsers(company);
			case login:
				return setCurrentUser(jsonData, company, servletData);
			default:
				log.error("未找到此类型定义的操作。type:" + type.name());
				return UNKNOWN;
		}
	}


}
