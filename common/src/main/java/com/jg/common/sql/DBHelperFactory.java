package com.jg.common.sql;

import com.jg.common.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Loader;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 得到数据库操作对象的工厂类.
 *
 * @author yimin
 */
public final class DBHelperFactory {
	private static final Logger logger = LogManager.getLogger(DBHelperFactory.class.getName());

	private static final String PROPERTIES_FILENAME = "DataBase.properties";
	public static DBHelper DB_HELPER;
	private static Properties properties;

	private static final Map<String, DBHelper> helpers = new HashMap<>();

	private DBHelperFactory() {

	}

	static {
		String string4File = FileUtil.getProperties(Loader.getResource(PROPERTIES_FILENAME, DBHelperFactory.class.getClassLoader()), "ISO-8859-1");
		properties = FileUtil.getPropertiesFromString(string4File);

		DB_HELPER = newIns("");
	}

	/**
	 * 得到一个数据操作对象.
	 * <p>
	 * 如果读取不到文件，或者文件参数错误，写日志对象，并且返回NULL.
	 *
	 * @return 数据库操作对象，如出现异常可能返回空。
	 */
	public static DBHelper getDBHelper() {
		return DB_HELPER;
	}

	/**
	 * 给出指定后缀数据库连接方式与对象.
	 * <p>
	 * 此给出的对象单独对应，与其他互不影响。
	 *
	 * @param suffix 后缀
	 * @return 数据操作对象
	 */
	public synchronized static DBHelper getSecondDBHelper(String suffix) {
		if (!helpers.containsKey(suffix)) {
			DBHelper dbHelper = newIns(suffix);
			helpers.put(suffix, dbHelper);
		}
		
		return helpers.get(suffix);
	}

	private static DBHelper newIns(String suffix) {
		DBType type;
		try {
			type = DBType.valueOf(properties.getProperty("DBType" + suffix));
		} catch (Exception e) {
			logger.error("数据库类型设置错误,值:{}", properties.getProperty("DBType" + suffix), e);
			return null;
		}

		AbstractDataBaseHandle handle;
		switch (type) {
			case MYSQL:
				handle = new MysqlHandle(suffix);
				break;
			case MSSQL:
				handle = new MSSQLSERVERHandle(suffix);
				break;
			case ORACLE:
				handle = new OracleHandle(suffix);
				break;
			case PGSQL:
				handle = new PostgresqlHandle(suffix);
				break;
			default:
				logger.error("此数据库操作对象还未实现.");
				return null;
		}


		return handle.init();
	}

	static Properties getProperties() {
		return properties;
	}
}
