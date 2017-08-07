package com.tuyang.test.perftest;

public class LoopTest {

	private int count;
	
	public LoopTest( int count) {
		this.count = count;
		System.out.println("==================================");
		System.out.println(String.format("Run Test for %d times:", count));
	}
	
	public ToBean run(BeanCopyInterface method, FromBean fromBean ) {
		try {
			ToBean toBean = null;
			String methodName = method.getMethodName();
			long begin = System.currentTimeMillis();
			
			for (int i = 0; i < count; i++) {
				toBean = method.callCopyBean(fromBean);
			}
			long end = System.currentTimeMillis();
			System.out.println(String.format("Run %s: %d ms", methodName, end - begin));
			return toBean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
