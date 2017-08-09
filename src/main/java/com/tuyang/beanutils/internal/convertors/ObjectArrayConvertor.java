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

package com.tuyang.beanutils.internal.convertors;

import java.lang.reflect.Array;

import com.tuyang.beanutils.BeanCopyConvertor;
import com.tuyang.beanutils.BeanCopyUtils;

@SuppressWarnings("rawtypes")
public class ObjectArrayConvertor implements BeanCopyConvertor {

	private Class<?> methodTargetType;
	private Class<?> methodSourceType;
	private Class<?> propertyOptionClass;
	
	
	public ObjectArrayConvertor( Class<?> methodSourceType, Class<?> methodTargetType, Class<?> propertyOptionClass) {
		this.methodSourceType = methodSourceType;
		this.methodTargetType = methodTargetType;
		this.propertyOptionClass = propertyOptionClass;
	}

	@Override
	public Object convertTo(Object object) {
		if( !object.getClass().isArray() )
			return null;
		if( !methodSourceType.equals(object.getClass().getComponentType()))
			return null;
		int length = Array.getLength(object);
		Object[] retObject = (Object[]) Array.newInstance(methodTargetType, length );
		for( int i =0; i<length; i++ ) {
			retObject[i] = BeanCopyUtils.copyBean(Array.get(object, i), methodTargetType, propertyOptionClass);
		}
		return retObject;
	}

}
