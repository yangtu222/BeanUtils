package com.tuyang.test.testConvertor;

public class FromBean {
	
	private boolean beanBool;
	private String beanString;
	
	private int gendor;
	
	private MyEnum myEnum;

	public MyEnum getMyEnum() {
		return myEnum;
	}

	public void setMyEnum(MyEnum myEnum) {
		this.myEnum = myEnum;
	}

	public boolean isBeanBool() {
		return beanBool;
	}

	public void setBeanBool(boolean beanBool) {
		this.beanBool = beanBool;
	}

	public String getBeanString() {
		return beanString;
	}

	public void setBeanString(String beanString) {
		this.beanString = beanString;
	}

	public int getGendor() {
		return gendor;
	}

	public void setGendor(int gendor) {
		this.gendor = gendor;
	}
	
}