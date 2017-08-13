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

package com.tuyang.beanutils.internal.cache;

import java.beans.PropertyDescriptor;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tuyang.beanutils.BeanCopier;
import com.tuyang.beanutils.BeanCopyConvertor;
import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyCollection;
import com.tuyang.beanutils.annotation.CopyProperty;
import com.tuyang.beanutils.config.BeanCopyConfig;
import com.tuyang.beanutils.exception.BeanCopyException;
import com.tuyang.beanutils.internal.convertors.ArrayConvertorFactory;
import com.tuyang.beanutils.internal.factory.BeanCopierFactory;
import com.tuyang.beanutils.internal.logger.Logger;
import com.tuyang.beanutils.internal.utils.PropertyUtils;


public class BeanCopyCache {
	
	private static Logger logger = Logger.getLogger(BeanCopyCache.class);

	private static Map<Long, SoftReference<BeanCopier>> beanCopyCacheMap = new ConcurrentHashMap<>();
	private static BeanCopyConfig beanCopyConfig = BeanCopyConfig.instance();
	private static BeanCopierFactory beanCopyFactory = null;
	
	public static void setBeanCopyConfig(BeanCopyConfig beanCopyConfig) {
		if( beanCopyConfig == null )
			beanCopyConfig = new BeanCopyConfig();
		BeanCopyCache.beanCopyConfig = beanCopyConfig;
		if( beanCopyFactory!= null && !beanCopyFactory.getClass().equals(beanCopyConfig.getBeanCopyFactory() ) )
			beanCopyCacheMap.clear();
		beanCopyFactory = null;
	}
	
	public static BeanCopier getBeanCopy(Class<?> sourceClass, Class<?> targetClass, Class<?> optionClass) {
		
		long cacheKey = (((long)sourceClass.hashCode() ) << 16 )+ (long) targetClass.hashCode();
		if( optionClass != null ) {
			cacheKey = (cacheKey <<16)  + (long)optionClass.hashCode();
		} else {
			cacheKey = (cacheKey <<16)  + (long)targetClass.hashCode();
		}
		BeanCopier beanCopy = null;
		SoftReference<BeanCopier> refBeanCopy = beanCopyCacheMap.get(cacheKey);
		if( refBeanCopy != null )
			beanCopy = refBeanCopy.get();
		
		if( beanCopy != null )
			return beanCopy;
		
		synchronized (BeanCopyCache.class) {
			if( beanCopyFactory == null ) {
				try {
					beanCopyFactory = beanCopyConfig.getBeanCopyFactory().newInstance();
				} catch (Exception e) {
					throw new BeanCopyException("BeanCopyConfig is not configured correctly!");
				}
			}
		}
		
		List<BeanCopyPropertyItem> itemList = buildBeanCopyPropertyItem(sourceClass, targetClass, optionClass);
		beanCopy = beanCopyFactory.createBeanCopier(sourceClass, targetClass, itemList);
		if( beanCopy != null ) {
			beanCopyCacheMap.put(cacheKey, new SoftReference<BeanCopier>(beanCopy));
		}
		return beanCopy;
	}

	@SuppressWarnings("rawtypes")
	private static List<BeanCopyPropertyItem> buildBeanCopyPropertyItem(Class<?> sourceClass, Class<?> targetClass, Class<?> optionClass ) {
		
		List<BeanCopyPropertyItem> itemList = new ArrayList<>();
		
		Class<?> beanAnnotationSource = null;
		if( optionClass != null ) {
			if( optionClass.isAnnotationPresent(BeanCopySource.class) ) {
				BeanCopySource source = optionClass.getAnnotation(BeanCopySource.class);
				Class<?> sourceClassFromAnnotation = source.source();
				if( sourceClassFromAnnotation.equals(sourceClass) ) {
					beanAnnotationSource = sourceClassFromAnnotation;
				} else {
					//fix sourceClass is proxy class.
					if( sourceClass.getName().startsWith(sourceClassFromAnnotation.getName()) ) {
						beanAnnotationSource = sourceClassFromAnnotation;
					}
				}
			}
		}
		if( beanAnnotationSource == null && targetClass.isAnnotationPresent(BeanCopySource.class) ) {
			BeanCopySource source = targetClass.getAnnotation(BeanCopySource.class);
			Class<?> sourceClassFromAnnotation = source.source();
			if( sourceClassFromAnnotation.equals(sourceClass) ) {
				beanAnnotationSource = sourceClassFromAnnotation;
			}
		}

		PropertyDescriptor[] targetPds = PropertyUtils.getPropertyDescriptors(targetClass);
		
		for( PropertyDescriptor  targetPd: targetPds ) {
			
			Method writeMethod = null;
			writeMethod = targetPd.getWriteMethod();
			if( writeMethod == null )
				continue;

			String propertyName = null;
			Field propertyField = null;
			Class<?> methodTargetType = null;
			Class<?> methodTargetArray = null;
			boolean targetIsArray = false;
			
			Method[] readMethods = null;
			Class<?> methodSourceType = null;
			Class<?> methodSourceArray = null;
			boolean sourceIsArray = false;
			
			Class<?> convertorClass = null;
			Object convertorObject = null;
			
			boolean isCollection = false;
			Class<?> propertyOptionClass = null;
			Class<?> collectionClass = null;
			
			propertyName = targetPd.getName();
			propertyField = null;
			methodTargetType = writeMethod.getParameterTypes()[0];
			methodTargetArray = methodTargetType;
			targetIsArray = methodTargetType.isArray();
			
			if( targetIsArray ) {
				methodTargetType = methodTargetArray.getComponentType();
			}
			
			propertyField = getClassField(targetClass, optionClass, propertyName);
			
			if( propertyField!= null && propertyField.isAnnotationPresent(CopyProperty.class)) {
				
				CopyProperty copyAnnotation = propertyField.getAnnotation(CopyProperty.class);
				String annotationPropertyName = copyAnnotation.property();
				
				if( copyAnnotation.ignored() ) {
					continue;
				}
				
				if( !( annotationPropertyName == null || "".equals(annotationPropertyName) ) ) {
					if( beanAnnotationSource == null ) {
						logger.warn("BeanCopy: " + targetClass.getName() + " has no BeanPropertySource annotation, but " 
								+ propertyName + " has BeanProperty annotation with property defined");
						throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " has no BeanPropertySource annotation, but " 
								+ propertyName + " has BeanProperty annotation with property defined"  );
						
					} else {
						propertyName = annotationPropertyName;
					}
				}
				String[] propertyNames = null;
				if( propertyName.contains(".") ) {
					propertyNames = propertyName.split("\\.");
				} else {
					propertyNames = new String[1];
					propertyNames[0] = propertyName;
				}
				readMethods = new Method[propertyNames.length];
				methodSourceType = sourceClass;
				
				for( int i = 0; i< propertyNames.length ; i++ ) {
					PropertyDescriptor sourcePd = PropertyUtils.getPropertyDescriptor(methodSourceType, propertyNames[i] );
					if( sourcePd == null ) {
						logger.error("BeanCopy: " + methodSourceType.getName() + " has no property "+ propertyNames[i] +" defined.!" );
						throw new BeanCopyException("BeanCopy: " + methodSourceType.getName() + " has no property "+ propertyNames[i] +" defined.!" );
					} else {
						readMethods[i] = sourcePd.getReadMethod();
					}
					methodSourceType = readMethods[i].getReturnType();
				}
				
				methodSourceArray = methodSourceType;
				sourceIsArray = methodSourceArray.isArray();
				if( sourceIsArray ) {
					methodSourceType = methodSourceArray.getComponentType();
				}

				convertorClass = copyAnnotation.convertor();
				convertorObject = null;
				
				if( convertorClass.equals(void.class) ) {
					convertorClass = null;
				}
				else if( !isInterfaceType(convertorClass, BeanCopyConvertor.class ) ) {
					convertorClass = null;
					logger.error("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Annotation BeanProperty convertor property is not a convertor class!!");
					throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Annotation BeanProperty convertor property is not a convertor class!!");
				} 
				else if( convertorClass.isInterface() ) {
					logger.error("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Annotation BeanProperty convertor is a interface!!");
					throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Annotation BeanProperty convertor is a interface!!");
				}
				else {
					try {
						Type[] genericInterfaces =  convertorClass.getGenericInterfaces();
						
						ParameterizedType convertorInterface = null;
						for( Type type : genericInterfaces ) {
							ParameterizedType parameterizedType = (ParameterizedType) type;
							if( parameterizedType.getRawType().equals(BeanCopyConvertor.class) ) {
								convertorInterface = parameterizedType;
							}
						}
						
						Class<?> converterClassSource =  (Class<?>) convertorInterface.getActualTypeArguments()[0];
						Class<?> converterClassTarget =  (Class<?>) convertorInterface.getActualTypeArguments()[1];
						
						if( !( isAssignable(methodSourceType, converterClassSource) && isAssignable(methodTargetType, converterClassTarget ) ) ) {
							logger.error("BeanCopy: " + targetClass.getName() + " property " + propertyName
									+ " Annotation BeanProperty convertor does match the type!!");
							throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
									+ " Annotation BeanProperty convertor does match the type!!");
						}
					} catch (Exception e) {
						logger.error("BeanCopy: " + targetClass.getName() + " property " + propertyName
								+ " Annotation BeanProperty convertor: get generic type error!!", e);
						throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
								+ " Annotation BeanProperty convertor: get generic type error!!", e);
					}
				}
				
				propertyOptionClass = copyAnnotation.optionClass();
				if( propertyOptionClass.equals(void.class) || propertyOptionClass.equals(Void.class) ) {
					propertyOptionClass = null;
				}
				
				if( convertorClass != null && propertyOptionClass != null ) {
					logger.error("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Annotation BeanProperty convertor and optionClass cannot be set both!!");
					throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Annotation BeanProperty convertor and optionClass cannot be set both!!"); 
				}
				
				if(((targetIsArray && !sourceIsArray) || (!targetIsArray && sourceIsArray)) && convertorClass == null ) {
					logger.error("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Array type mismatch!!");
					throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Array type mismatch!!");
				}
				
				if( ( isPrimitive(methodSourceType) || isPrimitive(methodTargetType) ) && propertyOptionClass != null ) {
					logger.error("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Annotation BeanProperty optionClass cannot be set on primitive tpye!!");
					throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Annotation BeanProperty convertor and optionClass on primitive tpye!!"); 
				}
				
				if( convertorClass != null ) {
					
					BeanCopyPropertyItem item = new BeanCopyPropertyItem();
					
					item.writeMethod = writeMethod;
					item.readMethods = readMethods;
					item.isCollection = isCollection;
					item.useBeanCopy = false;
					item.optionClass = null;
					item.convertorClass = convertorClass;
					item.convertorObject = (BeanCopyConvertor) convertorObject;
					
					itemList.add(item);
					
					logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
							" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
				}
				
				else if( !targetIsArray ) {
					
					if( isAssignable(methodTargetType, methodSourceType) ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.optionClass = null;
						item.convertorClass = null;
						item.useBeanCopy = false;
						
						itemList.add(item);
						
						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
					} 
					else if( propertyOptionClass != null ) {
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						item.optionClass = propertyOptionClass;
						
						itemList.add(item);
						
						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
					}
					else if( methodTargetType.isAnnotationPresent(BeanCopySource.class) ) {
						
						BeanCopySource source = methodTargetType.getAnnotation(BeanCopySource.class);
						Class<?> sourceClassFromAnnotation = source.source();
						if( sourceClassFromAnnotation.equals(methodSourceType ) ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.useBeanCopy = false;
							item.optionClass = methodTargetType;
							
							itemList.add(item);
							
							logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
									" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
						} 
						else {
							logger.warn( "Property parameter does not match: " + 
									sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
									targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
							throw new BeanCopyException("Property parameter does not match: " + 
									sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
									targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
						}
					}
					else if( !(isPrimitive(methodSourceType)|| isPrimitive(methodTargetType) ) ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.optionClass = null;
						item.convertorClass = null;
						item.useBeanCopy = true;
						
						itemList.add(item);
						
						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
					}
					else {
						logger.warn( "Property parameter does not match: " + 
								sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
								targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
						throw new BeanCopyException("Property parameter does not match: " + 
								sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
								targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
					}
					
				}
				
				else if( targetIsArray ) {
					
					if( methodTargetArray.equals(methodSourceArray) ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						item.optionClass = propertyOptionClass;
						item.convertorClass = null;
						item.convertorObject = null;
						
						itemList.add(item);
						
						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
						
					}
					else if( isAssignable(methodTargetType, methodSourceType) ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						item.optionClass = null;
						item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, null);
						item.convertorClass = item.convertorObject.getClass();
						
						itemList.add(item);
						
						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
					
					}
					else if( propertyOptionClass != null ) { 
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, propertyOptionClass);
						item.convertorClass = item.convertorObject.getClass();
						item.optionClass = null;
						
						itemList.add(item);
						
						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
					}
					else if( methodTargetType.isAnnotationPresent(BeanCopySource.class) ) {
						
						BeanCopySource source = methodTargetType.getAnnotation(BeanCopySource.class);
						Class<?> sourceClassFromAnnotation = source.source();
						if( sourceClassFromAnnotation.equals(methodSourceType ) ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.useBeanCopy = false;
							item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, methodTargetType);
							item.convertorClass = item.convertorObject.getClass();
							item.optionClass = null;
							
							itemList.add(item);
							
							logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
									" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
						}
						else {
							logger.warn( "Property parameter does not match: " + 
									sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
									targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
							throw new BeanCopyException("Property parameter does not match: " + 
									sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
									targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
						}
					}
					else if( !(isPrimitive(methodSourceType) || isPrimitive(methodTargetType) ) ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.optionClass = null;
						item.convertorClass = null;
						item.useBeanCopy = false;
						item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, null);
						item.convertorClass = item.convertorObject.getClass();
						item.optionClass = null;
						
						itemList.add(item);
						
						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
					}
					else {
						logger.warn( "Property parameter does not match: " + 
								sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
								targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
						throw new BeanCopyException("Property parameter does not match: " + 
								sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
								targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
					}
				}
				
			} else if( propertyField!= null && propertyField.isAnnotationPresent(CopyCollection.class)){
				
				CopyCollection copyAnnotation = propertyField.getAnnotation(CopyCollection.class);
				String annotationPropertyName = copyAnnotation.property();
				
				if( copyAnnotation.ignored() ) {
					continue;
				}
				
				if( !( annotationPropertyName == null || "".equals(annotationPropertyName) ) ) {
					if( beanAnnotationSource == null ) {
						logger.warn("BeanCopy: " + targetClass.getName() + " has no BeanPropertySource annotation, but " 
								+ propertyName + " has BeanProperty annotation with property defined");
						throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " has no BeanPropertySource annotation, but " 
								+ propertyName + " has BeanProperty annotation with property defined"  );
						
					} else {
						propertyName = annotationPropertyName;
					}
				}
				String[] propertyNames = null;
				if( propertyName.contains(".") ) {
					propertyNames = propertyName.split("\\.");
				} else {
					propertyNames = new String[1];
					propertyNames[0] = propertyName;
				}
				readMethods = new Method[propertyNames.length];
				methodSourceType = sourceClass;

				for( int i = 0; i< propertyNames.length ; i++ ) {
					PropertyDescriptor sourcePd = PropertyUtils.getPropertyDescriptor(methodSourceType, propertyNames[i] );
					if( sourcePd == null ) {
						logger.error("BeanCopy: " + methodSourceType.getName() + " has no property "+ propertyNames[i] +" defined.!" );
						throw new BeanCopyException("BeanCopy: " + methodSourceType.getName() + " has no property "+ propertyNames[i] +" defined.!" );
					} else {
						readMethods[i] = sourcePd.getReadMethod();
					}
					methodSourceType = readMethods[i].getReturnType();
				}
				
				methodSourceArray = methodSourceType;
				sourceIsArray = methodSourceArray.isArray();
				if( sourceIsArray ) {
					methodSourceType = methodSourceArray.getComponentType();
				}

				propertyOptionClass = copyAnnotation.optionClass();
				if( propertyOptionClass.equals(void.class) || propertyOptionClass.equals(Void.class) ) {
					propertyOptionClass = null;
				}
				
				if( !isInterfaceType(methodSourceArray, Collection.class) ) {
					logger.warn("BeanCopy: " + sourceClass.getName() + " property " + propertyName
							+ " is not collection type!");
					throw new BeanCopyException("BeanCopy: " + sourceClass.getName() + " property " + propertyName
						+ " is not collection type!");
				}
				
				if( !isInterfaceType(methodTargetType, Collection.class) ) {
					logger.warn("BeanCopy: " + targetClass.getName() + " property " + propertyName
						+ " is not collection type!");
					throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
						+ " is not collection type!");
				}
				
				isCollection = true;
				collectionClass = copyAnnotation.targetClass();
				
				BeanCopyPropertyItem item = new BeanCopyPropertyItem();
				
				item.writeMethod = writeMethod;
				item.readMethods = readMethods;
				item.isCollection = true;
				item.useBeanCopy = false;
				item.collectionClass = collectionClass;
				item.optionClass = propertyOptionClass;
				
				itemList.add(item);
				
				logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
						" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
				
			} else {
				//normal property.
				
				PropertyDescriptor sourcePd = PropertyUtils.getPropertyDescriptor(sourceClass, propertyName );
				if( sourcePd == null ) {
					continue;
				}
				Method readMethod = sourcePd.getReadMethod();
				if (readMethod == null ) {
					continue;
				}
				
				methodSourceType = readMethod.getReturnType();
				methodSourceArray = methodSourceType;
				
				sourceIsArray = methodSourceArray.isArray();
				if( sourceIsArray ) {
					methodSourceType = methodSourceArray.getComponentType();
				}
				
				readMethods = new Method[1];
				readMethods[0] = readMethod;
				
				if( (targetIsArray && !sourceIsArray) || (!targetIsArray && sourceIsArray) ) {
					logger.error("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Array type mismatch!!");
					throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Array type mismatch!!");
				}
				
				if( !targetIsArray ) {
					if( isAssignable(methodTargetType, methodSourceType) ) {
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						
						itemList.add(item);
						
						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
					} 
					else if(methodTargetType.isAnnotationPresent(BeanCopySource.class) ) {
						
						BeanCopySource source = methodTargetType.getAnnotation(BeanCopySource.class);
						Class<?> sourceClassFromAnnotation = source.source();
						if( sourceClassFromAnnotation.equals(methodSourceType ) ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.useBeanCopy = false;
							item.optionClass = methodTargetType;
							
							itemList.add(item);
							
							logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
									" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
						
						}
						else {
							logger.warn( "Property parameter does not match: " + 
									sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
									targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
							throw new BeanCopyException("Property parameter does not match: " + 
									sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
									targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
						}
					} else {
						logger.warn( "Property parameter does not match: " + 
								sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
								targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
						throw new BeanCopyException("Property parameter does not match: " + 
								sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
								targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
					}
				}
				
				if( targetIsArray ) {
					
					if( methodSourceArray.equals(methodTargetArray) ) {
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						
						itemList.add(item);
						
						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
					}
					else if( isAssignable(methodTargetType, methodSourceType) ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, null);
						item.convertorClass = item.convertorObject.getClass();
						
						itemList.add(item);
						
						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
						
					} else if( methodTargetType.isAnnotationPresent(BeanCopySource.class) ) {
						
						BeanCopySource source = methodTargetType.getAnnotation(BeanCopySource.class);
						Class<?> sourceClassFromAnnotation = source.source();
						if( sourceClassFromAnnotation.equals(methodSourceType ) ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.useBeanCopy = false;
							item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, methodTargetType);
							item.convertorClass = item.convertorObject.getClass();
							
							itemList.add(item);
							
							logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
									" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
						
						}
						else {
							logger.warn( "Property parameter does not match: " + 
									sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
									targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
							throw new BeanCopyException("Property parameter does not match: " + 
									sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
									targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
						}
						
					} else {
						logger.warn( "Property parameter does not match: " + 
								sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
								targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
						throw new BeanCopyException("Property parameter does not match: " + 
								sourceClass.getName() + "["+ propertyName+ "(" + methodSourceType.getSimpleName() + ")] : " + 
								targetClass.getName() + "[" + targetPd.getName() + "(" + methodTargetType.getSimpleName() + ")]");
					}
				}
			}
		}
		
		return itemList;
	}
	
	private static Field getClassField(Class<?> targetClass, Class<?> optionClass, String propertyName) {
		
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

	private static boolean isInterfaceType(Class<?> classType, Class<?> interfaceType) {
		
		if( classType.equals(interfaceType) ) {
			return true;
		}
		
		Class<?>[] interfaces = classType.getInterfaces();
		
		for( Class<?> interClass : interfaces ) {
			if( interClass.equals(interfaceType) ) {
				return true;
			}
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
	
	private static boolean isPrimitive(Class<?> classType) {
		if( classType.isPrimitive() )
			return true;
		
		for( Class<?> resolvedWrapper : primitiveWrapperTypeMap.keySet() ) {
			if( classType.equals(resolvedWrapper) ) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
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
