package com.tuyang.test.testArrayList2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class TestArrayList {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanBool(true);
		fromBean.setBeanInt(100);
		fromBean.setBeanString("Test test Test test.");

		return fromBean;
	}

	@Test
	public void testListToArray() {
		FromBean fromBean = getFromBean();
		List<FromBean> fromBeanList = Arrays.asList(fromBean, fromBean, fromBean);
		ToBean[] toBeanArray = BeanCopyUtils.copyArray(fromBeanList, ToBean.class);

		assertEquals(fromBean.isBeanBool(), toBeanArray[0].isBeanBool() );
		assertEquals( fromBean.getBeanInt(), toBeanArray[1].getBeanInt() );
		assertEquals( fromBean.getBeanString(), toBeanArray[2].getBeanString() );
	}
	
	@Test
	public void testListToList() {
		FromBean fromBean = getFromBean();
		List<FromBean> fromBeanList = Arrays.asList(fromBean, fromBean, fromBean);
		List<ToBean> toBeanList = BeanCopyUtils.copyList(fromBeanList, ToBean.class);

		assertEquals(fromBean.isBeanBool(), toBeanList.get(0).isBeanBool() );
		assertEquals( fromBean.getBeanInt(), toBeanList.get(1).getBeanInt() );
		assertEquals( fromBean.getBeanString(), toBeanList.get(2).getBeanString() );
	}
	
	@Test
	public void testArrayToArray() {
		FromBean fromBean = getFromBean();
		FromBean[] fromBeanList = new FromBean[3];
		fromBeanList[0] = fromBean;
		fromBeanList[1] = fromBean;
		fromBeanList[2] = fromBean;
		ToBean[] toBeanArray = BeanCopyUtils.copyArray(fromBeanList, ToBean.class);

		assertEquals(fromBean.isBeanBool(), toBeanArray[0].isBeanBool() );
		assertEquals( fromBean.getBeanInt(), toBeanArray[1].getBeanInt() );
		assertEquals( fromBean.getBeanString(), toBeanArray[2].getBeanString() );
	}
	
	@Test
	public void testArrayToList() {
		FromBean fromBean = getFromBean();
		FromBean[] fromBeanList = new FromBean[3];
		fromBeanList[0] = fromBean;
		fromBeanList[1] = fromBean;
		fromBeanList[2] = fromBean;
		List<ToBean> toBeanList = BeanCopyUtils.copyList(fromBeanList, ToBean.class);

		assertEquals(fromBean.isBeanBool(), toBeanList.get(0).isBeanBool() );
		assertEquals( fromBean.getBeanInt(), toBeanList.get(1).getBeanInt() );
		assertEquals( fromBean.getBeanString(), toBeanList.get(2).getBeanString() );
	}
}

