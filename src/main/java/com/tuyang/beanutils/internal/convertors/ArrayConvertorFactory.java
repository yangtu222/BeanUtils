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

import com.tuyang.beanutils.BeanCopyConvertor;

public class ArrayConvertorFactory {

	@SuppressWarnings("rawtypes")
	public static BeanCopyConvertor getArrayConvertor(Class<?> methodSourceType, Class<?> methodTargetType, Class<?> propertyOptionClass) {
		
		if( !(methodSourceType.isPrimitive() || methodTargetType.isPrimitive() ) ) {
			return new ObjectArrayConvertor(methodSourceType, methodTargetType, propertyOptionClass);
		}
		
		if( int.class.equals(methodSourceType) ) {
			return new IntArrayConvertor();
		} else if( boolean.class.equals(methodSourceType) ) {
			return new BoolArrayConvertor();
		} else if( byte.class.equals(methodSourceType) ) {
			return new ByteArrayConvertor();
		} else if( char.class.equals(methodSourceType) ) {
			return new CharArrayConvertor();
		} else if( short.class.equals(methodSourceType) ) {
			return new ShortArrayConvertor();
		} else if( long.class.equals(methodSourceType) ) {
			return new LongArrayConvertor();
		} else if( float.class.equals(methodSourceType) ) {
			return new FloatArrayConvertor();
		} else if( double.class.equals(methodSourceType) ) {
			return new DoubleArrayConvertor();
		} else if( Integer.class.equals(methodSourceType) ) {
			return new IntObjectArrayConvertor();
		} else if( Boolean.class.equals(methodSourceType) ) {
			return new BoolObjectArrayConvertor();
		} else if( Character.class.equals(methodSourceType) ) {
			return new CharObjectArrayConvertor();
		} else if( Byte.class.equals(methodSourceType) ) {
			return new ByteObjectArrayConvertor();
		} else if( Short.class.equals(methodSourceType) ) {
			return new ShortObjectArrayConvertor();
		} else if( Long.class.equals(methodSourceType) ) {
			return new LongObjectArrayConvertor();
		} else if( Float.class.equals(methodSourceType) ) {
			return new FloatObjectArrayConvertor();
		} else if( Double.class.equals(methodSourceType) ) {
			return new DoubleObjectArrayConvertor();
		}
		
		return null;
		
	}
	
}
