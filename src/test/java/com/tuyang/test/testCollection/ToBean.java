package com.tuyang.test.testCollection;

import java.util.List;

import com.tuyang.beanutils.annotation.BeanPropertyCollection;
import com.tuyang.beanutils.annotation.BeanPropertySource;

@BeanPropertySource(source=FromBean.class)
public class ToBean {

	private Integer beanInt;
	private long beanLong;
	private String beanString;
	
	@BeanPropertyCollection(targetClass=ToBean2.class)
	private List<ToBean2> beanList;

	public Integer getBeanInt() {
		return beanInt;
	}

	public void setBeanInt(Integer beanInt) {
		this.beanInt = beanInt;
	}

	public long getBeanLong() {
		return beanLong;
	}

	public void setBeanLong(long beanLong) {
		this.beanLong = beanLong;
	}

	public String getBeanString() {
		return beanString;
	}

	public void setBeanString(String beanString) {
		this.beanString = beanString;
	}

	public List<ToBean2> getBeanList() {
		return beanList;
	}

	public void setBeanList(List<ToBean2> beanList) {
		this.beanList = beanList;
	}

}