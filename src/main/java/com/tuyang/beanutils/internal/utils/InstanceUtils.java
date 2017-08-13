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

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.tuyang.beanutils.config.BeanCopyConfig;
import com.tuyang.beanutils.exception.BeanCopyException;

public class InstanceUtils {
	
	@SuppressWarnings("rawtypes")
	public static Collection newCollection(Class<?> clazz) {
		
		BeanCopyConfig config = BeanCopyConfig.instance();
		
		if( clazz.isInterface() ) {
			if( clazz.equals(List.class) ) {
				return (Collection) newInstance(config.getListClass());
			}
			if( clazz.equals(Set.class) ) {
				return (Collection) newInstance(config.getSetClass() );
			}
			if( clazz.equals(Deque.class) ) {
				return (Collection) newInstance(config.getDequeClass() );
			}
			if( clazz.equals(Queue.class) ) {
				return (Collection) newInstance(config.getQueueClass() );
			}
			
			Class<?>[] interfaces = clazz.getInterfaces();
			
			for( Class<?> interfaceClass : interfaces ) {
				
				if( interfaceClass.equals(List.class) ) {
					return (Collection) newInstance(config.getListClass());
				}
				if( interfaceClass.equals(Set.class) ) {
					return (Collection) newInstance(config.getSetClass() );
				}
				if( clazz.equals(Deque.class) ) {
					return (Collection) newInstance(config.getDequeClass() );
				}
				if( clazz.equals(Queue.class) ) {
					return (Collection) newInstance(config.getQueueClass() );
				}
			}
		}
		else
		{
			Collection<?> retCollection = (Collection<?>) newInstance(clazz);
			return retCollection;
		}
		
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T newInstance( Class<T> targetClass ) {
		
		T targetObject = null;
		
		try {
			targetObject = targetClass.newInstance();
		} catch (Exception e) {
		}
		if( targetObject == null ) {
			try {
				Constructor con = targetClass.getDeclaredConstructor();
				con.setAccessible(true);
				targetObject = (T) con.newInstance();
				
			}catch (Exception e) {
				throw new BeanCopyException( "beanCopy new instance", e);
			}
		}
		return targetObject;
	}
}
