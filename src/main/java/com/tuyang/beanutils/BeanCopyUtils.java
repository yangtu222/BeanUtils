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
import com.tuyang.beanutils.internal.utils.InstanceUtils;

/**
 * Static convenience methods for JavaBeans: copy source JavaBean properties to target JavaBean properties. 
 *
 */
public class BeanCopyUtils {
	
	/**
	 * Get the BeanCopier instance's interface for Bean Copy.
	 * <p>The following code snippet is an example of use of this class:
	 *
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   BeanCopier copier = BeanCopyUtils.getBeanCopier( FromBean.class, ToBean.class );
	 *   ToBean toBean = new ToBean();
	 *   copier(fromBean, toBean);
	 * </pre>
	 * </p>
	 * 
	 * @param sourceClass The source object class.
	 * @param targetClass the target object class.
	 * @return the BeanCopier instance.
	 * @throws BeanCopyException if the property type does not match in sourceClass and targetClass, or Annotation
	 *  is not used correctly.
	 * 
	 * @see com.tuyang.beanutils.BeanCopier
	 */
	public static BeanCopier getBeanCopier( Class<?> sourceClass, Class<?> targetClass ) {
		return getBeanCopier(sourceClass, targetClass, null);
	}
	
	/**
	 * Get the BeanCopier instance's interface for Bean Copy.
	 * <p>The following code snippet is an example of use of this function:
	 *
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   BeanCopier copier = BeanCopyUtils.getBeanCopier( FromBean.class, ToBean.class, ToBeanOption.class );
	 *   ToBean toBean = new ToBean();
	 *   copier(fromBean, toBean);
	 * </pre>
	 * </p>
	 * 
	 * @param sourceClass The source object class.
	 * @param targetClass the target object class.
	 * @param optionClass The option class.
	 * @return the BeanCopier instance.
	 * @throws BeanCopyException if the property type does not match in sourceClass and targetClass, or Annotation
	 *  is not used correctly.
	 * 
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
	 * <p>The following code snippet is an example of use of this function:
	 *
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
	 * </pre>
	 * </p>
	 * 
	 * @param sourceObject The source object.
	 * @param targetClass The target object class.
	 * @return If sourceObject is null, then return null, otherwise return targetClass instance with the property 
	 * values been copied to. 
	 * @throws BeanCopyException if the property type does not match in sourceClass and targetClass, or Annotation
	 *  is not used correctly.
	 * @throws NullPointerException if targetClass is null.
	 */
	public static <T> T copyBean( Object sourceObject, Class<T> targetClass ) {
		return copyBean(sourceObject, targetClass, null);
	}
	
	/**
	 * Copy property values from sourceObject to targetObject. 
	 * <p>The following code snippet is an example of use of this function:
	 *
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   ToBean toBean = ...;
	 *   toBean = (ToBean) BeanCopyUtils.copyBean(fromBean, toBean );
	 * </pre>
	 * </p>
	 * 
	 * @param sourceObject The source object.
	 * @param targetClass The target object.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return targetClass instance 
	 * with the property values been copied to. 
	 * @throws BeanCopyException if the property type does not match in sourceClass and targetClass, or Annotation
	 *  is not used correctly.
	 * @throws NullPointerException if sourceObject or targetObject is null.
	 */
	public static Object copyBean( Object sourceObject, Object targetObject ) {
		return copyBean(sourceObject, targetObject, null);
	}
	
	/**
	 * New a targetClass instance and copy property values from sourceObject to targetObject. 
	 * <p>The following code snippet is an example of use of this function:
	 *
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class, ToBeanOption.class );
	 * </pre>
	 * </p>
	 * 
	 * @param sourceObject The source object.
	 * @param targetClass The target object.
	 * @param optionClass The option class.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return targetClass instance 
	 * with the property values been copied to. 
	 * @throws BeanCopyException if the property type does not match in sourceClass and targetClass, or Annotation
	 *  is not used correctly.
	 * @throws NullPointerException if targetClass is null.
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
	 * <p>The following code snippet is an example of use of this function:
	 *
	 * <pre>
	 *   FromBean fromBean = ...;
	 *   ToBean toBean = ...;
	 *   toBean = (ToBean) BeanCopyUtils.copyBean(fromBean, toBean, ToBeanOption.class );
	 * </pre>
	 * </p>
	 * 
	 * @param sourceObject The source object.
	 * @param targetClass The target object.
	 * @param optionClass The option class.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return targetClass instance 
	 * with the property values been copied to. 
	 * @throws BeanCopyException if the property type does not match in sourceClass and targetClass, or Annotation
	 *  is not used correctly.
	 * @throws NullPointerException if targetClass is null.
	 */
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
	
	/**
	 * Copy property values from List object to a new List. Be default, the new List is ArrayList.
	 * <p>The following code snippet is an example of use of this function:
	 *
	 * <pre>
	 *   List<FromBean> fromBeanList = ...;
	 *   List<ToBean> = BeastnCopyUtils.copyList(fromBeanList, toBean.class );
	 * </pre>
	 * </p>
	 * 
	 * @param sourceList The source List object.
	 * @param targetClass The target object.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 * @throws BeanCopyException if the property type does not match in sourceClass and targetClass, or Annotation
	 *  is not used correctly.
	 * @throws NullPointerException if sourceObject or targetObject is null.
	 */
	public static <R, T> List<R> copyList(List<T> sourceList, Class<R> targetClass ) {
		return (List<R>) copyCollection(sourceList, targetClass, null);
	}
	
	/**
	 * Copy property values from List object to a new List. Be default, the new List is ArrayList.
	 * <p>The following code snippet is an example of use of this function:
	 *
	 * <pre>
	 *   List<FromBean> fromBeanList = ...;
	 *   List<ToBean> = BeastnCopyUtils.copyList(fromBeanList, toBean.class );
	 * </pre>
	 * </p>
	 * 
	 * @param sourceList The source List object.
	 * @param targetClass The target object.
	 * @param optionClass The option class.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 * @throws BeanCopyException if the property type does not match in sourceClass and targetClass, or Annotation
	 *  is not used correctly, or the new List cannot be created.
	 * @throws NullPointerException if targetObject is null.
	 */
	public static <R, T> List<R> copyList(List<T> sourceList, Class<R> targetClass, Class<?> optionClass ) {
		return (List<R>) copyCollection(sourceList, targetClass, optionClass);
	}
	
	/**
	 * Copy property values from List object to a new Collection. Be default, the new Collection will be
	 * <p>   List  => ArrayList </p>
	 * <p>   Set   => HashSet </p>
	 * <p>   Queue => ArrayDeque </p>
	 * <p>   Deque => ArrayDeque </p>
	 * <p></p>
	 * <p>The following code snippet is an example of use of this function:
	 *
	 * <pre>
	 *   Set<FromBean> fromBeanCollection = ...;
	 *   Set<ToBean> = BeastnCopyUtils.copyCollection(fromBeanCollection, toBean.class );
	 * </pre>
	 * </p>
	 * 
	 * @param sourceList The source List object.
	 * @param targetClass The target object.
	 * @param optionClass The option class.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 * @throws BeanCopyException if the property type does not match in sourceClass and targetClass, or Annotation
	 *  is not used correctly, or the new List cannot be created.
	 * @throws NullPointerException if targetObject is null.
	 */
	public static <R, T> Collection<R> copyCollection(Collection<T> sourceCollection, Class<R> targetClass ) {
		return copyCollection(sourceCollection, targetClass, null);
	}

	/**
	 * Copy property values from List object to a new Collection. Be default, the new Collection will be
	 * <p>   List  => ArrayList </p>
	 * <p>   Set   => HashSet </p>
	 * <p>   Queue => ArrayDeque </p>
	 * <p>   Deque => ArrayDeque </p>
	 * <p></p>
	 * <p>The following code snippet is an example of use of this function:
	 *
	 * <pre>
	 *   Set<FromBean> fromBeanCollection = ...;
	 *   Set<ToBean> = BeastnCopyUtils.copyList(fromBeanCollection, toBean.class, ToBeanOption.class );
	 * </pre>
	 * </p>
	 * 
	 * @param sourceList The source List object.
	 * @param targetClass The target object.
	 * @param optionClass The option class.
	 * @return If sourceObject is null, it will throw NullPointerException, otherwise return a list with item as 
	 * targetClass instance that the property values are been copied to. 
	 * @throws BeanCopyException if the property type does not match in sourceClass and targetClass, or Annotation
	 *  is not used correctly, or the new List cannot be created.
	 * @throws NullPointerException if targetObject is null.
	 */
	@SuppressWarnings("unchecked")
	public static <R, T> Collection<R> copyCollection(Collection<T> sourceCollection, Class<R> targetClass, Class<?> optionClass ) {
		
		if( sourceCollection == null )
			return null;
		
		Collection<R> dataList = (Collection<R>) InstanceUtils.newCollection(sourceCollection.getClass());
		for( T t : sourceCollection ) {
			R newInst = (R) copyBean(t, targetClass, optionClass);
			dataList.add(newInst);
		}
		return dataList;
	}
}
