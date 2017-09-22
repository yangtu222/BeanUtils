package com.tuyang.test.testFeatureIgnorePimitive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class TestFeatureIgnorePimitive {
	
	private FromBean getFromBean() {

		FromBean fromBean = new FromBean();
		
		fromBean.setBeanBool(true);
		fromBean.setBeanByte((byte)5);
		fromBean.setBeanChar((char)40);
		fromBean.setBeanShort((short)50);
		fromBean.setBeanInt(100);
		fromBean.setBeanFloat(null);
		fromBean.setBeanLong(null);
		fromBean.setBeanDouble(null);
		fromBean.setBeanString("Test test Test test.");
		return fromBean;
	}


	@Test
	public void testPrimitiveTypeWithFeature() {
		FromBean fromBean = getFromBean();
		BeanCopyUtils.dumpPropertyMapping(fromBean, ToBean.class, ToBeanOption.class);
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class, ToBeanOption.class);
		assertEquals(fromBean.isBeanBool(), toBean.getBeanBool().booleanValue());
		assertEquals( fromBean.getBeanByte(), toBean.getBeanByte().byteValue() );
		assertEquals( fromBean.getBeanChar(), toBean.getBeanChar().charValue() );
		assertEquals( fromBean.getBeanShort(), toBean.getBeanShort().shortValue() );
		assertEquals( fromBean.getBeanInt(), toBean.getBeanInt().intValue() );
		assertEquals( 0, toBean.getBeanLong());
		assertEquals( 0.0f, toBean.getBeanFloat(), 0);
		assertEquals( 0.0f, toBean.getBeanDouble(), 0 );
		assertEquals( fromBean.getBeanString(), toBean.getBeanString() );
	}
}
