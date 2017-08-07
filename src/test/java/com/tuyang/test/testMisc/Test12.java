package com.tuyang.test.testMisc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class Test12 {
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanBool(true);
		fromBean.setBeanByte((byte)5);
		fromBean.setBeanChar((char)40);
		fromBean.setBeanShort((short)50);
		fromBean.setBeanInt(100);
		fromBean.setBeanFloat(100.50f);
		fromBean.setBeanLong(234323243243243234L);
		fromBean.setBeanDouble(2342332423.23432432523523);
		fromBean.setBeanString("Test test Test test.");
		
		FromBean2 bean2 = new FromBean2();
		bean2.setBeanFloat(20.5f);
		bean2.setBeanString("bean2");
		fromBean.setBean2(bean2);
		
		fromBean.setBeanDate(new Date());
		fromBean.setBeanIntArray(new int[] {1,2,3});
		
		FromBean3 bean3 = new FromBean3();
		bean3.setBeanFloat(152.654f);
		bean3.setBeanString("bean3");
		
		List<FromBean3> bean3List = new ArrayList<>();
		bean3List.add(bean3);
		bean3List.add(bean3);
		bean3List.add(bean3);

		fromBean.setBean3List(bean3List);
		
		FromBean4[] bean4Array = new FromBean4[3];
		FromBean4 bean4 = new FromBean4();
		bean4.setBeanFloat(789.2f);
		bean4.setBeanString("bean4");
		
		bean4Array[0] = bean4;
		bean4Array[1] = bean4;
		bean4Array[2] = bean4;
		
		fromBean.setBean4Array(bean4Array);
		
		
		return fromBean;
	}

	@Test
	public void testBasic() {
		FromBean fromBean = getFromBean();
		ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
		assertEquals(fromBean.isBeanBool(), toBean.getBeanBool().booleanValue() );
		assertEquals( fromBean.getBeanByte(), toBean.getBeanByte().byteValue() );
		assertEquals( fromBean.getBeanChar(), toBean.getBeanChar().charValue() );
		assertEquals( fromBean.getBeanShort(), toBean.getBeanShort().shortValue() );
		assertEquals( fromBean.getBeanInt(), toBean.getBeanInt().intValue() );
		assertEquals( fromBean.getBeanLong().longValue(), toBean.getBeanLong() );
		assertEquals( fromBean.getBeanFloat().floatValue(), toBean.getBeanFloat() , 0);
		assertEquals( fromBean.getBeanDouble().doubleValue(), toBean.getBeanDouble(), 0 );
		assertEquals( fromBean.getBeanString(), toBean.getBeanString() );
		
		assertArrayEquals(fromBean.getBeanIntArray(), toBean.getBeanIntArray());
		
		assertEquals(fromBean.getBean2().getBeanFloat().floatValue(), toBean.getBean2().getBeanFloat().floatValue(), 0);
		
		assertNotNull(toBean.getBeanDate());
		assertEquals(fromBean.getBean3List().get(1).getBeanFloat().floatValue(), toBean.getBean3List().get(1).getBeanFloat().floatValue(), 0);
		
		assertEquals(fromBean.getBean4Array()[1].getBeanFloat().floatValue(), toBean.getBean4Array()[1].getBeanFloat().floatValue(), 0);
	}
	
}

