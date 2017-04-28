package com.jg.identification;

import java.util.HashMap;
import java.util.Set;

/**
 * 记录查看权限对应信息.
 * <p>
 * 此信息将以类型：值（list）的方式存储。
 */
public class LookOverPermission extends HashMap<String, Set<Integer>> {
	private static final long serialVersionUID = -2315483653855678579L;

	public LookOverPermission() {
	}

}
