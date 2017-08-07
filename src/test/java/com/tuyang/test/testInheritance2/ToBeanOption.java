package com.tuyang.test.testInheritance2;

import com.tuyang.beanutils.annotation.BeanProperty;
import com.tuyang.beanutils.annotation.BeanPropertySource;

@BeanPropertySource(source=FromBean.class)
public class ToBeanOption {

	@BeanProperty(property="beanInt")
	private int toBeanInt;
	@BeanProperty(property="beanString")
	private String toBeanString;
	@BeanProperty(property="baseBool")
	private boolean toBaseBool;
	@BeanProperty(property="baseByte")
	private byte toBaseByte;
	@BeanProperty(property="baseChar")
	private char toBaseChar;
	@BeanProperty(property="baseShort")
	private short toBaseShort;
	@BeanProperty(property="baseInt")
	private int toBaseInt;
	@BeanProperty(property="baseLong")
	private long toBaseLong;
	@BeanProperty(property="baseFloat")
	private float toBaseFloat;
	@BeanProperty(property="baseDouble")
	private double toBaseDouble;
	@BeanProperty(property="baseString")
	private String toBaseString;

}