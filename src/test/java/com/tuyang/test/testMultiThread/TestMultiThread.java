package com.tuyang.test.testMultiThread;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class TestMultiThread {
	
	@Test
	public void testMultiThread() {
		List<Thread> threads = new ArrayList<>();
		for (int i=0;i<10;i++){
			threads.add(new Thread(()->{
				FromBean fromBean = new FromBean();
				fromBean.setId("1");
				ToBean toBean  = BeanCopyUtils.copyBean(fromBean, ToBean.class);
				System.out.println(toBean.getId());
			} ));
		}
		threads.forEach(e->e.start());
		threads.forEach(e-> {
			try {
				e.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		});
	}
}
