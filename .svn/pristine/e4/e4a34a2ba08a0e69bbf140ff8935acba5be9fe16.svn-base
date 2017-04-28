package com.jg.workflow;

import com.jg.common.result.ResultCode;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * create by 2017/4/7.
 *
 * @author yimin
 */

public class TestPattern {
	@Test
	public void test() {
//		Pattern p = Pattern.compile("\\d+");
		Pattern p = Pattern.compile("\\$\\{.+?}");
		Matcher m = p.matcher("我的${QQ}是:456456${我的}电话是:0532214我的邮箱是:aaa@aaa.com");
//		Matcher m = p.matcher("我的QQ是:456456 我的电话是:0532214 我的邮箱是:aaa123@aaa.com");
		while (m.find()) {
			System.out.println(m.group());
		}

//		System.out.print(m.start());
//		for (String s : m.) {
//			System.out.println(s);
//		}
	}


	@Test
	public void test2(){
		ResultCode t = new ResultCode(0,"");

		System.out.println(t);
	}
}
