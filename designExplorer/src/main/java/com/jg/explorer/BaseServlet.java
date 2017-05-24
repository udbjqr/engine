package com.jg.explorer;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.result.HttpResult;
import com.jg.identification.Context;
import com.jg.identification.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static com.jg.common.result.HttpResult.UNKNOWN;

/**
 * 此类为实际Servlet的基类.
 * <p>
 * 负责进行所有Servlet需要的判断、保护等操作。
 * <p>
 * 同时负责将操作分发至具体的操作中。
 * <p>
 * 将网络操作转换成业务操作.
 * <p>
 * create by 17/4/26.
 *
 * @author yimin
 */
public abstract class BaseServlet extends HttpServlet {
	private final static Logger log = LogManager.getLogger(BaseServlet.class.getName());
	private static final String WRITER = "writer";
	private static final String TYPE = "type";
	private static final String REQUEST = "request";
	private static final String RESPONSE = "response";
	protected static final String USER = "user";
	protected static final String COMPANY = "company";
	static final String COMPANY_ID = "company_id";
	protected static final String JSON_DATA = "jsonData";
	protected static final String CONTENT = "content";
	protected static final String NAME = "name";
	protected static final String ID = "id";
	protected static final String LIST = "list";

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		//FIXME 这个部分需要在正式使用时删除
		setTestValue(req);

		ServletData servletData = new ServletData();

		try {
			req.setCharacterEncoding("UTF-8");
			res.setCharacterEncoding("UTF-8");
			((HttpServletResponse) res).setHeader("content-type", "application/json;charset=UTF-8");

			HttpResult httpResult = checkAndSaveParameter(servletData, req, res);

			if (httpResult != null) {
				((PrintWriter) servletData.get(WRITER)).print(httpResult.toString());
				return;
			}

			HttpRequestType type = (HttpRequestType) servletData.get(TYPE);
			httpResult = execute(type, servletData);

			if (httpResult == null) {
				((PrintWriter) servletData.get(WRITER)).print(HttpResult.ISNULL.toString());
				return;
			}

			writeToFront(servletData, httpResult);
		} catch (Exception e) {
			log.error("发生未捕获异常：", e);
			writeToFront(servletData, UNKNOWN);
		}
	}

	private void writeToFront(ServletData servletData, HttpResult httpResult) {

		String result = httpResult.toString();
		((PrintWriter) servletData.get(WRITER)).print(result);
		log.debug("向前端返回：" + result);
	}


	private HttpResult checkAndSaveParameter(ServletData servletData, ServletRequest req, ServletResponse res) {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		servletData.put(REQUEST, request);
		servletData.put(RESPONSE, response);

		try {
			servletData.put(WRITER, response.getWriter());
		} catch (IOException e) {
			log.error("得到输出异常：", e);
			return UNKNOWN;
		}

		JSONObject jsonData = getParameter(request);
		if (jsonData == null) {
			return HttpResult.NO_PARAMETER;
		}

		servletData.put(JSON_DATA, jsonData);

		HttpRequestType type = null;
		try {
			type = HttpRequestType.valueOf(((String) jsonData.get(TYPE)).toLowerCase());
		} finally {
			if (type == null) {
				//noinspection ReturnInsideFinallyBlock
				log.error("缺失请求参数：" + jsonData.get(TYPE));
				return HttpResult.NO_SET_REQUEST_TYPE;
			}
		}

		servletData.put(TYPE, type);

		HttpResult httpResult = checkUserExists(servletData, request);
		if (httpResult != null) {
			return httpResult;
		}

		return null;
	}

	private JSONObject getParameter(HttpServletRequest request) {
		BufferedReader reader;
		try {
			reader = request.getReader();

			String input;
			StringBuilder builder = new StringBuilder();

			while ((input = reader.readLine()) != null) {
				builder.append(input);
			}

			String result = builder.toString();

			log.debug("从客户端收到请求：\t" + result);
			return JSONObject.parseObject(result);
		} catch (IOException e) {
			log.error("获得参数错误：", e);
			return null;
		}
	}


	/**
	 * 检查用户是否存在.
	 * <p>
	 * 如子类不需要操作此方法，直接返回null
	 *
	 * @param servletData 本流程对应数据保存对象。
	 * @param request     请求的对象。
	 * @return 正常检查返回 null 异常返回 异常对象。
	 */
	private HttpResult checkUserExists(ServletData servletData, HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute(USER);
		if (user == null) {
			return HttpResult.NOT_LOGIN;
		}

		servletData.put(USER, user);
		servletData.put(COMPANY, user.getCompany());
		return null;
	}

	protected abstract HttpResult execute(HttpRequestType type, ServletData servletData);

	protected class ServletData extends HashMap<String, Object> {
		ServletData() {
			super(20);
		}
	}

	private void setTestValue(ServletRequest req) {
		((HttpServletRequest) req).getSession().setAttribute(USER, Context.getCurrentOperatorUser());
	}
}


















