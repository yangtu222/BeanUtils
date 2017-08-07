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

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PropertyUtils {
	
	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String propertyName) {
		
		Field field = null;
		try {
			field = clazz.getDeclaredField(propertyName);
			return getPropertyDescriptor(clazz, field);
		}catch (NoSuchFieldException e) {
		}
		
		clazz = clazz.getSuperclass();
		while( clazz != null ) {
			try {
				field = clazz.getDeclaredField(propertyName);
				return getPropertyDescriptor(clazz, field);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}
	
	private static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, Field field) {
		PropertyDescriptor pd = null;
		try {
			Method setMethod = null;
			Method getMethod = null;

			String propertyName = field.getName();
			
			String getMethedName;
			String setMethodName;
			String methodEnd = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
			if( field.getType().equals(boolean.class)) {
				getMethedName = "is" + methodEnd;
				try {
					getMethod = clazz.getDeclaredMethod(getMethedName.toString());
				}catch (Exception e) {
				}
				if( setMethod == null ) {
					try {
						getMethedName = "get" + methodEnd;
						getMethod = clazz.getDeclaredMethod(getMethedName.toString());
					}catch (Exception e) {
					}
				}
			} else {
				try {
					getMethedName = "get" + methodEnd;
					getMethod = clazz.getDeclaredMethod(getMethedName.toString());
				}catch (Exception e) {
				}
			}
			setMethodName = "set" + methodEnd;
			try {
				setMethodName = "set" + methodEnd;
				setMethod = clazz.getDeclaredMethod(setMethodName, new Class[]{ field.getType() });
			}catch (Exception e) {
			}
			pd = new PropertyDescriptor(propertyName, getMethod, setMethod);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
		return pd;
	}
	
	public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<PropertyDescriptor> list = new ArrayList<>();
		for( int i =0; i< fields.length; i++ ) {
			Field field = fields[i];
			PropertyDescriptor pd = getPropertyDescriptor(clazz, field);
			if( pd != null)
				list.add(pd);
		}
		
		clazz = clazz.getSuperclass();
		while( clazz != null ) {
			fields = clazz.getDeclaredFields();
			for( int i =0; i< fields.length; i++ ) {
				Field field = fields[i];
				PropertyDescriptor pd = getPropertyDescriptor(clazz, field);
				if( pd != null)
					list.add(pd);
			}
			clazz = clazz.getSuperclass();
		}
		
		return list.toArray(new PropertyDescriptor[list.size()]);
	}
}
