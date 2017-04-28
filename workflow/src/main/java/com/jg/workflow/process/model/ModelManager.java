package com.jg.workflow.process.model;


import com.alibaba.fastjson.JSONObject;
import com.jg.identification.Company;

import java.util.List;

/**
 * 模型对象管理接口
 *
 * @author yimin
 */
public interface ModelManager {
	/**
	 * 创建一个新的模型.
	 * <p>
	 * 调用此方法模型未被保存至持久层。
	 * 需要调用{@link com.jg.workflow.process.model.Model#flush()}方法进行保存.
	 *
	 * @return 新的模型对象
	 */
	Model createModel();

	/**
	 * 更新一个流程的内容
	 * <p>
	 * 调用此方法模型未被保存至持久层。
	 * 需要调用{@link com.jg.workflow.process.model.Model#flush()}方法进行保存.
	 *
	 * @param modelId         模型的Id
	 * @param structure       模型的流程定义结构
	 * @param viewInformation 模型的可视化的结构
	 */
	void updateModel(int modelId, JSONObject structure, String viewInformation);

	void deleteModel(int modelId);

	/**
	 * 得到当前企业模型列表.
	 *
	 * @param offset    从第几个模型开始获取
	 * @param limit     获得的行数，如果实际获得行数小于此值，返回实际行数
	 * @param condition 查询的条件，此处必须带有order by 字段，否则可能返回错乱.
	 * @return 模型的列表
	 */
	List<Model> getModels(String condition, int offset, int limit);

	/**
	 * 得到当前企业所有的模型对象
	 *
	 * @return 模型对象的列表
	 */
	List<Model> getAllModels(String condition);

	/**
	 * 根据模型Id，返回对应的模型对象.
	 * <p>
	 * 如未找到指定的模型对象，将弹出未找到模型异常.
	 *
	 * @param modelId 模型ID
	 * @return 模型对象
	 */
	Model getModel(int modelId);

	/**
	 * 得到模型管理器的企业对象.
	 *
	 * @return 对应的企业对象
	 */
	Company getCompany();
}
