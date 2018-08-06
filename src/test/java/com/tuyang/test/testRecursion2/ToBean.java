package com.tuyang.test.testRecursion2;

import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyProperty;

@BeanCopySource(source=FromBean.class)
public class ToBean {
	
	private String name;
	
	@CopyProperty(optionClass=ToBean.class)
	private ToBean parent;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ToBean getParent() {
		return parent;
	}
	public void setParent(ToBean parent) {
		this.parent = parent;
	}
}
