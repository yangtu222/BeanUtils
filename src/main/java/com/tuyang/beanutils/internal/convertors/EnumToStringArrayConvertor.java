package com.tuyang.beanutils.internal.convertors;

import com.tuyang.beanutils.BeanCopyConvertor;

@SuppressWarnings("rawtypes")
public class EnumToStringArrayConvertor implements BeanCopyConvertor<Enum[], String[]> {

	@Override
	public String[] convertTo(Enum[] object) {
		if( object == null )
			return null;
		String[] retList = new String[object.length];
		int i =0;
		for( Enum enum1 : object ) {
			retList[i++] = enum1.toString();
		}
		return retList;
	}

}
