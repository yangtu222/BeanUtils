package com.tuyang.test.testInheritance2;

public class ToBean extends ToBeanBase {

	private boolean beanBool;
	private int toBeanInt;
	private String toBeanString;
	
	public boolean isBeanBool() {
		return beanBool;
	}
	public void setBeanBool(boolean beanBool) {
		this.beanBool = beanBool;
	}
	public int getToBeanInt() {
		return toBeanInt;
	}
	public void setToBeanInt(int toBeanInt) {
		this.toBeanInt = toBeanInt;
	}
	public String getToBeanString() {
		return toBeanString;
	}
	public void setToBeanString(String toBeanString) {
		this.toBeanString = toBeanString;
	}

	
	
}