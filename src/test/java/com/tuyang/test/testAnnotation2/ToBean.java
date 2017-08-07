package com.tuyang.test.testAnnotation2;

import java.util.List;

import com.tuyang.beanutils.annotation.BeanProperty;
import com.tuyang.beanutils.annotation.BeanPropertySource;

@BeanPropertySource(source=FromBean.class)
public class ToBean {

	private Integer beanInt;
	private long beanLong;
	private String beanString;
	
	@BeanProperty
	private ToBean2 bean2;
	
	private List<FromBean2> beanList;

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

	public List<FromBean2> getBeanList() {
		return beanList;
	}

	public void setBeanList(List<FromBean2> beanList) {
		this.beanList = beanList;
	}

	public ToBean2 getBean2() {
		return bean2;
	}

	public void setBean2(ToBean2 bean2) {
		this.bean2 = bean2;
	}
	
	

}