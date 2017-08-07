package com.tuyang.test.testConvertor;

import com.tuyang.beanutils.annotation.CopyProperty;

public class ToBean {

	private boolean beanBool;
	private String beanString;
	
	@CopyProperty(convertor=GendorConvertor.class)
	private String gendor;

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

	public String getGendor() {
		return gendor;
	}

	public void setGendor(String gendor) {
		this.gendor = gendor;
	}
	
	
}