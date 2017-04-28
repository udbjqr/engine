package com.jg;

import org.apache.logging.log4j.core.util.IOUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * create by 2017/4/18.
 *
 * @author yimin
 */

public class TestOther {

	@Test
	public void testLoadFiles(){
		InputStream inStream = TestOther.class.getResourceAsStream("/testmodel.json");


		BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

		try {
			System.out.println(IOUtils.toString(reader));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
