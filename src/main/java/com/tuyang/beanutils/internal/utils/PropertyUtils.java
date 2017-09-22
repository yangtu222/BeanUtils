 /*
  BeanUtils Version 1.0.0

  Created by yangtu222 on 2017.08.05

  Distributed under the permissive zlib License
  Get the latest version from here:

  https://github.com/yangtu222/BeanUtils

  This software is provided 'as-is', without any express or implied
  warranty.  In no event will the authors be held liable for any damages
  arising from the use of this software.

  Permission is granted to anyone to use this software for any purpose,
  including commercial applications, and to alter it and redistribute it
  freely, subject to the following restrictions:

  1. The origin of this software must not be misrepresented; you must not
  claim that you wrote the original software. If you use this software
  in a product, an acknowledgment in the product documentation would be
  appreciated but is not required.

  2. Altered source versions must be plainly marked as such, and must not be
  misrepresented as being the original software.

  3. This notice may not be removed or altered from any source distribution.
*/

package com.tuyang.beanutils.internal.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class PropertyUtils {
	
	private static Map<Class<?>, WeakReference<PropertyDescriptor[]>> cacheMap = new HashMap<>();
	
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName) {

		PropertyDescriptor[] allPds = null;
		synchronized (cacheMap) {
			WeakReference<PropertyDescriptor[]> pdsRef = cacheMap.get(clazz);
			if( pdsRef != null ) {
				allPds = pdsRef.get();
			}
		}
		if( allPds == null ) {
			allPds = getPropertyDescriptors(clazz);
			
		}
		for( PropertyDescriptor pd : allPds ) {
			if( propertyName.equals(pd.getName() ) ) {
				return pd;
			}
		}
		return null;
	}
	
	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
		
		PropertyDescriptor[] allPds = null;
		synchronized (cacheMap) {
			WeakReference<PropertyDescriptor[]> pdsRef = cacheMap.get(clazz);
			if( pdsRef != null ) {
				allPds = pdsRef.get();
				if( allPds != null )
					return allPds;
			}
		}

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			allPds = beanInfo.getPropertyDescriptors();
			synchronized (cacheMap) {
				cacheMap.put(clazz, new WeakReference<PropertyDescriptor[]>(allPds));
			}
			return allPds;
		} catch (IntrospectionException e) {
			return new PropertyDescriptor[0];
		}
	}
	
	public static Field getClassField(Class<?> targetClass, Class<?> optionClass, String propertyName) {
		
		Field propertyField = null;
		
		if( optionClass != null ) {
			try {
				propertyField = optionClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException | SecurityException e) {
			}
		}
		if( propertyField == null ) {
			
			Class<?> tryClass = targetClass;
			while( tryClass != null ) {
				try {
					propertyField = tryClass.getDeclaredField(propertyName);
					return propertyField;
				} catch (NoSuchFieldException | SecurityException e) {
					tryClass = tryClass.getSuperclass();
				}
			}
		}
		return propertyField;
	}

	public static boolean isInterfaceType(Class<?> classType, Class<?> interfaceType) {
		
		if( classType.equals(interfaceType) ) {
			return true;
		}
		
		while( classType != null ) {
			Class<?>[] interfaces = classType.getInterfaces();
			
			for( Class<?> interClass : interfaces ) {
				if( interClass.equals(interfaceType) ) {
					return true;
				}
			}
			classType = classType.getSuperclass();
		}
		
		return false;
	}
	
	private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<Class<?>, Class<?>>(8);

	static {
		primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
		primitiveWrapperTypeMap.put(Byte.class, byte.class);
		primitiveWrapperTypeMap.put(Character.class, char.class);
		primitiveWrapperTypeMap.put(Double.class, double.class);
		primitiveWrapperTypeMap.put(Float.class, float.class);
		primitiveWrapperTypeMap.put(Integer.class, int.class);
		primitiveWrapperTypeMap.put(Long.class, long.class);
		primitiveWrapperTypeMap.put(Short.class, short.class);
	}
	
	public static boolean isPrimitive(Class<?> classType) {
		if( classType.isPrimitive() )
			return true;
		
		for( Class<?> resolvedWrapper : primitiveWrapperTypeMap.keySet() ) {
			if( classType.equals(resolvedWrapper) ) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
		if (lhsType.isAssignableFrom(rhsType)) {
			return true;
		}
		if (lhsType.isPrimitive()) {
			Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);
			if (lhsType == resolvedPrimitive) {
				return true;
			}
		}
		else if( rhsType.isPrimitive() ) {
			Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(lhsType);
			if (rhsType == resolvedPrimitive) {
				return true;
			}
		}
		return false;
	}

}
