 /*
  BeanUtils Version 1.0.2

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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;

import com.tuyang.beanutils.internal.cache.BeanCopyCache;
import com.tuyang.beanutils.internal.dump.BeanCopyDump;
import com.tuyang.beanutils.internal.utils.InstanceUtils;
import com.tuyang.beanutils.internal.utils.PropertyUtils;

/**
 * Static convenience methods for JavaBeans: copy source JavaBean properties to target JavaBean properties. 
 *
 */
public class BeanCopyUtils {
	
	/**
	 * Get the BeanCopier instance's interface for Bean Copy.
	 * <p>The following code snippet is an example of use of this class:</p>
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   BeanCopier copier = BeanCopyUtils.getBeanCopier( FromBean.class, ToBean.class );
	 *   ToBean toBean = new ToBean();
	 *   copier(fromBean, toBean);
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceClass The source object class.
	 * @param targetClass the target object class.
	 * @return the BeanCopier instance.
	 * 
	 * @see com.tuyang.beanutils.BeanCopier
	 */
	public static BeanCopier getBeanCopier( Class<?> sourceClass, Class<?> targetClass ) {
		return getBeanCopier(sourceClass, targetClass, null);
	}
	
	/**
	 * Get the BeanCopier instance's interface for Bean Copy.
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   BeanCopier copier = BeanCopyUtils.getBeanCopier( FromBean.class, ToBean.class, ToBeanOption.class );
	 *   ToBean toBean = new ToBean();
	 *   copier(fromBean, toBean);
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceClass The source object class.
	 * @param targetClass the target object class.
	 * @param optionClass The option class.
	 * @return the BeanCopier instance.
	 * @see com.tuyang.beanutils.BeanCopier
	 */
	public static BeanCopier getBeanCopier( Class<?> sourceClass, Class<?> targetClass, Class<?> optionClass ) {
		if( sourceClass == null ) {
			throw new NullPointerException("sourceClass must not be null");
		}
		if( targetClass == null ) {
			throw new NullPointerException("targetClass must not be null");
		}
		return BeanCopyCache.getBeanCopy(sourceClass, targetClass, optionClass);
	}

	/**
	 * New a targetClass instance and copy property values from sourceObject. 
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceObject The source object.
	 * @param targetClass The target object class.
	 * @param <T> target template type.
	 * @return If sourceObject is null, then return null, otherwise return targetClass instance with the property 
	 * values been copied to. 
	 */
	public static <T> T copyBean( Object sourceObject, Class<T> targetClass ) {
		return copyBean(sourceObject, targetClass, null);
	}
	
	/**
	 * Copy property values from sourceObject to targetObject. 
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   ToBean toBean = ...;
	 *   toBean = (ToBean) BeanCopyUtils.copyBean(fromBean, toBean );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceObject The source object.
	 * @param targetObject The target object.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return targetClass instance 
	 * with the property values been copied to. 
	 */
	public static Object copyBean( Object sourceObject, Object targetObject ) {
		return copyBean(sourceObject, targetObject, null);
	}
	
	/**
	 * New a targetClass instance and copy property values from sourceObject to targetObject. 
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class, ToBeanOption.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceObject The source object.
	 * @param targetClass The target object.
	 * @param optionClass The option class.
	 * @param <T> target template type.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return targetClass instance 
	 * with the property values been copied to. 
	 */
	public static <T> T copyBean( Object sourceObject, Class<T> targetClass, Class<?> optionClass ) {
		
		if( sourceObject == null ) 
			return null;

		if( targetClass == null ) {
			throw new NullPointerException("targetClass must not be null");
		}
		
		T targetObject = (T) InstanceUtils.newInstance(targetClass);
		return copyBean(sourceObject, targetObject, optionClass);
	}
	
	/**
	 * Copy property values from sourceObject to targetObject. 
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   ToBean toBean = ...;
	 *   toBean = (ToBean) BeanCopyUtils.copyBean(fromBean, toBean, ToBeanOption.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceObject The source object.
	 * @param targetObject The target object.
	 * @param optionClass The option class.
	 * @param <T> target template type.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return targetClass instance 
	 * with the property values been copied to. 
	 */
	@SuppressWarnings({ "unchecked" })
	public static <T> T copyBean( Object sourceObject, T targetObject , Class<?> optionClass ) {
		
		if( sourceObject == null ) {
			throw new NullPointerException("sourceObject must not be null");
		}
		if( targetObject == null ) {
			throw new NullPointerException("targetObject must not be null");
		}
		BeanCopyDump.beginDump();
		BeanCopier beanCopy = BeanCopyCache.getBeanCopy(sourceObject.getClass(), targetObject.getClass(), optionClass);
		targetObject = (T) beanCopy.copyBean(sourceObject, targetObject);
		BeanCopyDump.endDump();
	
		return targetObject;
	}
	
	/**
	 * Copy property values from List object to a new List. Be default, the new List is ArrayList.
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   List&lt;FromBean&gt; fromBeanList = ...;
	 *   List&lt;ToBean&gt; toBeanList = BeastnCopyUtils.copyList(fromBeanList, toBean.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceList The source List object.
	 * @param targetClass The target object.
	 * @param <S> source template type.
	 * @param <T> target template type.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 */
	public static <S, T> List<T> copyList(List<S> sourceList, Class<T> targetClass ) {
		return (List<T>) copyCollection(sourceList, targetClass, null);
	}
	
	/**
	 * Copy property values from List object to a new List. Be default, the new List is ArrayList.
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   List&lt;FromBean&gt; fromBeanList = ...;
	 *   List&lt;ToBean&gt; toBeanList = BeastnCopyUtils.copyList(fromBeanList, toBean.class, ToBeanOption.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceList The source List object.
	 * @param targetClass The target object.
	 * @param optionClass The option class.
	 * @param <S> source template type.
	 * @param <T> target template type.
	 * @return If sourceList is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 */
	public static <S, T> List<T> copyList(List<S> sourceList, Class<T> targetClass, Class<?> optionClass ) {
		return (List<T>) copyCollection(sourceList, targetClass, optionClass);
	}
	
	/**
	 * Copy property values from Array object to a new List. Be default, the new List is ArrayList.
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   FromBean[] fromBeanArray = ...;
	 *   List&lt;ToBean&gt; toBeanList = BeastnCopyUtils.copyList(fromBeanArray, toBean.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceArray The source array object.
	 * @param targetClass The target object.
	 * @param <S> source template type.
	 * @param <T> target template type.
	 * @return If sourceArray is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 */
	public static <S, T> List<T> copyList(S[] sourceArray, Class<T> targetClass ) {
		return copyList( sourceArray, targetClass, null);
	}

	/**
	 * Copy property values from Array object to a new List. Be default, the new List is ArrayList.
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   FromBean[] fromBeanArray = ...;
	 *   List&lt;ToBean&gt; toBeanList = BeastnCopyUtils.copyList(fromBeanArray, toBean.class, ToBeanOption.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceArray The source array object.
	 * @param targetClass The target object.
	 * @param optionClass The option class.
	 * @param <S> source template type.
	 * @param <T> target template type.
	 * @return If sourceArray is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 */
	@SuppressWarnings("unchecked")
	public static <S, T> List<T> copyList(S[] sourceArray, Class<T> targetClass, Class<?> optionClass ) {
		if( sourceArray == null )
			return null;
		BeanCopyDump.beginDump();
		List<T> dataList = (List<T>) InstanceUtils.newCollection(List.class);
		for( S s : sourceArray ) {
			T newInst = (T) copyBean(s, targetClass, optionClass);
			dataList.add(newInst);
		}
		BeanCopyDump.endDump();
		return dataList;
	}
	
	/**
	 * Copy property values from List object to a new Array Object.
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   List&lt;FromBean&gt; fromBeanList = ...;
	 *   ToBean[] toBeanArray = BeastnCopyUtils.copyArray(fromBeanList, toBean.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceList The source List object.
	 * @param targetClass The target object.
	 * @param <S> source template type.
	 * @param <T> target template type.
	 * @return If sourceList is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 */
	public static <S, T> T[] copyArray(List<S> sourceList, Class<T> targetClass ) {
		return copyArray( sourceList, targetClass, null);
	}

	/**
	 * Copy property values from List object to a new Array Object.
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   List&lt;FromBean&gt; fromBeanList = ...;
	 *   ToBean[] toBeanArray = BeastnCopyUtils.copyArray(fromBeanList, toBean.class, ToBeanOption.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceList The source List object.
	 * @param targetClass The target object.
	 * @param optionClass The option class.
	 * @param <S> source template type.
	 * @param <T> target template type.
	 * @return If sourceList is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 */
	@SuppressWarnings("unchecked")
	public static <S, T> T[] copyArray(List<S> sourceList, Class<T> targetClass, Class<?> optionClass ) {
		if( sourceList == null )
			return null;
		BeanCopyDump.beginDump();
		T[] tArray = (T[]) Array.newInstance(targetClass, sourceList.size());
		int index = 0;
		if( PropertyUtils.isPrimitive(targetClass)) {
			for( S s : sourceList ) {
				Array.set(tArray, index++ , s);
			}
		} else {
			for( S s : sourceList ) {
				T newInst = (T) copyBean(s, targetClass, optionClass);
				tArray[index++] = newInst;
			}
		}
		BeanCopyDump.endDump();
		return tArray;
	}
	
	/**
	 * Copy property values from an array object to a new array object.
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   FromBean[] fromBeanArray = ...;
	 *   ToBean[] toBeanArray = BeastnCopyUtils.copyArray(fromBeanArray, toBean.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceArray The source List object.
	 * @param targetClass The target object.
	 * @param <S> source template type.
	 * @param <T> target template type.
	 * @return If sourceArray is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 */
	public static <S, T> T[] copyArray(S[] sourceArray, Class<T> targetClass ) {
		return copyArray( sourceArray, targetClass, null);
	}

	/**
	 * Copy property values from an array object to a new array object.
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   FromBean[] fromBeanArray = ...;
	 *   ToBean[] toBeanArray = BeastnCopyUtils.copyArray(fromBeanArray, toBean.class, ToBeanOption.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceArray The source List object.
	 * @param targetClass The target object.
	 * @param optionClass The option class.
	 * @param <S> source template type.
	 * @param <T> target template type.
	 * @return If sourceArray is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 */
	@SuppressWarnings("unchecked")
	public static <S, T> T[] copyArray(S[] sourceArray, Class<T> targetClass, Class<?> optionClass ) {
		if( sourceArray == null )
			return null;
		BeanCopyDump.beginDump();
		T[] tArray = (T[]) Array.newInstance(targetClass, sourceArray.length);
		int index = 0;
		if( PropertyUtils.isPrimitive(targetClass)) {
			for( S s : sourceArray ) {
				Array.set(tArray, index++ , s);
			}
		} else {
			for( S s : sourceArray ) {
				T newInst = (T) copyBean(s, targetClass, optionClass);
				tArray[index++] = newInst;
			}
		}
		BeanCopyDump.endDump();
		return tArray;
	}
	/**
	 * Copy property values from List object to a new Collection. Be default, the new Collection will be
	 *    List  =&gt; ArrayList <br>
	 *    Set   =&gt; HashSet <br>
	 *    Queue =&gt; ArrayDeque <br>
	 *    Deque =&gt; ArrayDeque <br>
	 * <br>
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   Set&lt;FromBean&gt; fromBeanCollection = ...;
	 *   Set&lt;ToBean&gt; = BeastnCopyUtils.copyCollection(fromBeanCollection, toBean.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceCollection The source collection object.
	 * @param targetClass The target object.
	 * @param <S> source template type.
	 * @param <T> target template type.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 */
	public static <S, T> Collection<T> copyCollection(Collection<S> sourceCollection, Class<T> targetClass ) {
		return copyCollection(sourceCollection, targetClass, null);
	}

	/**
	 * Copy property values from List object to a new Collection. Be default, the new Collection will be
	 *    List  =&gt; ArrayList <br>
	 *    Set   =&gt; HashSet <br>
	 *    Queue =&gt; ArrayDeque <br>
	 *    Deque =&gt; ArrayDeque <br>
	 * <br>
	 * <p>The following code snippet is an example of use of this function:</p>
	 * <pre>
	 *   Set&lt;FromBean&gt; fromBeanCollection = ...;
	 *   Set&lt;ToBean&gt; = BeastnCopyUtils.copyCollection(fromBeanCollection, toBean.class, ToBeanOption.class );
	 * </pre>
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceCollection The source collection object.
	 * @param targetClass The target object.
	 * @param optionClass The option class.
	 * @param <S> source template type.
	 * @param <T> target template type.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 */
	@SuppressWarnings("unchecked")
	public static <S, T> Collection<T> copyCollection(Collection<S> sourceCollection, Class<T> targetClass, Class<?> optionClass ) {
		
		if( sourceCollection == null )
			return null;
		BeanCopyDump.beginDump();
		Collection<T> dataList = (Collection<T>) InstanceUtils.newCollection(sourceCollection.getClass());
		for( S s : sourceCollection ) {
			T newInst = (T) copyBean(s, targetClass, optionClass);
			dataList.add(newInst);
		}
		BeanCopyDump.endDump();
		return dataList;
	}
	
	/**
	 * Dump the property mapping when do bean copy. The dump message will output to System.out if there has no
	 * org.apache.log4j.Logger imported, otherwise the message will output into log.
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceObject The source object.
	 * @param targetObject The target object.
	 */
	public static void dumpPropertyMapping(Object sourceObject, Object targetObject ) {
		BeanCopyDump.dumpPropertyMapping( sourceObject.getClass(), targetObject.getClass(), targetObject.getClass());
	}
	
	/**
	 * Dump the property mapping when do bean copy. The dump message will output to System.out if there has no
	 * org.apache.log4j.Logger imported, otherwise the message will output into log.
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceObject The source object.
	 * @param targetObject The target object.
	 * @param optionClass The option class.
	 */
	public static void dumpPropertyMapping(Object sourceObject, Object targetObject , Class<?> optionClass ) {
		BeanCopyDump.dumpPropertyMapping( sourceObject.getClass(), targetObject.getClass(), optionClass);
	}

	/**
	 * Dump the property mapping when do bean copy. The dump message will output to System.out if there has no
	 * org.apache.log4j.Logger imported, otherwise the message will output into log.
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceObject The source object.
	 * @param targetClass The target object.
	 */
	public static void dumpPropertyMapping(Object sourceObject, Class<?> targetClass ) {
		BeanCopyDump.dumpPropertyMapping(sourceObject.getClass(), targetClass, targetClass);
	}
	
	/**
	 * Dump the property mapping when do bean copy. The dump message will output to System.out if there has no
	 * org.apache.log4j.Logger imported, otherwise the message will output into log.
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceObject The source object.
	 * @param targetClass The target class.
	 * @param optionClass The option class.
	 */
	public static void dumpPropertyMapping(Object sourceObject, Class<?> targetClass , Class<?> optionClass ) {
		BeanCopyDump.dumpPropertyMapping(sourceObject.getClass(), targetClass, optionClass);
	}
	
	/**
	 * Dump the property mapping when do bean copy. The dump message will output to System.out if there has no
	 * org.apache.log4j.Logger imported, otherwise the message will output into log.
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceClass The source class.
	 * @param targetClass The target class.
	 */
	public static void dumpPropertyMapping(Class<?> sourceClass, Class<?> targetClass ) {
		BeanCopyDump.dumpPropertyMapping(sourceClass, targetClass, targetClass);
	}

	/**
	 * Dump the property mapping when do bean copy. The dump message will output to System.out if there has no
	 * org.apache.log4j.Logger imported, otherwise the message will output into log.
	 * <p>Note: </p>
	 * <p> BeanCopyException(RuntimeException) will be thrown if: </p>
	 * <pre> 1. The property type does not match.
	 *  2. Contains CopyCollection/CopyProperty annotation but not define BeanCopySource.
	 *  3. CopyCollection/CopyProperty defined property but the property cannot be found in sourceClass.
	 *  4. CopyCollection is used but the field is not a collection class.
	 *  5. One is array type but the other is not.
	 *  6. Property 'convertor' is defined in CopyProperty but the types do not match the source and target.
	 *  7. Property 'convertor' is defined but it is not BeanCopyConvertor type or it is still an interface type.
	 *  </pre>
	 * @param sourceClass The source class.
	 * @param targetClass The target class.
	 * @param optionClass The option class.
	 */
	public static void dumpPropertyMapping(Class<?> sourceClass, Class<?> targetClass , Class<?> optionClass ) {
		BeanCopyDump.dumpPropertyMapping(sourceClass, targetClass, optionClass);
	}

}
