package com.jg.workflow.process.handle;

import com.jg.common.persistence.AbstractPersistence;
import com.jg.identification.User;

/**
 * 操作类的数据对象,此类仅代表数据操作类本身与数据库对接.
 * <p>
 * 当需要新增和删除某一个操作时，需要使用到此类。
 */
public final class HandleData extends AbstractPersistence {
	protected HandleData(HandleDataFactory factory, User user) {
		super(factory);
	}

	@Override
	protected boolean insertWithSerial(String sqlstr) {
		boolean result = super.insertWithSerial(sqlstr);

		HandleFactory.addHandle(this);

		return result;
	}
}
