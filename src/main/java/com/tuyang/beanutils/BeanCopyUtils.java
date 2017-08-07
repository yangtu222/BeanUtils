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

package com.tuyang.beanutils;

import java.util.Collection;
import java.util.List;

import com.tuyang.beanutils.internal.cache.BeanCopyCache;
import com.tuyang.beanutils.internal.logger.Logger;
import com.tuyang.beanutils.internal.utils.Utils;

public class BeanCopyUtils {
	
	private static Logger logger = Logger.getLogger(BeanCopyUtils.class);
	
	public static BeanCopier getBeanCopier( Class<?> sourceClass, Class<?> targetClass ) {
		return BeanCopyCache.getBeanCopy(sourceClass, targetClass, null);
	}
	
	public static BeanCopier getBeanCopier( Class<?> sourceClass, Class<?> targetClass, Class<?> optionClass ) {
		return BeanCopyCache.getBeanCopy(sourceClass, targetClass, optionClass);
	}

	public static <T> T copyBean( Object sourceObject, Class<T> targetClass ) {
		return copyBean(sourceObject, targetClass, null);
	}
	
	public static Object copyBean( Object sourceObject, Object targetObject ) {
		return copyBean(sourceObject, targetObject, null);
	}
	
	public static <R, T> List<R> copyList(List<T> aList, Class<R> classType ) {
		return (List<R>) copyCollection(aList, classType, null);
	}

	public static <R, T> List<R> copyList(List<T> aList, Class<R> classType, Class<?> optionClass ) {
		return (List<R>) copyCollection(aList, classType, optionClass);
	}
	
	public static <R, T> Collection<R> copyCollection(Collection<T> aList, Class<R> classType ) {
		return copyCollection(aList, classType, null);
	}

	public static <T> T copyBean( Object sourceObject, Class<T> targetClass, Class<?> optionClass ) {
		
		if( targetClass == null ) {
			throw new NullPointerException("targetClass must not be null");
		}
		
		T targetObject = (T) Utils.newInstance(targetClass);
		return copyBean(sourceObject, targetObject, optionClass);
	}
	
	@SuppressWarnings({ "unchecked" })
	public static <T> T copyBean( Object sourceObject, T targetObject , Class<?> optionClass ) {
		
		if( sourceObject == null ) {
			throw new NullPointerException("sourceObject must not be null");
		}
		if( targetObject == null ) {
			throw new NullPointerException("targetObject must not be null");
		}
		
		BeanCopier beanCopy = BeanCopyCache.getBeanCopy(sourceObject.getClass(), targetObject.getClass(), optionClass);
		targetObject = (T) beanCopy.copyBean(sourceObject, targetObject);
	
		return targetObject;
	}
	
	@SuppressWarnings("unchecked")
	public static <R, T> Collection<R> copyCollection(Collection<T> aList, Class<R> classType, Class<?> optionClass ) {
		
		Collection<R> dataList = (Collection<R>) Utils.createCollection(aList.getClass());
		for( T t : aList ) {
			try {
				R newInst = (R) copyBean(t, classType, optionClass);
				dataList.add(newInst);
			} catch ( SecurityException | IllegalArgumentException e) {
				logger.error("copyCollection",e);
			}
		}
		return dataList;
	}
}
