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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tuyang.beanutils.BeanCopier;
import com.tuyang.beanutils.BeanCopyConvertor;
import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyCollection;
import com.tuyang.beanutils.annotation.CopyFeature;
import com.tuyang.beanutils.annotation.CopyProperty;
import com.tuyang.beanutils.config.BeanCopyConfig;
import com.tuyang.beanutils.exception.BeanCopyException;
import com.tuyang.beanutils.internal.convertors.ArrayConvertorFactory;
import com.tuyang.beanutils.internal.convertors.EnumToEnumArrayConvertor;
import com.tuyang.beanutils.internal.convertors.EnumToStringArrayConvertor;
import com.tuyang.beanutils.internal.convertors.EnumToStringConvertor;
import com.tuyang.beanutils.internal.convertors.ObjectToStringConvertor;
import com.tuyang.beanutils.internal.convertors.ListToEnumArrayConvertor;
import com.tuyang.beanutils.internal.convertors.StringToEnumArrayConvertor;
import com.tuyang.beanutils.internal.convertors.ObjectToEnumConvertor;
import com.tuyang.beanutils.internal.convertors.ObjectToStringArrayConvertor;
import com.tuyang.beanutils.internal.dump.BeanCopyDump;
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
		
		if( BeanCopyConfig.instance().getDumpOption() == BeanCopyConfig.DumpOption.AutoDumpAlways ) {
			BeanCopyDump.dumpPropertyMapping(sourceClass, targetClass, optionClass);
		}
		
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
		
		CopyFeature[] features = parseBeanCopyFeatures(sourceClass, targetClass, optionClass);
		List<BeanCopyPropertyItem> itemList = buildBeanCopyPropertyItem(sourceClass, targetClass, optionClass);
		beanCopy = beanCopyFactory.createBeanCopier(sourceClass, targetClass, itemList, features);
		if( beanCopy != null ) {
			beanCopyCacheMap.put(cacheKey, new SoftReference<BeanCopier>(beanCopy));
		}
		if( BeanCopyConfig.instance().getDumpOption() == BeanCopyConfig.DumpOption.AutoDumpAtFirstCopy ) {
			BeanCopyDump.dumpPropertyMapping(sourceClass, targetClass, optionClass, itemList);
		}

		return beanCopy;
	}

	private static CopyFeature[] parseBeanCopyFeatures(Class<?> sourceClass, Class<?> targetClass, Class<?> optionClass ) {
		
		if( optionClass != null ) {
			if( optionClass.isAnnotationPresent(BeanCopySource.class) ) {
				BeanCopySource source = optionClass.getAnnotation(BeanCopySource.class);
				Class<?> sourceClassFromAnnotation = source.source();
				if( sourceClassFromAnnotation.isAssignableFrom(sourceClass) ) {
					return source.features();
				} else {
					//fix sourceClass is proxy class.
					if( sourceClass.getName().startsWith(sourceClassFromAnnotation.getName()) ) {
						return source.features();
					}
				}
			}
		}
		if( targetClass.isAnnotationPresent(BeanCopySource.class) ) {
			BeanCopySource source = targetClass.getAnnotation(BeanCopySource.class);
			Class<?> sourceClassFromAnnotation = source.source();
			if( sourceClassFromAnnotation.isAssignableFrom(sourceClass) ) {
				return source.features();
			}
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static List<BeanCopyPropertyItem> buildBeanCopyPropertyItem(Class<?> sourceClass, Class<?> targetClass, Class<?> optionClass ) {
		
		List<BeanCopyPropertyItem> itemList = new ArrayList<>();
		
		Class<?> beanAnnotationSource = null;
		BeanCopySource beanCopySource = null;
		if( optionClass != null ) {
			if( optionClass.isAnnotationPresent(BeanCopySource.class) ) {
				beanCopySource = optionClass.getAnnotation(BeanCopySource.class);
				Class<?> sourceClassFromAnnotation = beanCopySource.source();
				if( sourceClassFromAnnotation.isAssignableFrom(sourceClass) ) {
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
			beanCopySource = targetClass.getAnnotation(BeanCopySource.class);
			Class<?> sourceClassFromAnnotation = beanCopySource.source();
			if( sourceClassFromAnnotation.isAssignableFrom(sourceClass) ) {
				beanAnnotationSource = sourceClassFromAnnotation;
			}
		}
		
		boolean enumThrowExceptions = true;
		boolean useObjectToStringCopy = false;
		CopyFeature[] features = null;
		if( beanCopySource != null ) {
			features = beanCopySource.features();
			for( CopyFeature feature :features ) {
				if(feature == CopyFeature.IGNORE_ENUM_CONVERT_EXCEPTION ) {
					enumThrowExceptions = false;
				}else if( feature == CopyFeature.ENABLE_JAVA_BEAN_TO_STRING ) {
					useObjectToStringCopy = true;
				}
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
			
			propertyField = PropertyUtils.getClassField(targetClass, optionClass, propertyName);
			
			if( propertyField!= null && propertyField.isAnnotationPresent(CopyProperty.class)) {
				
				CopyProperty copyAnnotation = propertyField.getAnnotation(CopyProperty.class);
				String annotationPropertyName = copyAnnotation.property();
				
				if( copyAnnotation.ignored() ) {
					continue;
				}
				
				if( !( annotationPropertyName == null || "".equals(annotationPropertyName) ) ) {
					if( beanAnnotationSource == null ) {
						logger.warn("BeanCopy: " + targetClass.getName() + " has no BeanCopySource annotation, but " 
								+ propertyName + " has BeanProperty annotation with property defined");
						throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " has no BeanCopySource annotation, but " 
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
				else if( !PropertyUtils.isInterfaceType(convertorClass, BeanCopyConvertor.class ) ) {
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
						
						Class<?> converterClassSource = null;
						Class<?> converterClassTarget = null;
						Type[] converterTypes = convertorInterface.getActualTypeArguments();
						if( converterTypes[0] instanceof ParameterizedType  ) {
							converterClassSource = (Class<?>) ((ParameterizedType)converterTypes[0]).getRawType();
						} else {
							converterClassSource = (Class<?>) converterTypes[0];
						}
						if( converterTypes[1] instanceof ParameterizedType ) {
							converterClassTarget = (Class<?>) ((ParameterizedType)converterTypes[1]).getRawType();
						} else {
							converterClassTarget = (Class<?>) converterTypes[1];
						}
						
						if( !( PropertyUtils.isAssignable(methodSourceType, converterClassSource) && PropertyUtils.isAssignable(methodTargetType, converterClassTarget ) ) ) {
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
				
				if( convertorClass == null ) {
					if( (!targetIsArray && sourceIsArray) || 
						(targetIsArray && (!sourceIsArray && !PropertyUtils.isInterfaceType(methodSourceType, Collection.class) ) ) ) {
						logger.error("BeanCopy: " + targetClass.getName() + " property " + propertyName
								+ " Array type mismatch!!");
						throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
								+ " Array type mismatch!!");
					}
				}
				
				if( ( PropertyUtils.isPrimitive(methodSourceType) || PropertyUtils.isPrimitive(methodTargetType) ) && propertyOptionClass != null ) {
					logger.error("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Annotation BeanProperty optionClass cannot be set on primitive tpye!!");
					throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
							+ " Annotation BeanProperty convertor and optionClass on primitive tpye!!"); 
				}
				
				if( convertorClass != null ) {
					
					BeanCopyPropertyItem item = new BeanCopyPropertyItem();
					
					item.propertyName = propertyName;
					item.writeMethod = writeMethod;
					item.readMethods = readMethods;
					item.isCollection = isCollection;
					item.useBeanCopy = false;
					item.optionClass = null;
					item.convertorClass = convertorClass;
					item.convertorObject = (BeanCopyConvertor) convertorObject;
					
					itemList.add(item);
					
				}
				else if( !targetIsArray ) {
					
					if( PropertyUtils.isAssignable(methodTargetType, methodSourceType) ) {
						
						if( PropertyUtils.isPrimitive(methodTargetType)) {
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.optionClass = null;
							item.convertorClass = null;
							item.useBeanCopy = false;
							
							itemList.add(item);
						}
						else{
							//deep copy.
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.optionClass = null;
							item.convertorClass = null;
							item.useBeanCopy = true;
							
							itemList.add(item);
						}
					} 
					else if( propertyOptionClass != null ) {
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.propertyName = propertyName;
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						item.optionClass = propertyOptionClass;
						
						itemList.add(item);
					}
					
					else if( methodTargetType.isAnnotationPresent(BeanCopySource.class) ) {
						
						beanCopySource = methodTargetType.getAnnotation(BeanCopySource.class);
						Class<?> sourceClassFromAnnotation = beanCopySource.source();
						if( sourceClassFromAnnotation.equals(methodSourceType ) ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.useBeanCopy = false;
							item.optionClass = methodTargetType;
							
							itemList.add(item);
							
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
					else if( methodTargetType.isEnum() ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.propertyName = propertyName;
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.optionClass = null;
						item.convertorClass = ObjectToEnumConvertor.class;
						item.convertorObject = new ObjectToEnumConvertor(methodTargetType, enumThrowExceptions);
						item.useBeanCopy = true;
						
						itemList.add(item);
					}
					else if( methodTargetType.equals(String.class) && methodSourceType.isEnum() ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.propertyName = propertyName;
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.optionClass = null;
						item.convertorClass = EnumToStringConvertor.class;
						item.convertorObject = new EnumToStringConvertor();
						item.useBeanCopy = true;
						
						itemList.add(item);
					}
					else if( !(PropertyUtils.isPrimitive(methodSourceType) || PropertyUtils.isPrimitive(methodTargetType) ) ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.propertyName = propertyName;
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.optionClass = null;
						item.convertorClass = null;
						item.useBeanCopy = true;
						
						itemList.add(item);
					}
					else {
						
						if( useObjectToStringCopy && methodTargetType.equals(String.class) ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.optionClass = null;
							item.convertorClass = ObjectToStringConvertor.class;
							item.convertorObject = new ObjectToStringConvertor();
							item.useBeanCopy = false;
							
							itemList.add(item);
							
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
				else if( targetIsArray ) {
					
					if( methodTargetArray.equals(methodSourceArray) ) {
						//use deep copy.
						if( methodSourceType.isEnum() ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.useBeanCopy = false;
							item.optionClass = null;
							item.convertorObject = new EnumToEnumArrayConvertor();
							item.convertorClass = item.convertorObject.getClass();
							
							itemList.add(item);
						} else {
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.useBeanCopy = false;
							item.optionClass = null;
							item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, null);
							item.convertorClass = item.convertorObject.getClass();
							
							itemList.add(item);
						}
					}
					else if( PropertyUtils.isAssignable(methodTargetType, methodSourceType) ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.propertyName = propertyName;
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						item.optionClass = null;
						item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, null);
						item.convertorClass = item.convertorObject.getClass();
						
						itemList.add(item);
					}
					else if( PropertyUtils.isInterfaceType(methodSourceType, Collection.class) ) {
						
						if( methodTargetType.isEnum() ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.useBeanCopy = false;
							item.optionClass = null;
							item.convertorObject = new ListToEnumArrayConvertor(methodTargetType, enumThrowExceptions);
							item.convertorClass = item.convertorObject.getClass();
							
							itemList.add(item);
						} else {
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = true;
							item.useBeanCopy = true;
							item.convertorObject = null;
							item.convertorClass = null;
							item.optionClass = propertyOptionClass;
							item.features = features;
							
							itemList.add(item);
						}
					}
					else if( propertyOptionClass != null ) { 
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.propertyName = propertyName;
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, propertyOptionClass);
						item.convertorClass = item.convertorObject.getClass();
						item.optionClass = null;
						
						itemList.add(item);
					}
					else if( methodTargetType.isAnnotationPresent(BeanCopySource.class) ) {
						
						BeanCopySource source = methodTargetType.getAnnotation(BeanCopySource.class);
						Class<?> sourceClassFromAnnotation = source.source();
						if( sourceClassFromAnnotation.equals(methodSourceType ) ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.useBeanCopy = false;
							item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, methodTargetType);
							item.convertorClass = item.convertorObject.getClass();
							item.optionClass = null;
							
							itemList.add(item);
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
					else if( methodSourceType.isEnum() && methodTargetType.equals(String.class)) {
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.propertyName = propertyName;
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.optionClass = null;
						item.convertorClass = EnumToStringArrayConvertor.class;
						item.convertorObject = new EnumToStringArrayConvertor();
						item.useBeanCopy = true;
						
						itemList.add(item);
					}
					else if( methodSourceType.equals(String.class) && methodTargetType.isEnum() ) {
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.propertyName = propertyName;
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.optionClass = null;
						item.convertorClass = StringToEnumArrayConvertor.class;
						item.convertorObject = new StringToEnumArrayConvertor(methodTargetType, enumThrowExceptions);
						item.useBeanCopy = true;
						
						itemList.add(item);

					}
					else if( !(PropertyUtils.isPrimitive(methodSourceType) || PropertyUtils.isPrimitive(methodTargetType) ) ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.propertyName = propertyName;
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
					}
					else {
						
						if( useObjectToStringCopy && methodTargetType.equals(String.class) ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.optionClass = null;
							item.convertorClass = ObjectToStringArrayConvertor.class;
							item.convertorObject = new ObjectToStringArrayConvertor();
							item.useBeanCopy = false;
							
							itemList.add(item);
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
				
			} else if( propertyField!= null && propertyField.isAnnotationPresent(CopyCollection.class)){
				
				CopyCollection copyAnnotation = propertyField.getAnnotation(CopyCollection.class);
				String annotationPropertyName = copyAnnotation.property();
				
				if( copyAnnotation.ignored() ) {
					continue;
				}
				
				if( !( annotationPropertyName == null || "".equals(annotationPropertyName) ) ) {
					if( beanAnnotationSource == null ) {
						logger.warn("BeanCopy: " + targetClass.getName() + " has no BeanCopySource annotation, but " 
								+ propertyName + " has BeanProperty annotation with property defined");
						throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " has no BeanCopySource annotation, but " 
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
				
				if( !PropertyUtils.isInterfaceType(methodSourceArray, Collection.class) && !methodSourceArray.isArray() ) {
					logger.warn("BeanCopy: " + sourceClass.getName() + " property " + propertyName
							+ " is not collection type!");
					throw new BeanCopyException("BeanCopy: " + sourceClass.getName() + " property " + propertyName
						+ " is not collection type!");
				}
				
				if( !PropertyUtils.isInterfaceType(methodTargetType, Collection.class) ) {
					logger.warn("BeanCopy: " + targetClass.getName() + " property " + propertyName
						+ " is not collection type!");
					throw new BeanCopyException("BeanCopy: " + targetClass.getName() + " property " + propertyName
						+ " is not collection type!");
				}
				
				isCollection = true;
				collectionClass = copyAnnotation.targetClass();
				
				if( methodSourceArray.isArray() ) {
					BeanCopyPropertyItem item = new BeanCopyPropertyItem();
					
					item.propertyName = propertyName;
					item.writeMethod = writeMethod;
					item.readMethods = readMethods;
					item.isCollection = true;
					item.useBeanCopy = true;
					item.collectionClass = collectionClass;
					item.optionClass = propertyOptionClass;
					
					itemList.add(item);
				} else if( collectionClass.isEnum() ) {
					
					BeanCopyPropertyItem item = new BeanCopyPropertyItem();
					
					item.propertyName = propertyName;
					item.writeMethod = writeMethod;
					item.readMethods = readMethods;
					item.isCollection = true;
					item.useBeanCopy = true;
					item.collectionClass = collectionClass;
					item.optionClass = propertyOptionClass;
					
					itemList.add(item);
					
					
				} else if( collectionClass.equals(String.class) && useObjectToStringCopy ){
					BeanCopyPropertyItem item = new BeanCopyPropertyItem();
					
					item.propertyName = propertyName;
					item.writeMethod = writeMethod;
					item.readMethods = readMethods;
					item.isCollection = true;
					item.useBeanCopy = true;
					item.collectionClass = collectionClass;
					item.optionClass = propertyOptionClass;
					
					itemList.add(item);
				}
				else {
					BeanCopyPropertyItem item = new BeanCopyPropertyItem();
					
					item.propertyName = propertyName;
					item.writeMethod = writeMethod;
					item.readMethods = readMethods;
					item.isCollection = true;
					item.useBeanCopy = false;
					item.collectionClass = collectionClass;
					item.optionClass = propertyOptionClass;
					
					itemList.add(item);
				}
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
					if( PropertyUtils.isAssignable(methodTargetType, methodSourceType) ) {
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.propertyName = propertyName;
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						
						itemList.add(item);
						
//						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
//								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
					} 
					else if(methodTargetType.isAnnotationPresent(BeanCopySource.class) ) {
						
						BeanCopySource source = methodTargetType.getAnnotation(BeanCopySource.class);
						Class<?> sourceClassFromAnnotation = source.source();
						if( sourceClassFromAnnotation.equals(methodSourceType ) ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.useBeanCopy = false;
							item.optionClass = methodTargetType;
							
							itemList.add(item);
							
//							logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
//									" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
						
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
						
						item.propertyName = propertyName;
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						
						itemList.add(item);
						
//						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
//								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
					}
					else if( PropertyUtils.isAssignable(methodTargetType, methodSourceType) ) {
						
						BeanCopyPropertyItem item = new BeanCopyPropertyItem();
						
						item.propertyName = propertyName;
						item.writeMethod = writeMethod;
						item.readMethods = readMethods;
						item.isCollection = false;
						item.useBeanCopy = false;
						item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, null);
						item.convertorClass = item.convertorObject.getClass();
						
						itemList.add(item);
						
//						logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
//								" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
						
					} else if( methodTargetType.isAnnotationPresent(BeanCopySource.class) ) {
						
						BeanCopySource source = methodTargetType.getAnnotation(BeanCopySource.class);
						Class<?> sourceClassFromAnnotation = source.source();
						if( sourceClassFromAnnotation.equals(methodSourceType ) ) {
							
							BeanCopyPropertyItem item = new BeanCopyPropertyItem();
							
							item.propertyName = propertyName;
							item.writeMethod = writeMethod;
							item.readMethods = readMethods;
							item.isCollection = false;
							item.useBeanCopy = false;
							item.convertorObject = ArrayConvertorFactory.getArrayConvertor(methodSourceType, methodTargetType, methodTargetType);
							item.convertorClass = item.convertorObject.getClass();
							
							itemList.add(item);
							
//							logger.debug("BeanCopy: Add Copy Item From " + sourceClass.getSimpleName() + "[" + propertyName+ "]" +
//									" To " + targetClass.getSimpleName() + "[" + writeMethod.getName() + "]");
						
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
}

