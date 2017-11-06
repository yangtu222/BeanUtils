package com.tuyang.test.testBeanToString;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class Test03 {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanInt(100);
		fromBean.setBeanLong(200);
		fromBean.setBeanString("Test test Test test.");
		
		FromBean2 bean2 = new FromBean2();
		bean2.setBeanFloat(10.4f);
		bean2.setBeanString("bean2");
		fromBean.setFromBean2(bean2);
		
		FromBean2[] bean2s = new FromBean2[2];
		bean2s[0] = bean2;
		bean2s[1] = bean2;
		fromBean.setFromBean2s(bean2s);
		
		fromBean.setFromBean2s2(Arrays.asList(bean2s));
		
		return fromBean;
	}

	@Test
	public void testAnnotation() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
		assertEquals( fromBean.getBeanInt(), toBean.getToInt().intValue() );
		assertEquals( 0, toBean.getBeanLong());
		assertEquals( fromBean.getBeanString(), toBean.getBeanString() );
		assertEquals(fromBean.getFromBean2().toString(), toBean.getFromBean2());
	}
}

