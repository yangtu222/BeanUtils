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
import com.tuyang.beanutils.annotation.CopyFeature;
import com.tuyang.beanutils.exception.BeanCopyException;
import com.tuyang.beanutils.internal.cache.BeanCopyPropertyItem;
import com.tuyang.beanutils.internal.logger.Logger;
import com.tuyang.beanutils.internal.utils.InstanceUtils;

@SuppressWarnings("rawtypes")
public class ReflectBeanCopy implements BeanCopier {
	
	private static Logger logger = Logger.getLogger(BeanCopyUtils.class);

	private List<BeanCopyPropertyItem> items ;
	private CopyFeature[] features;
	
	public ReflectBeanCopy(List<BeanCopyPropertyItem> items, CopyFeature[] features) {
		this.items = items;
		this.features = features;
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
					
					if( item.useBeanCopy ) {
						Object writeObject = null;
						if (item.collectionClass != null )
							writeObject = InstanceUtils.unsafeCopyCollection(targetValue, item.collectionClass, item.optionClass, targetPropertyType, features);
						else
							writeObject = InstanceUtils.unsafeCopyArray(targetValue, targetPropertyType.getComponentType(), item.optionClass, features);
						item.writeMethod.invoke(targetObject, writeObject);
					} else {
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
					}
					
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
					}
					else if( item.convertorClass != null ) {
						BeanCopyConvertor convertor = (BeanCopyConvertor) InstanceUtils.newInstance(item.convertorClass);
						sourceData = convertor.convertTo(sourceData);
						
					} else if( item.optionClass != null ) {
						if( sourceData != null ) {
							sourceData = BeanCopyUtils.copyBean(sourceData, targetPropertyType, item.optionClass);
						}
					} else if( item.useBeanCopy ) {
						sourceData = BeanCopyUtils.copyBean(sourceData, targetPropertyType);
					}
					boolean ignoreInvoke = false;
					if( targetPropertyType.isPrimitive() && sourceData == null ) {
						ignoreInvoke = findFeature( CopyFeature.IGNORE_PRIMITIVE_NULL_SOURCE_VALUE );
					}
					if( !ignoreInvoke ) {
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

	private boolean findFeature(CopyFeature feature ) {
		if( features == null || features.length == 0 )
			return false;
		for( CopyFeature f : features ) {
			if( f == feature )
				return true;
		}
		return false;
	}
	
}
