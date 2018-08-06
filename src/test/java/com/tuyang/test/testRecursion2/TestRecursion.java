package com.tuyang.test.testRecursion2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class TestRecursion {

	private FromBean generateBean(int i) {
		
		FromBean newBean = new FromBean();
		newBean.setName("S" + i);
		if(i == 0) {
			newBean.setParent(null);
		}
		else {
			FromBean parentBean = generateBean(i-1);
			newBean.setParent(parentBean);
		}
		return newBean;
	}

	@Test
	public void testRecursion() {
		FromBean fromBean = generateBean(10);
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
		assertEquals(toBean.getParent().getParent().getParent().getParent().getName(),
					 fromBean.getParent().getParent().getParent().getParent().getName());
		
	}
}
