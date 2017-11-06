package com.tuyang.test.testBeanToString;

import java.util.List;

public class FromBean {
	
	private int beanInt;
	private long beanLong;

	private String beanString;
	
	FromBean2 fromBean2;
	
	FromBean2[] fromBean2s;
	
	List<FromBean2> fromBean2s2;

	public List<FromBean2> getFromBean2s2() {
		return fromBean2s2;
	}

	public void setFromBean2s2(List<FromBean2> fromBean2s2) {
		this.fromBean2s2 = fromBean2s2;
	}

	public int getBeanInt() {
		return beanInt;
	}

	public void setBeanInt(int beanInt) {
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

	public FromBean2 getFromBean2() {
		return fromBean2;
	}

	public void setFromBean2(FromBean2 fromBean2) {
		this.fromBean2 = fromBean2;
	}

	public FromBean2[] getFromBean2s() {
		return fromBean2s;
	}

	public void setFromBean2s(FromBean2[] fromBean2s) {
		this.fromBean2s = fromBean2s;
	}
	
}