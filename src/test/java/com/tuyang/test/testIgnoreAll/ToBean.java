package com.tuyang.test.testIgnoreAll;

import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyProperty;
import com.tuyang.beanutils.annotation.CopyFeature;

@BeanCopySource(source=FromBean.class, features={CopyFeature.IGNORE_ALL_NULL_SOURCE_VALUE})
public class ToBean {

	private Integer beanInt;
	private long beanLong;
	private String beanString;
	
	@CopyProperty(property="bean2.beanString")
	private String bean2String;

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

	public String getBean2String() {
		return bean2String;
	}

	public void setBean2String(String bean2String) {
		this.bean2String = bean2String;
	}
}