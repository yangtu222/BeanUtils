package com.tuyang.beanutils.internal.convertors;

import com.tuyang.beanutils.BeanCopyConvertor;

@SuppressWarnings("rawtypes")
public class ObjectToEnumConvertor implements BeanCopyConvertor<Object, Enum> {
	
	private Class enumClass;
	private boolean throwExceptions;
	
	public ObjectToEnumConvertor(Class enumClass, boolean throwExceptions) {
		this.enumClass = enumClass;
		this.throwExceptions = throwExceptions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Enum convertTo(Object object) {
		if( object == null ) 
			return null;
		if( throwExceptions ) {
			return Enum.valueOf(enumClass, object.toString());
		}
		Enum[] enums = (Enum[])enumClass.getEnumConstants();
		for( Enum enumKey : enums ) {
			if( enumKey.name().equals(object.toString())) {
				return enumKey;
			}
		}
		return null;
	}

}
