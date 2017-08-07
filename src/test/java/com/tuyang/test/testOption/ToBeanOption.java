package com.tuyang.test.testOption;

import com.tuyang.beanutils.annotation.CopyProperty;
import com.tuyang.beanutils.annotation.BeanCopySource;

@BeanCopySource(source=FromBean.class)
public class ToBeanOption {

	@CopyProperty(property="beanInt")
	private int toBeanInt;
	@CopyProperty(property="beanString")
	private String toBeanString;
	

}