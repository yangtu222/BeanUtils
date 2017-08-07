package com.tuyang.test.testBasic;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class Test01 {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanBool(true);
		fromBean.setBeanByte((byte)5);
		fromBean.setBeanChar((char)40);
		fromBean.setBeanShort((short)50);
		fromBean.setBeanInt(100);
		fromBean.setBeanFloat(100.50f);
		fromBean.setBeanLong(234323243243243234L);
		fromBean.setBeanDouble(2342332423.23432432523523);
		fromBean.setBeanString("Test test Test test.");
		return fromBean;
	}

	@Test
	public void testBasic() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
		assertEquals(fromBean.isBeanBool(), toBean.isBeanBool() );
		assertEquals( fromBean.getBeanByte(), toBean.getBeanByte() );
		assertEquals( fromBean.getBeanChar(), toBean.getBeanChar() );
		assertEquals( fromBean.getBeanShort(), toBean.getBeanShort() );
		assertEquals( fromBean.getBeanInt(), toBean.getBeanInt() );
		assertEquals( fromBean.getBeanLong(), toBean.getBeanLong() );
		assertEquals( fromBean.getBeanFloat(), toBean.getBeanFloat() , 0);
		assertEquals( fromBean.getBeanDouble(), toBean.getBeanDouble(), 0 );
		assertEquals( fromBean.getBeanString(), toBean.getBeanString() );
	}
}

