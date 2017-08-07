package com.tuyang.test.testConvertor;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class Test11 {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanBool(true);
		fromBean.setBeanString("Test test Test test.");
		
		fromBean.setGendor(1);
		
		return fromBean;
	}

	@Test
	public void testBasic() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
		assertEquals(fromBean.isBeanBool(), toBean.isBeanBool() );
		assertEquals( fromBean.getBeanString(), toBean.getBeanString() );
		
		assertEquals(toBean.getGendor(), "Male");
	}
}

