package com.tuyang.test.testInheritance;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class Test09 {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanBool(true);
		fromBean.setBeanInt(100);
		fromBean.setBeanString("Test test Test test.");
		
		fromBean.setBaseBool(true);
		fromBean.setBaseByte((byte)5);
		fromBean.setBaseChar((char)40);
		fromBean.setBaseShort((short)50);
		fromBean.setBaseInt(100);
		fromBean.setBaseFloat(100.50f);
		fromBean.setBaseLong(234323243243243234L);
		fromBean.setBaseDouble(2342332423.23432432523523);
		fromBean.setBaseString("Test test Test test.");

		return fromBean;
	}

	@Test
	public void testInheritance() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
		assertEquals(fromBean.isBeanBool(), toBean.isBeanBool() );
		assertEquals( fromBean.getBeanInt(), toBean.getToBeanInt() );
		assertEquals( fromBean.getBeanString(), toBean.getToBeanString() );
		assertEquals(fromBean.getBaseByte(), toBean.getBaseByte() );
		assertEquals(fromBean.getBaseChar(), toBean.getBaseChar() );
		assertEquals(fromBean.getBaseDouble(), toBean.getBaseDouble(), 0 );
		assertEquals(fromBean.getBaseFloat(), toBean.getBaseFloat(), 0 );
		assertEquals(fromBean.getBaseInt(), toBean.getBaseInt() );
		assertEquals(fromBean.getBaseLong(), toBean.getBaseLong() );
		assertEquals(fromBean.getBaseShort(), toBean.getBaseShort() );
		assertEquals(fromBean.getBaseString(), toBean.getBaseString() );
	}
}

