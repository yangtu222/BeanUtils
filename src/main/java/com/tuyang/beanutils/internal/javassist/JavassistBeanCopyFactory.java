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

package com.tuyang.beanutils.internal.javassist;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tuyang.beanutils.BeanCopier;
import com.tuyang.beanutils.BeanCopyConvertor;
import com.tuyang.beanutils.internal.cache.BeanCopyPropertyItem;
import com.tuyang.beanutils.internal.factory.BeanCopierFactory;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;

public class JavassistBeanCopyFactory implements BeanCopierFactory {
	
	private int instanceCount = 0;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public BeanCopier createBeanCopier(Class<?> sourceClass, Class<?> targetClass, List<BeanCopyPropertyItem> items) {
		int thisCount = 0;
		synchronized (this) {
			thisCount = this.instanceCount++;
		}
		
		String className = "com.tuyang.beanutils.internal.javassist.impl.BeanCopier$$javassist" + thisCount;
		
		try {
			int converterCount = 0;
			
			ClassPool pool = ClassPool.getDefault();
			if( thisCount == 0 ) {
				ClassClassPath classPath = new ClassClassPath(this.getClass());  
				pool.insertClassPath(classPath);
			}
			
			CtClass beanCopyInterface = pool.get(BeanCopier.class.getName());
			
			CtClass beanCopyCtClass = pool.makeClass(className);
			CtClass objectClass = pool.get(Object.class.getName());
			
			CtConstructor constructor = CtNewConstructor.defaultConstructor(beanCopyCtClass);
			beanCopyCtClass.addConstructor(constructor);
			
			StringBuffer sb = new StringBuffer();
			sb.append("{\n");
			
			sb.append(sourceClass.getName() + " source = (" +sourceClass.getName() + ") $1;\n");
			sb.append(targetClass.getName() + " target = (" +targetClass.getName() + ") $2;\n");
			
			Map<String, BeanCopyConvertor> convertorMap = new HashMap<>();
			
			for( BeanCopyPropertyItem item: items ) {
				
				String sourceMethod = "";
				Class<?> writeType = item.writeMethod.getParameterTypes()[0];
				Class<?> readType  = item.readMethods[item.readMethods.length-1].getReturnType();

				if( item.readMethods.length == 1 ) {
					sourceMethod = "source." + item.readMethods[0].getName() + "()";
				} else {
					sourceMethod = "source";
					sb.append("if( ");
					for( int i = 0; i< item.readMethods.length-1; i++ ) {
						if( i != 0 )
							sb.append(" && ");
						sourceMethod = sourceMethod + "." + item.readMethods[i].getName() + "()";
						sb.append( sourceMethod + " != null ");
					}
					sb.append(" ) { \n");
					sourceMethod = sourceMethod + "." + item.readMethods[item.readMethods.length-1].getName() + "()";
				}
				
				if( !item.isCollection ) {
					
					if( item.convertorObject != null ) {
						CtClass converterClass = pool.get(item.convertorClass.getName());
						String convertorName = "convertor"+ converterCount++;
						
						CtField field = new CtField(converterClass, convertorName , beanCopyCtClass );
						beanCopyCtClass.addField( field );
						
						String methodName = "set" + convertorName.substring(0, 1).toUpperCase() + convertorName.substring(1);
						String function = "this." + convertorName + "=$1;";
						CtMethod convertorMethod = CtNewMethod.make(CtClass.voidType , methodName, new CtClass[]{converterClass},
								null, function , beanCopyCtClass);
						
						beanCopyCtClass.addMethod(convertorMethod);
						
						converterClass.detach();
						convertorMap.put(methodName, item.convertorObject);
						
						sb.append("target." + item.writeMethod.getName() + "((" + getArrayClassName(writeType) + ")" + convertorName + ".convertTo(" ) .append(sourceMethod +") );\n");
						
					}
					
					else if( item.convertorClass != null ) {
						CtClass converterClass = pool.get(item.convertorClass.getName());
						String convertorName = "convertor"+ converterCount++;
						
						CtField field = new CtField(converterClass, convertorName , beanCopyCtClass );
						beanCopyCtClass.addField( field );
						
						constructor.insertAfter( "this."+convertorName +" = (" + item.convertorClass.getName() + ") com.tuyang.beanutils.internal.utils.InstanceUtils.newInstance("
										+ item.convertorClass.getName() + ".class);");
						
						sb.append("{\n");
						sb.append( writeType.getName() + " local=" + convertorName + ".convertTo(" );
						if( readType.isPrimitive() ) {
							String objectPrimitiveName = getPrimitiveName(readType);
							sb.append(  objectPrimitiveName + ".valueOf( " + sourceMethod + "))") ;
						}else {
							sb.append(sourceMethod +")");
						}
						if( writeType.isPrimitive() ) {
							sb.append("."+ writeType.toString()+"Value()");
						}
						sb.append(";\n");
						sb.append("target." + item.writeMethod.getName() + "( local );\n");
						sb.append("}\n");
						
						converterClass.detach();
						
					} else if( item.optionClass != null ) {
						sb.append("target." + item.writeMethod.getName() + "( ("+writeType.getName() +") com.tuyang.beanutils.BeanCopyUtils.copyBean( " + sourceMethod +", "
								+ writeType.getName() +".class, "+ item.optionClass.getName()+ ".class ) );\n");
					} else if( item.useBeanCopy ) {
						sb.append("target." + item.writeMethod.getName() + "( ("+writeType.getName() +") com.tuyang.beanutils.BeanCopyUtils.copyBean( " + sourceMethod +", "
								+ writeType.getName() +".class ) );\n");
					} else {
						if( writeType.isPrimitive() && !readType.isPrimitive() ) {
							sb.append("target." + item.writeMethod.getName() +"( "+sourceMethod + "." + writeType.toString() +"Value() );\n");
						} else if( !writeType.isPrimitive() && readType.isPrimitive() ) {
							sb.append("target." + item.writeMethod.getName() +"( "+ getPrimitiveName(readType) +".valueOf(" + sourceMethod + " ) );\n");
//							sb.append("target." + item.writeMethod.getName() +"(("+getPrimitiveName(readType) +") ($w) "+sourceMethod + " );\n");
						} else {
							sb.append("target." + item.writeMethod.getName() +"( "+sourceMethod + " );\n");
						}
					}
					
				} else {
					
					sb.append("{ " +readType.getName() + " localList=" + sourceMethod + ";\n");
					sb.append("if( localList !=null ) {\n");
					sb.append(writeType.getName() + " targetList=(" + writeType.getName()  + 
							")com.tuyang.beanutils.internal.utils.InstanceUtils.newCollection("+ writeType.getName() +".class);\n");
					sb.append("java.util.Iterator it = localList.iterator();\n");
					sb.append( "while( it.hasNext() ) {\n");
					sb.append("targetList.add( com.tuyang.beanutils.BeanCopyUtils.copyBean( it.next(), "+ item.collectionClass.getName() + ".class");
					if( item.optionClass != null ) {
						sb.append(", " +item.optionClass.getName()+".class");
					}
					sb.append("));\n}\n");
					sb.append("target." + item.writeMethod.getName() +"( targetList );\n");
					sb.append("} else { \ntarget." + item.writeMethod.getName() + "(null); }\n}\n");
				}
				
				if( item.readMethods.length >1 ) {
					sb.append("}\n");
				}
			}
			sb.append("return target;}\n");
//			System.out.println(sb.toString());
			CtMethod copyBeanMethod = CtNewMethod.make(objectClass , "copyBean", new CtClass[]{objectClass, objectClass},
					null, sb.toString() , beanCopyCtClass);
			beanCopyCtClass.addMethod(copyBeanMethod);
			beanCopyCtClass.addInterface(beanCopyInterface);
//			beanCopyCtClass.writeFile("D:/tmp");
			Class<BeanCopier> classBeanCopy = beanCopyCtClass.toClass();
			
			beanCopyCtClass.detach();
			
			BeanCopier retObject = classBeanCopy.newInstance();
			
			if( convertorMap.size() > 0 ) {
				for(String methodName : convertorMap.keySet() ) {
					BeanCopyConvertor convertor = convertorMap.get(methodName);
					try {
						Method method = retObject.getClass().getMethod(methodName, convertor.getClass());
						method.invoke(retObject, convertor);
					}catch (Exception e) {
						
					}
				}
			}
			
			return retObject;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private String getArrayClassName(Class<?> writeType) {
		
		if( writeType.getComponentType().isPrimitive() ) {
			if( writeType.equals(int[].class)) {
				return "int[]";
			} else if( writeType.equals(boolean[].class)) {
				return "boolean[]";
			} else if( writeType.equals(char[].class)) {
				return "char[]";
			} else if( writeType.equals(byte[].class)) {
				return "byte[]";
			} else if( writeType.equals(short[].class)) {
				return "short[]";
			} else if( writeType.equals(long[].class)) {
				return "long[]";
			} else if( writeType.equals(float[].class)) {
				return "float[]";
			} else if( writeType.equals(double[].class)) {
				return "double[]";
			}
		}
		else {
			return writeType.getComponentType().getName() + "[]";
		}
		
		return null;
	}

	private String getPrimitiveName(Class<?> readType) {
		if( readType.equals(int.class ) ) {
			return "Integer";
		} else if( readType.equals(float.class ) ) {
			return "Float";
		} else if( readType.equals(boolean.class ) ) {
			return "Boolean";
		} else if( readType.equals(char.class ) ) {
			return "Character";
		} else if( readType.equals(byte.class ) ) {
			return "Byte";
		} else if( readType.equals(long.class ) ) {
			return "Long";
		} else if( readType.equals(short.class ) ) {
			return "Short";
		} else if( readType.equals(double.class ) ) {
			return "Double";
		}
		return "";
	}
	
}
