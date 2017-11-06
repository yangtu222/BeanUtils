package com.tuyang.beanutils.internal.convertors;

import com.tuyang.beanutils.BeanCopyConvertor;

@SuppressWarnings("rawtypes")
public class EnumToEnumArrayConvertor implements BeanCopyConvertor<Enum[], Enum[]> {

	@Override
	public Enum[] convertTo(Enum[] object) {
		if( object == null )
			return null;
		return object.clone();
	}

}
