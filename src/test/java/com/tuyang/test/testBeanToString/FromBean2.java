package com.tuyang.test.testBeanToString;

public class FromBean2 {
	
	private Float beanFloat;
	private String beanString;
	
	public Float getBeanFloat() {
		return beanFloat;
	}
	public void setBeanFloat(Float beanFloat) {
		this.beanFloat = beanFloat;
	}
	public String getBeanString() {
		return beanString;
	}
	public void setBeanString(String beanString) {
		this.beanString = beanString;
	}
	
	@Override
	public String toString() {
		return beanString + "|" + beanFloat;
	}
	

}