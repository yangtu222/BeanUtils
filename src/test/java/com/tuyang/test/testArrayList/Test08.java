package com.tuyang.test.testArrayList;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
		
		Long[] longArray = new Long[3];
		longArray[0] = Long.valueOf(1000);
		longArray[1] = Long.valueOf(2000);
		longArray[2] = Long.valueOf(3000);
		fromBean.setBeanLongArray(longArray);
		
		List<Float> floatArray = new ArrayList<>();
		floatArray.add(new Float(100.1));
		floatArray.add(new Float(100.2));
		floatArray.add(new Float(100.3));
		fromBean.setBeanFloatArray(floatArray);
		

		List<Double> doubles = new ArrayList<>();
		doubles.add(new Double(100.11));
		doubles.add(new Double(100.22));
		doubles.add(new Double(100.33));

		fromBean.setBeanDoubleArray(doubles);

		FromBean2[] bean2s = new FromBean2[3];
		
		FromBean2 bean2 = new FromBean2();
		bean2.setBeanFloat(10.5f);
		bean2.setBeanString("bean2");
		bean2s[0] = bean2;
		bean2s[1] = bean2;
		bean2s[2] = bean2;
		fromBean.setBean2s(bean2s);
		
		List<FromBean2> bean2sList = new ArrayList<>();
		bean2sList.add(bean2);
		bean2sList.add(bean2);
		bean2sList.add(bean2);
		bean2sList.add(bean2);

		fromBean.setBean2sList(bean2sList);
		return fromBean;
	}

	@Test
	public void testArrayList() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
		assertEquals(fromBean.getBean2s().length, toBean.getToBeans().size());
		assertEquals(fromBean.getBean2sList().size(), toBean.getBean2sList().length);
		
		assertEquals(fromBean.getBeanIntArray().length, toBean.getBeanIntArray().size());
		assertEquals(fromBean.getBeanLongArray().length, toBean.getBeanLongArray().size());
		assertEquals(fromBean.getBeanFloatArray().size(), toBean.getBeanFloatArray().length);
		assertEquals(fromBean.getBeanDoubleArray().size(), toBean.getBeanDoubleArray().length);

		assertEquals(fromBean.isBeanBool(), toBean.isBeanBool() );
		assertEquals( fromBean.getBeanInt(), toBean.getBeanInt() );
		assertEquals( fromBean.getBeanString(), toBean.getBeanString() );
	}
}

