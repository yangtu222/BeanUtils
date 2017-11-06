package com.tuyang.test.testEnum;

import static org.junit.Assert.*;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class Test08 {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();

		fromBean.setMyEnum1(MyEnum.One);
		fromBean.setMyEnum2(MyEnum.Two);
		fromBean.setEnumString(MyEnum.Three.toString());
		
		MyEnum[] array = new MyEnum[2];
		array[0]= MyEnum.One;
		array[1] = MyEnum.Three;
		fromBean.setMyEnums1(array);
		fromBean.setMyEnums2(array);
		fromBean.setMyEnums3(Arrays.asList(array));
		
		String[] enumStrings = new String[2];
		enumStrings[0] = MyEnum.Three.toString();
		enumStrings[1] = MyEnum.Two.toString();
		
		fromBean.setEnumStrings(enumStrings);
		fromBean.seteStrings(Arrays.asList(enumStrings));
		
		return fromBean;
	}

	@Test
	public void testEnum() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);

		assertEquals(toBean.getMyEnum1(), MyEnum.One);
		assertEquals(toBean.getMyEnum2(), MyEnum.Two.toString());
		assertEquals(toBean.getEnumString(), MyEnum.Three);
		assertEquals(toBean.getMyEnums1()[1], MyEnum.Three);
		assertEquals(toBean.getMyEnums2()[1], MyEnum.Three.toString());
		assertEquals(toBean.getMyEnums3().get(1), MyEnum.Three);
		assertEquals(toBean.geteStrings().get(1), MyEnum.Two);
	}
}

