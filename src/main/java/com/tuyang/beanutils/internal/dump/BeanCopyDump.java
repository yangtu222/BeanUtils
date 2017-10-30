package com.tuyang.beanutils.internal.dump;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.tuyang.beanutils.config.BeanCopyConfig;
import com.tuyang.beanutils.config.BeanCopyConfig.DumpOption;
import com.tuyang.beanutils.internal.cache.BeanCopyCache;
import com.tuyang.beanutils.internal.cache.BeanCopyPropertyItem;
import com.tuyang.beanutils.internal.logger.Logger;
import com.tuyang.beanutils.internal.utils.PropertyUtils;

public class BeanCopyDump {
	
	private static Logger logger = Logger.getLogger(BeanCopyDump.class);
	
	private static final int LineCharCount = 93;
	private static ThreadLocal<Integer> localDumpLevel = new ThreadLocal<>();
	private static ThreadLocal<List<Long>> localDumpStack = new ThreadLocal<>();
	
	public static void beginDump() {
		
		if( BeanCopyConfig.instance().getDumpOption() == DumpOption.AutoDumpNone )
			return;
		
		Integer dumpLevel = localDumpLevel.get();
		if( dumpLevel == null ) {
			dumpLevel = -1;
		}
		if(dumpLevel == -1 ) {
			localDumpStack.set(new ArrayList<Long>());
		}
		localDumpLevel.set(++dumpLevel);
	}
	
	public static void endDump() {
		if( BeanCopyConfig.instance().getDumpOption() == DumpOption.AutoDumpNone )
			return;

		Integer dumpLevel = localDumpLevel.get();
		localDumpLevel.set(--dumpLevel);
		if(dumpLevel == -1 && localDumpStack.get().size() > 0 ) {
			logger.info("=============================================================================================");
		}
	}
	
	public static void dumpPropertyMapping(Class<?> sourceClass, Class<?> targetClass , Class<?> optionClass) {
		Integer dumpLevel = localDumpLevel.get();
		if( dumpLevel == null ) {
			dumpLevel = -1;
		}
		if(dumpLevel == -1 ) {
			localDumpStack.set(new ArrayList<Long>());
		}
		localDumpLevel.set(++dumpLevel);
		
		dumpPropertyMappingInternal(sourceClass, targetClass, optionClass);
		
		localDumpLevel.set(--dumpLevel);
		if(dumpLevel == -1 && localDumpStack.get().size() > 0 ) {
			logger.info("=============================================================================================");
		}
	}
	
	public static void dumpPropertyMappingInternal(Class<?> sourceClass, Class<?> targetClass , Class<?> optionClass) {
		if( optionClass == null )
			optionClass = targetClass;
		
		Integer dumpLevel = localDumpLevel.get();
		List<Long> stackCacheKeyList = localDumpStack.get();
		if( stackCacheKeyList == null ) {
			stackCacheKeyList = new ArrayList<>();
			localDumpStack.set(stackCacheKeyList);
		}

		long cacheKey = (((long)sourceClass.hashCode() ) << 16 )+ (long) targetClass.hashCode();
		if( optionClass != null ) {
			cacheKey = (cacheKey <<16)  + (long)optionClass.hashCode();
		} else {
			cacheKey = (cacheKey <<16)  + (long)targetClass.hashCode();
		}
		
		if( stackCacheKeyList.contains(cacheKey) ) {
			return;
		}
		if( stackCacheKeyList.isEmpty() ) {
			logger.info("=============================================================================================");
			logger.info("Dump Bean Copy Property Mapping:");
		}
		stackCacheKeyList.add(cacheKey);
		dumpPropertyMappingInternal(sourceClass, targetClass, optionClass, dumpLevel);
	}
	
	private static void dumpPropertyMappingInternal(Class<?> sourceClass, Class<?> targetClass , Class<?> optionClass, int level ) {
		if( optionClass == null )
			optionClass = targetClass;
		
		logger.info("---------------------------------------------------------------------------------------------");
		logger.info("From: [" + sourceClass.getSimpleName() + "] To: [" + targetClass.getSimpleName() + "] Option: [" + optionClass.getSimpleName() + "]");
		logger.info("---------------------------------------------------------------------------------------------");
		List<BeanCopyPropertyItem> itemList = BeanCopyCache.buildBeanCopyPropertyItem(sourceClass, targetClass, optionClass);
		PropertyDescriptor[] sourcePds = PropertyUtils.getPropertyDescriptors(sourceClass);
		PropertyDescriptor[] targetPds = PropertyUtils.getPropertyDescriptors(targetClass);
		List<PropertyDescriptor> ignoredSourcePds = new ArrayList<>();
		List<PropertyDescriptor> ignoredTargetPds = new ArrayList<>();
		for( PropertyDescriptor pDescriptor : sourcePds ) {
			ignoredSourcePds.add(pDescriptor);
		}
		for( PropertyDescriptor pDescriptor : targetPds ) {
			ignoredTargetPds.add(pDescriptor);
		}
		
		for( BeanCopyPropertyItem item : itemList ) {
			
			Method readMethod = item.readMethods[0];
			Method writeMethod = item.writeMethod;
			
			PropertyDescriptor readPd  = findPropertyDescriptor( sourcePds, readMethod, true);
			PropertyDescriptor writePd = findPropertyDescriptor( targetPds, writeMethod, false);
			
			ignoredSourcePds.remove(readPd);
			ignoredTargetPds.remove(writePd);

			if( readMethod.getDeclaringClass().equals(Object.class)) {
				continue;
			}
			Class<?> readType = item.readMethods[item.readMethods.length-1].getReturnType() ;
			Class<?> writeType = writeMethod.getParameterTypes()[0];
			
			StringBuilder sBuilder = new StringBuilder();
			
			sBuilder.append(readType.getSimpleName()).append(" ");
			
			for( int i =0; i< item.readMethods.length; i++ ) {
				Method method = item.readMethods[i];
				if( i != 0 )
					sBuilder.append(".");
				sBuilder.append(method.getName()).append("()");
			}
			
			for( int i = sBuilder.length(); i < LineCharCount/2; i++ ) {
				sBuilder.insert(0, ' ');
			}
			sBuilder.append("|");
			sBuilder.append(writeType.getSimpleName()).append(" ");
			
			sBuilder.append(item.writeMethod.getName()).append("()");
			logger.info( sBuilder.toString() );
		}
		
		if( !ignoredSourcePds.isEmpty() ) {
			for( PropertyDescriptor pd : ignoredSourcePds ) {
				Method method = pd.getReadMethod() != null ? pd.getReadMethod(): pd.getWriteMethod();
				
				Class<?> belongClass = method.getDeclaringClass();
				if( belongClass.equals(Object.class))
					continue;
				
				method = pd.getReadMethod();
				
				if( method != null ) {
					Class<?> readType = method.getReturnType() ;
					StringBuilder sBuilder = new StringBuilder();
					sBuilder.append(readType.getSimpleName()).append(" ");
					sBuilder.append(method.getName()).append("()");
					for( int i = sBuilder.length(); i < LineCharCount/2; i++ ) {
						sBuilder.insert(0, ' ');
					}
					sBuilder.append("|(ignored)");
					logger.info( sBuilder.toString() );
				} else {

					StringBuilder sBuilder = new StringBuilder();
					sBuilder.append("(No get method for ").append(pd.getName()).append(")");
					for( int i = sBuilder.length(); i < LineCharCount/2; i++ ) {
						sBuilder.insert(0, ' ');
					}
					sBuilder.append("|(ignored)");
					logger.info( sBuilder.toString() );
				}
				
			}
		}
		
		if( !ignoredTargetPds.isEmpty() ) {
			for( PropertyDescriptor pd : ignoredTargetPds ) {
				Method method = pd.getWriteMethod() != null ? pd.getWriteMethod() : pd.getReadMethod();
				Class<?> belongClass = method.getDeclaringClass();
				if( belongClass.equals(Object.class))
					continue;
				method = pd.getWriteMethod();
				if( method != null ) {
					Class<?> writeType = method.getParameterTypes()[0];
					StringBuilder sBuilder = new StringBuilder();
					sBuilder.append("(ignored)");
					for( int i = sBuilder.length(); i < LineCharCount/2; i++ ) {
						sBuilder.insert(0, ' ');
					}
					sBuilder.append("|");
					sBuilder.append(writeType.getSimpleName()).append(" ");
					sBuilder.append( method.getName() ).append("()");
					logger.info( sBuilder.toString() );
				} else {
					StringBuilder sBuilder = new StringBuilder();
					sBuilder.append("(ignored)");
					for( int i = sBuilder.length(); i < LineCharCount/2; i++ ) {
						sBuilder.insert(0, ' ');
					}
					sBuilder.append("|");
					sBuilder.append("(No set method for "+ pd.getName() +")");
					logger.info( sBuilder.toString() );
				}
				
			}
		}
		
		for( BeanCopyPropertyItem item : itemList ) {
			if( item.useBeanCopy || item.optionClass != null ) {
				Class<?> sourceProertyType = item.readMethods[item.readMethods.length-1].getReturnType();
				Class<?> targetPropertyType = item.writeMethod.getParameterTypes()[0];
				if( targetPropertyType.isArray() || PropertyUtils.isInterfaceType(targetPropertyType, Collection.class) )
					continue;
				dumpPropertyMappingInternal(sourceProertyType, targetPropertyType, item.optionClass, level+1 );
			}
		}
	}

	private static PropertyDescriptor findPropertyDescriptor(PropertyDescriptor[] sourcePds, Method method, boolean isRead ) {
		
		for( PropertyDescriptor pDescriptor : sourcePds ) {
			if( isRead ) {
				if ( pDescriptor.getReadMethod() != null && pDescriptor.getReadMethod().equals(method) ) {
					return pDescriptor;
				}
			}
			if( !isRead ) {
				if( pDescriptor.getWriteMethod() != null && pDescriptor.getWriteMethod().equals(method) ) {
					return pDescriptor;
				}
			}
		}
		
		return null;
	}
	
}
