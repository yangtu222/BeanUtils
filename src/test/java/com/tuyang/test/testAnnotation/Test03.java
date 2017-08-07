package com.tuyang.test.testAnnotation;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class Test03 {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanInt(100);
		fromBean.setBeanLong(200);
		fromBean.setBeanString("Test test Test test.");
		return fromBean;
	}

	@Test
	public void testAnnotation() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
		assertEquals( fromBean.getBeanInt(), toBean.getToInt().intValue() );
		assertEquals( 0, toBean.getBeanLong());
		assertEquals( fromBean.getBeanString(), toBean.getBeanString() );
	}
}

