package com.tuyang.beanutils.internal.convertors;

import com.tuyang.beanutils.BeanCopyConvertor;

@SuppressWarnings("rawtypes")
public class EnumToStringConvertor implements BeanCopyConvertor<Enum, String> {

	@Override
	public String convertTo(Enum object) {
		return object.toString();
	}

}
