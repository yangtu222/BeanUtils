package com.tuyang.beanutils.internal.convertors;

import com.tuyang.beanutils.BeanCopyConvertor;

public class ObjectToStringConvertor implements BeanCopyConvertor<Object, String> {
	
	@Override
	public String convertTo(Object object) {
		if( object == null)
			return null;
		return object.toString();
	}

}
