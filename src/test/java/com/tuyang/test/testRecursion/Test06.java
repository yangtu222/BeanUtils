package com.tuyang.test.testRecursion;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class Test06 {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanInt(100);
		fromBean.setBeanLong(200);
		fromBean.setBeanString("Test test Test test.");
		
		FromBean8 bean8 = new FromBean8();
		bean8.setBeanFloat(10.4f);
		bean8.setBeanString("bean8");
		
		FromBean7 bean7 = new FromBean7();
		bean7.setBean8(bean8);
		
		FromBean6 bean6 = new FromBean6();
		bean6.setBean7(bean7);
		
		FromBean5 bean5 = new FromBean5();
		bean5.setBean6(bean6);
		
		FromBean4 bean4 = new FromBean4();
		bean4.setBean5(bean5);
		
		FromBean3 bean3 = new FromBean3();
		bean3.setBean4(bean4);
		
		FromBean2 bean2 = new FromBean2();
		bean2.setBean3(bean3);
		bean2.setBeanInt(100);
		bean2.setBeanLong(200L);
		bean2.setBeanString("Test bean2");
		
		fromBean.setBean2(bean2);
		
		return fromBean;
	}

	@Test
	public void testRecursion() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
		
		assertEquals(fromBean.getBean2().getBeanInt(), toBean.getBean2Int());
		assertEquals(fromBean.getBean2().getBeanLong(), toBean.getBean2Long().longValue());
		assertEquals(fromBean.getBean2().getBeanString(), toBean.getBean2Str());
		
		assertEquals( fromBean.getBean2().getBean3().getBean4().getBean5().getBean6().getBean7().getBean8().getBeanFloat().floatValue(), 
				toBean.getBean8Float(), 0 );
		assertEquals( fromBean.getBean2().getBean3().getBean4().getBean5().getBean6().getBean7().getBean8().getBeanString(), 
				toBean.getBean8String() );
		assertEquals( fromBean.getBeanInt(), toBean.getBeanInt().intValue() );
		assertEquals( fromBean.getBeanLong(), toBean.getBeanLong());
		assertEquals( fromBean.getBeanString(), toBean.getBeanString() );
	}
}

