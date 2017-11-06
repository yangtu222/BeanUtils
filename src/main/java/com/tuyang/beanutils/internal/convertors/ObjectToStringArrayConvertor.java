package com.tuyang.beanutils.internal.convertors;

import java.lang.reflect.Array;
import java.util.Collection;

import com.tuyang.beanutils.BeanCopyConvertor;

public class ObjectToStringArrayConvertor implements BeanCopyConvertor<Object, String[]> {
	
	@SuppressWarnings("rawtypes")
	@Override
	public String[] convertTo(Object object) {
		if( object == null)
			return null;
		if( object.getClass().isArray() ) {
			int count = Array.getLength(object);
			String[] retList = new String[count];
			for( int i =0; i< count; i++ ) {
				retList[i] = Array.get(object, i) != null ? Array.get(object, i).toString(): null;
			}
			return retList;
		}
		else if( object instanceof Collection ) {
			Collection collection = (Collection) object;
			String[] retList = new String[collection.size()];
			int i =0;
			for(Object object2 : collection) {
				retList[i++] = object2!=null?object2.toString():null;
			}
			return retList;
		}
		return null;
	}

}
