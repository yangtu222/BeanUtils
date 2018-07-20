package com.tuyang.test.testIgnoreAll;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class TestIgnoreAll {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanInt(100);
		fromBean.setBeanLong(200);
		fromBean.setBeanString(null);
		
		FromBean2 bean2 = new FromBean2();
		bean2.setBeanFloat(10.4f);
		bean2.setBeanString(null);
		
		fromBean.setBean2(bean2);
		
		return fromBean;
	}

	@Test
	public void testIgnoreAll() {
		FromBean fromBean = getFromBean();
		ToBean toBean = new ToBean();
		toBean.setBeanString("Not Empty");
		toBean.setBean2String("Not Empty");
		BeanCopyUtils.copyBean(fromBean, toBean);
		assertEquals( toBean.getBeanString(), "Not Empty" );
		assertEquals( toBean.getBean2String(), "Not Empty" );
	}
}

