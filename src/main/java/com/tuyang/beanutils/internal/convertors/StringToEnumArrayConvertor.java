package com.tuyang.beanutils.internal.convertors;

import java.lang.reflect.Array;

import com.tuyang.beanutils.BeanCopyConvertor;

@SuppressWarnings("rawtypes")
public class StringToEnumArrayConvertor implements BeanCopyConvertor<String[], Object[]> {
	
	private Class enumClass;
	private boolean throwExceptions;
	
	public StringToEnumArrayConvertor(Class enumClass, boolean throwExceptions) {
		this.enumClass = enumClass;
		this.throwExceptions = throwExceptions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] convertTo(String[] object) {
		if( object == null )
			return null;
		
		Object[] retList = (Object[]) Array.newInstance(enumClass, object.length);
		if( throwExceptions ) {
			for( int i =0; i< object.length;i++ ) {
				retList[i] = Enum.valueOf(enumClass, object[i]);
			}
			return retList;
		}
		Enum[] enums = (Enum[])enumClass.getEnumConstants();
		for( int i =0; i< object.length;i++ ) {
			for( Enum enumKey : enums ) {
				if( enumKey.name().equals(object[i])) {
					retList[i] = enumKey;
				}
			}
		}
		return retList;
	}

}
