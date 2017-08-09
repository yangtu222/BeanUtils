package com.tuyang.test.testConvertor;

import com.tuyang.beanutils.BeanCopyConvertor;

public class GendorConvertor implements BeanCopyConvertor<Integer, String> {

	@Override
	public String convertTo(Integer object) {
		if( object == 1 ) 
			return "Male";
		if( object == 2) 
			return "Female";
		return "Unknown";
	}
}
