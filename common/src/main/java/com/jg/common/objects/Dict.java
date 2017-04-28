package com.jg.common.objects;

import com.jg.common.persistence.AbstractPersistence;
import com.jg.identification.User;

/**
 * 数据字典数据对象,此类仅代表数据字典类本身与数据库对接.
 * <p>
 * 当需要新增和删除某一个字典数据时，需要使用到此类。
 */
public final class Dict extends AbstractPersistence {
	protected Dict(DictFactory factory, User user) {
		super(factory);
	}
}
