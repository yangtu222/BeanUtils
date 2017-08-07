package com.tuyang.test.testArray;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class Test08 {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanBool(true);
		fromBean.setBeanInt(100);
		fromBean.setBeanString("Test test Test test.");

		int[] intArray = new int[3];
		intArray[0] = 100;
		intArray[1] = 200;
		intArray[2] = 300;
		fromBean.setBeanIntArray(intArray);
		
		long[] longArray = new long[3];
		longArray[0] = 1000;
		longArray[1] = 2000;
		longArray[2] = 3000;
		fromBean.setBeanLongArray(longArray);
		
		Float[] floatArray = new Float[3];
		floatArray[0] = new Float(100.1);
		floatArray[1] = new Float(100.2);
		floatArray[2] = new Float(100.3);
		fromBean.setBeanFloatArray(floatArray);
		

		Double[] doubles = new Double[3];
		doubles[0] = new Double(100.11);
		doubles[1] = new Double(100.22);
		doubles[2] = new Double(100.33);
		fromBean.setBeanDoubleArray(doubles);

		FromBean2[] bean2s = new FromBean2[3];
		
		FromBean2 bean2 = new FromBean2();
		bean2.setBeanFloat(10.5f);
		bean2.setBeanString("bean2");
		bean2s[0] = bean2;
		bean2s[1] = bean2;
		bean2s[2] = bean2;
		fromBean.setBean2s(bean2s);
		
		return fromBean;
	}

	@Test
	public void testArray() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
		assertEquals(fromBean.getBean2s().length, toBean.getToBeans().length);
		assertEquals(fromBean.getBeanIntArray().length, toBean.getBeanIntArray().length);
		assertEquals(fromBean.getBeanLongArray().length, toBean.getBeanLongArray().length);
		assertEquals(fromBean.getBeanFloatArray().length, toBean.getBeanFloatArray().length);
		assertEquals(fromBean.getBeanDoubleArray().length, toBean.getBeanDoubleArray().length);

		assertEquals(fromBean.isBeanBool(), toBean.isBeanBool() );
		assertEquals( fromBean.getBeanInt(), toBean.getBeanInt() );
		assertEquals( fromBean.getBeanString(), toBean.getBeanString() );
	}
}

