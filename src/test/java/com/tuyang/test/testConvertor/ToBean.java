package com.tuyang.test.testConvertor;

import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyProperty;

@BeanCopySource(source=FromBean.class)
public class ToBean {

	private boolean beanBool;
	private String beanString;
	
	@CopyProperty(property="myEnum", convertor=MyEnumConvertor.class)
	private String myEnumString;
	
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

	public String getMyEnumString() {
		return myEnumString;
	}

	public void setMyEnumString(String myEnumString) {
		this.myEnumString = myEnumString;
	}

	public String getGendor() {
		return gendor;
	}

	public void setGendor(String gendor) {
		this.gendor = gendor;
	}

}