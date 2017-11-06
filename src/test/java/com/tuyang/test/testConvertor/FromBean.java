package com.tuyang.test.testConvertor;

import java.util.Date;

public class FromBean {
	
	private boolean beanBool;
	private String beanString;
	
	private int gendor;
	
	private MyEnum myEnum;
	
	private String fromEnumString;
	
	private Date dateNow;

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

	public MyEnum getMyEnum() {
		return myEnum;
	}

	public void setMyEnum(MyEnum myEnum) {
		this.myEnum = myEnum;
	}

	public String getFromEnumString() {
		return fromEnumString;
	}

	public void setFromEnumString(String fromEnumString) {
		this.fromEnumString = fromEnumString;
	}

	public Date getDateNow() {
		return dateNow;
	}

	public void setDateNow(Date dateNow) {
		this.dateNow = dateNow;
	}
	
}