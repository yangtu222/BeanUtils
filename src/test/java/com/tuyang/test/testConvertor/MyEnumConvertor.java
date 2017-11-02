package com.tuyang.test.testConvertor;

import com.tuyang.beanutils.BeanCopyConvertor;

public class MyEnumConvertor implements BeanCopyConvertor<MyEnum, String> {

	@Override
	public String convertTo(MyEnum object) {
		if( object == MyEnum.One) 
			return "One";
		if( object == MyEnum.Two) 
			return "Two";
		if( object == MyEnum.Three) 
			return "Three";
		return "";
	}
}
