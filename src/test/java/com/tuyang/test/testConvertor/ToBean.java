package com.tuyang.test.testConvertor;

import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyFeature;
import com.tuyang.beanutils.annotation.CopyProperty;

@BeanCopySource(source=FromBean.class, features={CopyFeature.IGNORE_ENUM_CONVERT_EXCEPTION, CopyFeature.ENABLE_JAVA_BEAN_TO_STRING})
public class ToBean {

	private boolean beanBool;
	private String beanString;
	
	@CopyProperty(property="myEnum")
	private String myEnumString;
	
	@CopyProperty(convertor=GendorConvertor.class)
	private String gendor;
	
	@CopyProperty(property="fromEnumString")
	private MyEnum toEnum;
	
	@CopyProperty
	private String dateNow;

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

	public MyEnum getToEnum() {
		return toEnum;
	}

	public void setToEnum(MyEnum toEnum) {
		this.toEnum = toEnum;
	}

	public String getDateNow() {
		return dateNow;
	}

	public void setDateNow(String dateNow) {
		this.dateNow = dateNow;
	}
}