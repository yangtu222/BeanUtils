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

package com.tuyang.beanutils.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
@Documented
public @interface CopyProperty {
	
	/**
	 * The property name in source object that want to be copied from.
	 * <p>Note: </p>
	 * <pre> "" (empty string, or null): means the property name is same to source object. 
	 * a.b: means the source value is coming from source object's property a's property b. </pre>
	 * @return the property name which will be copied from.
	 */
	String property() default "";
	
	/**
	 * Specify this property will be ignored when do bean copy.
	 * @return true is ignored.
	 */
	boolean ignored() default false;
	
	/**
	 * The data convertor of this property. The convertor must be BeanCopyConvertor type.
	 * @return convertor's class type.
	 * 
	 * @see com.tuyang.beanutils.BeanCopyConvertor
	 */
	Class<?> convertor() default void.class ;
	
	/**
	 * Specify the optionClass when coping when this property is a Java bean class.
	 * @return option class.
	 */
	Class<?> optionClass() default void.class;
}
