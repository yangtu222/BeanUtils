package com.tuyang.test.testEnum2;

import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyProperty;

@BeanCopySource(source=FromBean.class)
public class ToBean {

	@CopyProperty(property = "inside.b")
	private MyEnum myEnum;

	public MyEnum getMyEnum() {
		return myEnum;
	}

	public void setMyEnum(MyEnum myEnum) {
		this.myEnum = myEnum;
	}
	
}