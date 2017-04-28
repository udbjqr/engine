package com.jg.common.persistence;

import com.jg.identification.User;
import com.jg.common.sql.DBHelper;
import com.jg.common.sql.DBHelperFactory;
import com.jg.identification.LookOverPermission;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 对象实例的工厂类.
 * <p>
 * 每一个对象均会实现一个继承类，用以获得对象实例。
 * <p>
 * 实现类做为一个单例存在，并且在构造函数当中设置对象类的表名/字段名等信息。并且实现{@link #createObject(User)}方法，
 * 并返回相对应的对象实例。
 * <p>
 * 使用此对象设置的表必须有主键，并且使用{@link #addField(String, Class, Object, boolean, boolean)}方法设置字段主键。
 * <p>
 * 继承类可修改对象操作过程当中执行的语句，{@link #setWherePrimaryKeys()}方法默认被其他方法调用。
 * <p>
 * 调用{@link #getObject(String, Object)}方法得到一个存在的对象。如此对象不存在，将返回null.调用{@link #getNewObject(User)}
 * 方法得到一个新的对象。
 * <p>
 * 每次获得一个对象时会将此对象进行缓存，使用{@link #setIsCheck(boolean)}可设置是否需要缓存，默认为true.
 */
public abstract class AbstractPersistenceFactory<T extends AbstractPersistence> {
	private static final Logger logger = LogManager.getLogger(AbstractPersistenceFactory.class.getName());
	protected static DBHelper dbHelper = DBHelperFactory.getDBHelper();
	protected final List<Field> fields = new ArrayList<>();
	private final List<T> lists = new ArrayList<>();
	protected String tableName;
	protected String wherePrimaryKeys;
	protected String insertStr;
	protected String updateStr;
	protected String deleteStr;
	protected String selectStr;
	protected String selectCondition;
	protected Field sequenceField = null;
	protected boolean hasId = true; //提供是否有ID字段，无ID字段可能某些操作无法执行。
	private boolean isAddToList = true;
	private int orderIndex = 0;

	public static Logger getLogger() {
		return logger;
	}

	protected String setSelectCondition() {
		selectCondition = "";
		return selectCondition;
	}

	/**
	 * 此方法初始化相关sql语法字串.
	 */
	protected void init() {
		setWherePrimaryKeys();
		setInsertStr();
		setSelectStr();
		setUpdateStr();
		setDeleteStr();
		setSelectCondition();
	}

	/**
	 * 创建一个记录对象.
	 *
	 * @return 一个记录对象
	 */
	public synchronized T getNewObject(User user) {
		T t = createObject(user);

		return addObject(t);
	}

	private boolean checkCondition(String key, Object value) {
		if (value == null) {
			logger.warn("未指定需要查找的对象，返回空。class:{},key:{},value:null", this.getClass().getSimpleName(), key);
			return false;
		}

		Field field = getFieldByName(key);
		if (field == null) {
			logger.error("指定的key在数据列当中查找不到:{},class:{}", key, this.getClass().getSimpleName(), new Exception());
			return false;
		}

		if (Date.class.isAssignableFrom(field.fieldClass)) {
			logger.warn("Date类型不能做为键查找存在的对象,class:{},key:{}", this.getClass().getSimpleName(), key);
			return false;
		}

		return true;
	}

	public synchronized T getObject(String key, Object value) {
		if (!checkCondition(key, value)) {
			return null;
		}

		T t = findObjectByList(key, value);
		if (t != null) {
			return t;
		}
//		logger.debug("未找到指定的对象，从数据库查找,key:{}", key);

		t = createObject(null);

		if (readFromDB(t, "select * from " + tableName + " where " + key + " = " + dbHelper.getString(value))) {
			//读取到数据后进行初始化动作。
			t.init();

			return addObject(t);
		} else {
			logger.warn("没有找到指定值的对象.Key:{},value:{}", key, value);
			return null;
		}
	}

	private T findObjectByList(String key, Object value) {
		for (T t : lists) {
			Object v = t.get(key);
			if (v == null) {
				continue;
			}
			if (v instanceof String) {
				String n = (String) v;
				if (n.equals(value.toString()))
					return t;
			} else if (v instanceof Integer) {
				if ((int) v == (int) value) {
					return t;
				}
			} else if (v instanceof Date) {
				Long l = ((Date) v).getTime();
				if (l.equals(value)) {
					return t;
				}
			} else if (v instanceof Long) {
				if ((value instanceof Integer && Long.valueOf((int) value).equals(v)) ||
					(value instanceof Long && (value).equals(v))) {
					return t;
				}
			}
		}

		return null;
	}

	/**
	 * 根据sql语句从数据库获得相应对象.
	 *
	 * @param sql 查询的sql语句。
	 */
	protected synchronized boolean readFromDB(T t, String sql) {
		boolean result = false;
		ResultSet resultSet = null;
		try {
			resultSet = dbHelper.select(sql);
			if (resultSet.next()) {
				//只取一行，从数据库里面读取所有的值。
				for (Field field : fields) {
					t.set(field, resultSet.getObject(field.name));
					result = true;
				}
				t.isNewCreate = false;
			}
		} catch (SQLException e) {
			logger.error("读取数据出现异常。sql:{}", sql, e);
		} catch (WriteValueException e) {
			logger.error("", e);
		} finally {
			dbHelper.close(resultSet);
		}
		return result;
	}

	/**
	 * 给出所有的对象的集合.
	 *
	 * @param user 当前用户。
	 * @return 对象的列表
	 */
	public List<T> getAllObjects(User user) {
		return findMultipleObject(selectStr + (selectCondition.length() > 1 ? " where " : ";") + selectCondition,
			user, -1, -1);
	}

	protected Set<Integer> getLimitList(User user) {
		if (user != null) {
			LookOverPermission permission = user.getLookOverPermission();
			if (permission != null) {
				return permission.get(tableName);
			}
		}
		return null;
	}

	/**
	 * 根据条件，给出对象的集合.
	 * <p>
	 * 此方法为底层方法，使用此方法将与sql语法藕合。尽可能使用其他方法调用 。
	 * 调用此方法得到的记录将不会记录至缓存内。
	 * <p>
	 * 调用此方法注意当数据库当中有标记被删除的对象时，同时需要在此写判断
	 *
	 * @param user      当前用户
	 * @param whereLast sql语句的where以及where后面部分
	 * @return 对象列表
	 */
	public List<T> getObjectsForString(String whereLast, User user) {
		return findMultipleObject(selectStr + whereLast, user, -1, -1);
	}

	/**
	 * 根据条件，给出对象的集合.
	 * <p>
	 * 此方法为底层方法，使用此方法将与sql语法藕合。尽可能使用其他方法调用 。
	 * 调用此方法得到的记录将不会记录至缓存内。
	 * <p>
	 * 调用此方法注意当数据库当中有标记被删除的对象时，同时需要在此写判断
	 *
	 * @param user      当前用户
	 * @param whereLast sql语句的where以及where后面部分
	 * @return 对象列表
	 */
	public List<T> getObjectsForString(String whereLast, User user, int limit, int offset) {
		return findMultipleObject(selectStr + whereLast, user, -1, -1);
	}

	/**
	 * 根据语句分页查询列表
	 */
	public List<T> getObjectsForStringByPage(String whereLast, int limit, int offset, User user) {
		return findMultipleObject(selectStr + whereLast, user, limit, offset);
	}

	private synchronized List<T> findMultipleObject(String sql, User user, int limit, int offset) {
		List<T> returns = new ArrayList<>();
		Set<Integer> limits = getLimitList(user);
		List<String> PKs = fields.stream().filter(field -> field.primaryKey).map(field -> field.name).collect(Collectors.toList());

		ResultSet resultSet = null;
		try {
			if (limit < 0) {
				resultSet = dbHelper.select(sql);
			} else {
				resultSet = dbHelper.select(sql, limit, offset);
			}

			while (resultSet.next()) {
				boolean find = false;
				//从列表当中查找
				for (T t : lists) {
					find = true;
					for (String PK : PKs) {
						//当有一个主键值不符合，即认为未找到。
						Object value = t.get(PK);
						if (value instanceof String) {
							String v = (String) value;
							if (!v.equals(resultSet.getObject(PK))) {
								find = false;
								break;
							}
						} else if (value instanceof Integer) {
							Integer v = (Integer) value;
							if (!v.equals(resultSet.getObject(PK))) {
								find = false;
								break;
							}
						} else if (value instanceof Long) {
							Long v = (Long) value;
							if (!v.equals(resultSet.getObject(PK))) {
								find = false;
								break;
							}
						} else if (value == null) {
							find = false;
							break;
						}
					}

					//如果在列表当中找到对象，跳出for循环。
					if (find) {
						addToReturn(returns, t, limits);
						break;
					}
				}

				//如果在列表当中已经找到对象，则不再增加
				if (find) {
					continue;
				}

				T t = createObject(user);
				for (Field field : fields) {
					t.set(field, resultSet.getObject(field.name));
				}
				t.isNewCreate = false;
				t.init();

				addToReturn(returns, t, limits);

				//将新增加的对象加入列表当中
				addObject(t);
			}
		} catch (SQLException e) {
			logger.error("读取数据出现异常。sql:{}", sql, e);
		} catch (WriteValueException e) {
			logger.error("", e);
		} finally {
			dbHelper.close(resultSet);
		}
		return returns;
	}

	private void addToReturn(List<T> returns, T t, Set<Integer> limits) {
		//目前无ID字段直接增加进入对象内。
		if (!hasId) {
			returns.add(t);
		}

		Object id = t.get("id");
		if (id instanceof Long) {
			id = ((Long) id).intValue();
		}

		//noinspection SuspiciousMethodCalls
		if (limits == null || limits.isEmpty() || limits.contains(id)) {
			returns.add(t);
//		} else {
//			logger.trace("被限制访问，不增加,id:" + t.get("id"));
		}
	}

	/**
	 * 得到符合条件的对象列表.
	 *
	 * @param user  当前用户
	 * @param key   条件名称
	 * @param value 条件值
	 * @return 对象的列表
	 */
	public synchronized List<T> getMultipleObjects(String key, Object value, User user) {
		if (!checkCondition(key, value)) {
			return null;
		}

		String string = selectStr + " where " + key + " = " + dbHelper.getString(value) +
			(selectCondition.length() > 1 ? " and " + selectCondition : "") + ";";

		return findMultipleObject(string, user, -1, -1);

	}

	/**
	 * 从数据库查询的数据分页.
	 *
	 * @param key    条件名称
	 * @param value  条件值
	 * @param limit  显示的行数
	 * @param offset 偏移量
	 * @param user   当前用户
	 * @return 对象的列表
	 */
	public synchronized List<T> getMultipleObjectsByPage(String key, Object value, int limit, int offset, User user) {
		if (!checkCondition(key, value)) {
			return null;
		}

		String string = selectStr + " where " + key + " = " + dbHelper.getString(value) +
			(selectCondition.length() > 1 ? " and " + selectCondition : "") + ";";

		return findMultipleObject(string, user, limit, offset);
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
	 * 继承类实现此方法，设置与数据库持久层相关的对象.
	 *
	 * @return 相对应的对象。
	 */
	protected abstract T createObject(User user);

	/**
	 * 使用此方法将定义对应持久层的列.
	 *
	 * @param name         列的名称
	 * @param defaultValue 默认值,此值标明
	 * @param isMandatory  是否不能为空
	 * @param isPrimaryKey 是否主键
	 */
	protected Field addField(String name, Class fieldClass,
	                         Object defaultValue, boolean isMandatory, boolean isPrimaryKey) {
		return addField(name, fieldClass, defaultValue, isMandatory, isPrimaryKey, false);
	}

	/**
	 * 使用此方法将定义对应持久层的列.
	 *
	 * @param name         列的名称
	 * @param defaultValue 默认值,此值标明
	 * @param isMandatory  是否不能为空
	 * @param isPrimaryKey 是否主键
	 * @param isViewField  此字段是否显示字段，显示字段不参于数据的插入与修改
	 */
	protected Field addField(String name, Class fieldClass,
	                         Object defaultValue, boolean isMandatory, boolean isPrimaryKey, boolean isViewField) {

		Field field = new Field(name, fieldClass, defaultValue, isMandatory, isPrimaryKey, isViewField, orderIndex++);

		fields.add(field);
		return field;
	}

	protected String setWherePrimaryKeys() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {
			Field f = fields.get(i);
			if (f.primaryKey) {
				if (stringBuilder.length() == 0) {
					stringBuilder.append(" where ");
				} else {
					stringBuilder.append(" and ");
				}

				stringBuilder.append(f.name).append(" = ");
				stringBuilder.append("%").append(i).append("$s");
			}
		}
		stringBuilder.append(";");

		this.wherePrimaryKeys = stringBuilder.toString();
		return this.wherePrimaryKeys;
	}

	/**
	 * 关键字用小写
	 *
	 * @return 插入语句
	 */
	protected String setInsertStr() {
		StringBuilder fieldNames = new StringBuilder();
		StringBuilder valuesString = new StringBuilder();
		fieldNames.append("insert into ").append(tableName).append("(");
		valuesString.append(" values(");
		fields.stream().filter(field -> !field.isViewField()).forEach(field -> {
			fieldNames.append(field.name).append(",");
			putStringFormatter(valuesString, field.order);
		});

		fieldNames.replace(fieldNames.length() - 1, fieldNames.length(), ")");
		valuesString.replace(valuesString.length() - 1, valuesString.length(), ")");

		fieldNames.append(valuesString).append(";");
		if (sequenceField != null) {
			fieldNames.append(Field.SPILT_STR).append(" select currval('").append(sequenceField.serial).append("')::INTEGER");
		}

		this.insertStr = fieldNames.toString();
		return insertStr;
	}

	/**
	 * 关键字用小写，并且最后不带“;”号
	 *
	 * @return 修改语句
	 */
	protected String setUpdateStr() {
		StringBuilder fieldNames = new StringBuilder();
		fieldNames.append("update ").append(tableName).append(" set ");
		for (Field field : fields) {
			if (field.primaryKey || field.isViewField()) {
				continue;
			}

			fieldNames.append(field.name).append(" = ");
			putStringFormatter(fieldNames, field.order);
		}

		fieldNames.delete(fieldNames.length() - 1, fieldNames.length());

		this.updateStr = fieldNames.toString() + wherePrimaryKeys;
		return updateStr;
	}

	protected String setDeleteStr() {
		this.deleteStr = "delete from " + tableName + wherePrimaryKeys;
		return this.deleteStr;
	}

	/**
	 * 查询不带关键字'where',并且没有结束。
	 *
	 * @return 查询语句
	 */
	protected String setSelectStr() {
		selectStr = " select t.* from " + tableName + " t ";
		return selectStr;
	}

	/**
	 * 将格式化字符串放至字符串中.
	 *
	 * @param stringBuilder StringBuilder对象
	 */
	private void putStringFormatter(StringBuilder stringBuilder, int i) {
		stringBuilder.append("%").append(i).append("$s,");
	}

	synchronized T removeObject(T object) {
		lists.remove(object);
		return object;
	}

	protected synchronized T addObject(T object) {
		if (isAddToList) {
			lists.add(object);
		}
		return object;
	}

	protected void setIsCheck(boolean addToList) {
		this.isAddToList = addToList;
	}

	public List<T> getLists() {
		return lists;
	}

	List<Field> getFields() {
		return fields;
	}

	String getInsertStr() {
		return insertStr;
	}

	String getUpdateStr() {
		return updateStr;
	}

	String getDeleteStr() {
		return deleteStr;
	}

	Field getSequenceField() {
		return sequenceField;
	}

	/**
	 * 清除缓冲的数据
	 */
	public void clear() {
		lists.clear();
	}

}
