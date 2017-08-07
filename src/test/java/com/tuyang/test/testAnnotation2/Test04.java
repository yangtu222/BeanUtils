package com.tuyang.test.testAnnotation2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class Test04 {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanInt(100);
		fromBean.setBeanLong(200);
		fromBean.setBeanString("Test test Test test.");
		
		FromBean2 bean2 = new FromBean2();
		bean2.setBeanFloat(10.4f);
		bean2.setBeanString("bean2");
		
		fromBean.setBean2(bean2);
		
		List<FromBean2> list = new ArrayList<>();
		list.add(bean2);
		list.add(bean2);
		fromBean.setBeanList(list);
		
		return fromBean;
	}

	@Test
	public void testAnnotation2() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
		assertNotNull(toBean.getBean2());
		assertEquals( toBean.getBeanList().size(), 2 );
		assertEquals( fromBean.getBeanInt(), toBean.getBeanInt().intValue() );
		assertEquals( fromBean.getBeanLong(), toBean.getBeanLong());
		assertEquals( fromBean.getBeanString(), toBean.getBeanString() );
	}
}

