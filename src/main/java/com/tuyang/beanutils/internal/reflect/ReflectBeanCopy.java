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

package com.tuyang.beanutils.internal.reflect;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import com.tuyang.beanutils.BeanCopier;
import com.tuyang.beanutils.BeanCopyConvertor;
import com.tuyang.beanutils.BeanCopyUtils;
import com.tuyang.beanutils.exception.BeanCopyException;
import com.tuyang.beanutils.internal.cache.BeanCopyPropertyItem;
import com.tuyang.beanutils.internal.logger.Logger;
import com.tuyang.beanutils.internal.utils.InstanceUtils;

@SuppressWarnings("rawtypes")
public class ReflectBeanCopy implements BeanCopier {
	
	private static Logger logger = Logger.getLogger(BeanCopyUtils.class);

	private List<BeanCopyPropertyItem> items ;
	
	public ReflectBeanCopy(List<BeanCopyPropertyItem> items) {
		this.items = items;
	}

	@SuppressWarnings( "unchecked")
	public Object copyBean(Object sourceObject, Object targetObject) {
		
		for( BeanCopyPropertyItem item: items ) {
			
			Class<?> targetPropertyType = item.writeMethod.getParameterTypes()[0];
			
			if( item.isCollection ) {
				
				try {
					Object targetValue = sourceObject;
					for( int i =0; i< item.readMethods.length; i++ ) {
						targetValue = item.readMethods[i].invoke(targetValue);
					}
					
					Collection collectionRead = (Collection) targetValue;
					Collection collectionWrite = null;
					
					if(collectionRead!= null ) {
						collectionWrite = InstanceUtils.newCollection(targetPropertyType);
						if( collectionWrite == null ) {
							logger.error("beanCopy: cannot copy collection property " + targetPropertyType.getSimpleName() );
							throw new BeanCopyException("beanCopy: cannot copy collection property " + targetPropertyType.getSimpleName() );
						}
						for( Object sourceObj: collectionRead ) {
							Object targetCollectionObject = BeanCopyUtils.copyBean(sourceObj, item.collectionClass, item.optionClass );
							collectionWrite.add(targetCollectionObject);
						}
					}
					
					item.writeMethod.invoke(targetObject, collectionWrite);
					
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					logger.warn("beanCopy: cannot invoke property copy on " + item.writeMethod.getName(), e );
					throw new BeanCopyException("beanCopy: cannot invoke property copy on " + item.writeMethod.getName(), e  );
				}
				
			} else {
				
				try {
					
					Object targetValue = sourceObject;
					for( int i =0; i< item.readMethods.length && targetValue != null; i++ ) {
						targetValue = item.readMethods[i].invoke(targetValue);
					}
					
					Object sourceData = targetValue;
					
					if( item.convertorObject != null ) {
						sourceData = item.convertorObject.convertTo(sourceData);
						item.writeMethod.invoke(targetObject, sourceData );
					}
					else if( item.convertorClass != null ) {
						BeanCopyConvertor convertor = (BeanCopyConvertor) InstanceUtils.newInstance(item.convertorClass);
						sourceData = convertor.convertTo(sourceData);
						
						item.writeMethod.invoke(targetObject, sourceData );
						
					} else if( item.optionClass != null ) {
						if( sourceData != null ) {
							sourceData = BeanCopyUtils.copyBean(sourceData, targetPropertyType, item.optionClass);
						}
						item.writeMethod.invoke(targetObject, sourceData );
					} else if( item.useBeanCopy ) {
						sourceData = BeanCopyUtils.copyBean(sourceData, targetPropertyType);
						item.writeMethod.invoke(targetObject, sourceData );
					} else {
						item.writeMethod.invoke(targetObject, sourceData );
					}
					
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | ClassCastException e) {
					logger.warn("beanCopy: cannot invoke property copy on " + item.writeMethod.getName(), e  );
					throw new BeanCopyException("beanCopy: cannot invoke property copy on " + item.writeMethod.getName(), e  );
				}		
			}
		}
		return targetObject;
	}
	
}
