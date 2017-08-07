package com.tuyang.test.testOption;

import com.tuyang.beanutils.annotation.BeanProperty;
import com.tuyang.beanutils.annotation.BeanPropertySource;

@BeanPropertySource(source=FromBean.class)
public class ToBeanOption {

	@BeanProperty(property="beanInt")
	private int toBeanInt;
	@BeanProperty(property="beanString")
	private String toBeanString;
	

}