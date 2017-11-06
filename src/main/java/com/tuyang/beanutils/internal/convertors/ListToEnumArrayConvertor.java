package com.tuyang.beanutils.internal.convertors;

import java.lang.reflect.Array;
import java.util.Collection;

import com.tuyang.beanutils.BeanCopyConvertor;

@SuppressWarnings("rawtypes")
public class ListToEnumArrayConvertor implements BeanCopyConvertor<Collection<Object>, Object[]> {
	
	private Class enumClass;
	private boolean throwExceptions;
	
	public ListToEnumArrayConvertor(Class enumClass, boolean throwExceptions) {
		this.enumClass = enumClass;
		this.throwExceptions = throwExceptions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object[] convertTo(Collection<Object> objects) {
		if( objects == null )
			return null;
		
		Object[] retList = (Object[]) Array.newInstance(enumClass, objects.size());
		if( throwExceptions ) {
			int i =0;
			for(Object object : objects ) {
				if( object!=null ) {
					String str = object.toString();
					retList[i] =  Enum.valueOf(enumClass, str);
				}
				i++;
			}
			return retList;
		}
		Enum[] enums = (Enum[])enumClass.getEnumConstants();
		int i =0;
		for(Object object : objects ) {
			if( object!=null ) {
				String str = object.toString();
				for( Enum enumKey : enums ) {
					if( enumKey.name().equals(str)) {
						retList[i] = enumKey;
					}
				}
			}
			i++;
		}
		return retList;
	}

}
