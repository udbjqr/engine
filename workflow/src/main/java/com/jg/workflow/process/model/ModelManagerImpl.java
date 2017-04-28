package com.jg.workflow.process.model;

import com.alibaba.fastjson.JSONObject;
import com.jg.identification.Company;
import com.jg.identification.Context;
import com.jg.workflow.exception.ModelNotFound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.jg.workflow.process.model.ModelFactory.MODEL_FACTORY;

/**
 * 模型管理接口实现类.
 *
 * @author yimin
 */

public class ModelManagerImpl implements ModelManager {
	private static final Logger log = LogManager.getLogger(ModelManagerImpl.class.getName());
	private static final Map<Company, ModelManagerImpl> MANAGERS = new HashMap<>();

	public Company getCompany() {
		return company;
	}

	private final Company company;

	private ModelManagerImpl(Company company) {
		this.company = company;

		init();
	}

	private void init() {
	}

	public synchronized static ModelManagerImpl getInstance(Company company) {
		if (company == null) {
			log.warn("企业为空。");
			return null;
		}

		if (!MANAGERS.containsKey(company)) {
			MANAGERS.put(company, new ModelManagerImpl(company));
		}

		return MANAGERS.get(company);
	}

	@Override
	public Model createModel() {
		return MODEL_FACTORY.createObject(null);
	}

	@Override
	public void updateModel(int modelId, JSONObject structure, String viewInformation) {
		Model model = getModel(modelId);

		model.set("content", structure);
		model.set("svg", viewInformation);
		model.set("update_time", new Date(System.currentTimeMillis()));
		model.flush();
	}

	@Override
	public void deleteModel(int modelId) {
		getModel(modelId).delete();
	}

	@Override
	public List<Model> getModels(String condition, int offset, int limit) {
		return new ArrayList<>(MODEL_FACTORY.getObjectsForString(condition, Context.getCurrentOperatorUser(), limit, offset));
	}

	@Override
	public List<Model> getAllModels(String condition) {
		return getModels(condition, -1, -1);
	}

	@Override
	public Model getModel(int modelId) {
		Model model = MODEL_FACTORY.getObject("id", modelId);
		if (model == null) {
			throw new ModelNotFound(modelId);
		}

		return model;
	}
}
