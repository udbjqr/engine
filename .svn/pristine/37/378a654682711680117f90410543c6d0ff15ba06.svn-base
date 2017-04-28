package com.jg.common.util;

import java.util.List;

/**
 * 分页方法
 * @author Administrator
 *
 */
public class PageUtil {
	
	
	/**
	 * 分页方法
	 * @param currentPage 当前页
	 * @param showRow 显示的行数
	 * @param list 
	 * @return
	 */
	public static <T> List<T> getPageList(int currentPage,int showRow,List<T> list){
		int count = list.size();
		int param2 = showRow*currentPage;
		
		if(count==0 || showRow > count){
			showRow = count;
		}
		
		if(param2 > count){
			param2 = count;
		}
		List<T> subList = list.subList(showRow*(currentPage-1), param2);
		return subList;
	}

}
