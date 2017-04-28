package com.jg.common.persistence;

/**
 * 数据字段对象类.
 * <p>
 * 此对象
 */
public class Field {
	public static final String SPILT_STR = "@@";

	public Class<?> fieldClass;
	public Object defaultValue = null;
	public String name;
	public boolean mandatory;
	public boolean primaryKey;
	 String serial = null;
	private final boolean isViewField;
	final int order;

	Field(String name, Class<?> fieldClass, Object defaultValue, boolean mandatory, boolean primaryKey, int order) {
		this(name, fieldClass, defaultValue, mandatory, primaryKey, false, order);
	}

	Field(String name, Class<?> fieldClass, Object defaultValue, boolean mandatory, boolean primaryKey, boolean isViewField, int order) {
		this.fieldClass = fieldClass;
		this.defaultValue = defaultValue;
		this.name = name;
		this.mandatory = mandatory;
		this.primaryKey = primaryKey;
		this.isViewField = isViewField;
		this.order = order;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public boolean isSerial() {
		return serial != null;
	}

	public boolean isViewField() {
		return isViewField;
	}
}
