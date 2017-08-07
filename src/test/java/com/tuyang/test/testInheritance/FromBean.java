package com.tuyang.test.testInheritance;

public class FromBean extends FromBeanBase {
	
	private boolean beanBool;
	private int beanInt;
	private String beanString;
	
	public boolean isBeanBool() {
		return beanBool;
	}
	
	public void setBeanBool(boolean beanBool) {
		this.beanBool = beanBool;
	}
	

	public int getBeanInt() {
		return beanInt;
	}
	public void setBeanInt(int beanInt) {
		this.beanInt = beanInt;
	}

	public String getBeanString() {
		return beanString;
	}
	public void setBeanString(String beanString) {
		this.beanString = beanString;
	}
}