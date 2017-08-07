package com.tuyang.test.testOption;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class Test07 {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanBool(true);

		fromBean.setBeanInt(100);

		fromBean.setBeanString("Test test Test test.");
		return fromBean;
	}

	@Test
	public void testOption() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class, ToBeanOption.class);
		assertEquals(fromBean.isBeanBool(), toBean.isBeanBool() );
		assertEquals( fromBean.getBeanInt(), toBean.getToBeanInt() );
		assertEquals( fromBean.getBeanString(), toBean.getToBeanString() );
	}
}

