package com.tuyang.test.testInheritance;

import com.tuyang.beanutils.annotation.CopyProperty;
import com.tuyang.beanutils.annotation.BeanCopySource;

@BeanCopySource(source=FromBean.class)
public class ToBean extends ToBeanBase {

	private boolean beanBool;
	@CopyProperty(property="beanInt")
	private int toBeanInt;
	@CopyProperty(property="beanString")
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