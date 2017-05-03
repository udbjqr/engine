package com.jg.common.persistence;

import com.alibaba.fastjson.JSONObject;
import com.jg.common.InternalConstant;
import com.jg.common.sql.DBHelper;
import com.jg.common.sql.DBHelperFactory;
import com.jg.common.util.StringUtil;
import com.jg.identification.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Array;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 持久化对象的虚类.
 * <p>
 * 此名称同时在数据持久层当中和程序当中使用
 */
public abstract class AbstractPersistence implements Persistence {
	protected static final Logger logger;
	private static final DBHelper dbHelper;
	private static final SimpleDateFormat format;

	static {
		logger = LogManager.getLogger(AbstractPersistence.class.getName());
		dbHelper = DBHelperFactory.getDBHelper();
		format = new SimpleDateFormat(InternalConstant.DATEFORMAT);
	}

	protected final AbstractPersistenceFactory factory;
	private final List<Field> fields;
	private final Object[] values;
	protected User user;
	//protected Map<String, Object> values = new HashMap<>();
	protected boolean isNewCreate = true;

	protected AbstractPersistence(AbstractPersistenceFactory factory) {
		this.factory = factory;
		//noinspection unchecked
		fields = factory.getFields();
		values = new Object[fields.size()];
	}

	protected void put(String name, Object value) {
		Field field = getFieldByName(name);
		if (field == null) {
			logger.error("需要插入的数据列未找到，列：" + name);
			return;
		}

		values[field.order] = value;
	}

	protected void put(Field field, Object value) {
		values[field.order] = value;
	}

	protected Object get(Field field) {
		if (field == null) {
			return null;
		}

		return values[field.order];
	}

	@Override
	public <T> T get(String name) {
		Field field = getFieldByName(name);

		if (field != null) {
			if (!canViewField(user, name)) {
				return null;
			}

			try {
				@SuppressWarnings("unchecked")
				T t = (T) this.get(field);
				return t;
			} catch (ClassCastException e) {
				logger.error("错误的类型转换。", e);
				return null;
			}
		} else {
			logger.warn("请求不存在的字段名{}。", name);
		}
		return null;
	}

	@Override
	public <T> T get(String name, T defaultValue) {
		T t = get(name);

		if (t == null) {
			logger.trace("得到的值为null,使用默认值。name:{},defaultValue:{}", name, defaultValue);
			return defaultValue;
		}

		return t;
	}


	/**
	 * 返回String类型的值
	 *
	 * @param name 字段名
	 * @return 值
	 */
	public String getToString(String name) {
		Object object = get(name);
		if (object instanceof Date) {
			return (new SimpleDateFormat(InternalConstant.DATEFORMAT).format((Date) object));
		}
		return object.toString();
	}

	@Override
	public <T> void set(String name, T value) throws WriteValueException {
		set(getFieldByName(name), value);
	}

	@Override
	public synchronized void set(Field field, Object value) throws WriteValueException {
		if (field == null) {
			WriteValueException exception = new WriteValueException("NULL", value, "请求不存在的名称");
			logger.error("设置数据值字段为空。", exception);
			throw exception;
		}

		if (value == null) {
			put(field, null);
			return;
		}

		//如果本来就是同一个对象，直接退出
		if (get(field) != null && values[field.order].equals(value)) {
			return;
		}

		if (field.fieldClass == String.class) {
			put(field, value);
			return;
		}

		try {
			if (field.fieldClass == Integer.class) {
				put(field, Integer.parseInt(value.toString()));
			} else if (field.fieldClass == Date.class) {
				Date date = null;
				if (!(value instanceof Date)) {
					date = format.parse(value.toString());
				}
				put(field, date == null ? value : date);
			} else if (field.fieldClass == Double.class) {
				put(field, Double.parseDouble(value.toString()));
			} else if (field.fieldClass == Long.class) {
				put(field, Long.parseLong(value.toString()));
			} else if (field.fieldClass == Integer[].class || field.fieldClass == String[].class) {
				try {
					Object oa = ((Array) value).getArray();
					put(field, oa);
				} catch (Exception e) {
					logger.error("转换数组出现异常：", e);
				}
			} else if (field.fieldClass == JSONObject.class) {
				put(field, JSONObject.parse(value.toString()));
			} else {
				logger.error("未能处理的类型！！指定类型：" + field.fieldClass);
			}

		} catch (NumberFormatException | ParseException e) {
			WriteValueException exception = new WriteValueException(field.name, value, "数据格式转换错误");
			logger.error("数据格式转换出错。从{} 转 {}，name:{},value:{}", value.getClass().getName(), field.fieldClass.getName(), field, value, e);
			throw exception;
		} catch (Exception e) {
			WriteValueException exception = new WriteValueException(field.name, value, "发生未知错误");
			logger.error("发生未知错误。从{} 转 {}，name:{},value:{}", value.getClass().getName(), field.fieldClass.getName(), field, value, e);
			throw exception;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized boolean delete() {
		String sql = factory.getDeleteStr();
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			if (field.primaryKey) {
				Object obj = get(field);
				if (obj == null) {
					logger.error("空值当做条件！name:" + field.name);
				} else {
					sql = sql.replace("%" + i + "$s", dbHelper.getString(obj));
				}
			}
		}

		dbHelper.update(sql);

		factory.removeObject(this);
		return false;
	}


	@Override
	public boolean flush() {
		if (isNewCreate) {
			isNewCreate = false;
			if (get("id") == null) {
				return insertWithSerial(factory.getInsertStr());
			} else {
				return insert(factory.getInsertStr());
			}
		} else {
			return updateData(factory.getUpdateStr());
		}
	}

	protected synchronized boolean updateData(String sqlstr) {
		String sql = sqlstr;
		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			//显示字段不替换
			if (!field.isViewField()) {
				Object obj = get(field);
				if (obj == null) {
					sql = sql.replace("%" + i + "$s", field.defaultValue == null ? "NULL" : String.valueOf(field.defaultValue));
				} else {
					sql = sql.replace("%" + i + "$s", dbHelper.getString(obj));
				}
			}
		}

		int ifag = dbHelper.update(sql);
		return ifag > 0;
	}


	private String[] getInsertSql(String sqlstr) {
		String[] sqls = sqlstr.split(Field.SPILT_STR);

		for (int i = 0; i < fields.size(); i++) {
			Field field = fields.get(i);
			//显示字段和排序字段不进行新增
			if (!field.isViewField()) {
				Object obj = get(field);
				if (obj == null) {
					sqls[0] = sqls[0].replace("%" + i + "$s", field.defaultValue == null ? "NULL" : String.valueOf(field.defaultValue));
				} else {
					sqls[0] = sqls[0].replace("%" + i + "$s", dbHelper.getString(obj));
				}
			}
		}

		return sqls;
	}

	protected synchronized boolean insert(String sqlstr) {
		String[] sqls = getInsertSql(sqlstr);

		return dbHelper.update(sqls[0]) > 0;
	}


	protected synchronized boolean insertWithSerial(String sqlstr) {
		String[] sqls = getInsertSql(sqlstr);

		try {
			if (factory.sequenceField != null) {
				Integer id = dbHelper.execBatchSqlWithOneValue(sqls);
				put(factory.sequenceField, id);
			} else {
				dbHelper.execBatchSql(sqls);
			}
			return true;
		} catch (SQLException e) {
			logger.error("批量插入数据出现异常。", e);
			return false;
		}
	}


	private Field getFieldByName(String name) {
		for (Field field : fields) {
			if (field.name.equals(name)) {
				return field;
			}
		}
		return null;
	}

	/**
	 * 实例对象的初始化动作，只在有数据被加载时进行.
	 */
	public void init() {

	}

	@Override
	public boolean canViewField(User user, String fieldName) {
		return user == null || user.canViewField(fieldName);
	}

	public void setIdBySequence()
	{
		if (factory.sequenceField != null) {
			set("id", dbHelper.getNextID(factory.sequenceField.serial));
		}

	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		for (Field field : fields) {
			buffer.append(field.name).append(":").append(StringUtil.converToJSONString(values[field.order])).append(",");
		}

		if (buffer.length() > 0) {
			buffer.insert(0, "{").replace(buffer.length() - 1, buffer.length(), "}");
		} else {
			buffer.append("{}");
		}

		return buffer.toString();
	}
}

