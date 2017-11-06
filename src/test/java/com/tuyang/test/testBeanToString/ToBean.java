package com.tuyang.test.testBeanToString;

import com.tuyang.beanutils.annotation.CopyProperty;

import java.util.List;

import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyCollection;
import com.tuyang.beanutils.annotation.CopyFeature;

@BeanCopySource(source=FromBean.class, features={CopyFeature.ENABLE_JAVA_BEAN_TO_STRING})
public class ToBean {

	@CopyProperty(property="beanInt")
	private Integer toInt;
	@CopyProperty(ignored=true)
	private long beanLong;
	
	private String beanString;
	
	@CopyProperty
	private String fromBean2;
	
	@CopyProperty
	private String[] fromBean2s;
	
	@CopyCollection(property="fromBean2s2", targetClass=String.class)
	List<String> fromBean2s2_1;
	
	@CopyProperty(property="fromBean2s2")
	private String[] fromBean2s2_2;

	public List<String> getFromBean2s2_1() {
		return fromBean2s2_1;
	}

	public void setFromBean2s2_1(List<String> fromBean2s2_1) {
		this.fromBean2s2_1 = fromBean2s2_1;
	}

	public String[] getFromBean2s2_2() {
		return fromBean2s2_2;
	}

	public void setFromBean2s2_2(String[] fromBean2s2_2) {
		this.fromBean2s2_2 = fromBean2s2_2;
	}

	public Integer getToInt() {
		return toInt;
	}

	public void setToInt(Integer toInt) {
		this.toInt = toInt;
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

	public String getFromBean2() {
		return fromBean2;
	}

	public void setFromBean2(String fromBean2) {
		this.fromBean2 = fromBean2;
	}

	public String[] getFromBean2s() {
		return fromBean2s;
	}

	public void setFromBean2s(String[] fromBean2s) {
		this.fromBean2s = fromBean2s;
	}
	
}