package com.tuyang.test.testMultiThread;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.tuyang.beanutils.BeanCopyUtils;

public class TestMultiThread {
	
	@Test
	public void testMultiThread() {
		List<Thread> threads = new ArrayList<>();
		for (int i=0;i<10;i++){
			Thread thread = new Thread( new Runnable() {
				@Override
				public void run() {
					FromBean fromBean = new FromBean();
					fromBean.setId("1");
					ToBean toBean  = BeanCopyUtils.copyBean(fromBean, ToBean.class);
					System.out.println(toBean.getId());
				}
			});
			threads.add(thread);
		}
		Iterator<Thread> iterator = threads.iterator();
		while( iterator.hasNext() ) {
			iterator.next().start();
		}
		iterator = threads.iterator();
		while( iterator.hasNext() ) {
			try {
				iterator.next().join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
