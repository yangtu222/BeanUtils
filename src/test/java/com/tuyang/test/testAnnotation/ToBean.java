package com.tuyang.test.testAnnotation;

import com.tuyang.beanutils.annotation.BeanProperty;
import com.tuyang.beanutils.annotation.BeanPropertySource;

@BeanPropertySource(source=FromBean.class)
public class ToBean {

	@BeanProperty(property="beanInt")
	private Integer toInt;
	@BeanProperty(ignored=true)
	private long beanLong;
	
	private String beanString;
	
	public Integer getToInt() {
		return toInt;
	}
	public void setToInt(Integer toInt) {
		this.toInt = toInt;
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

	
	
}