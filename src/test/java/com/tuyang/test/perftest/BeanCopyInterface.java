package com.tuyang.test.perftest;

public interface BeanCopyInterface {

	String getMethodName();
	ToBean callCopyBean(FromBean fromBean)  throws Exception;

}
