package com.tuyang.test.testInheritance2;

import com.tuyang.beanutils.annotation.CopyProperty;
import com.tuyang.beanutils.annotation.BeanCopySource;

@BeanCopySource(source=FromBean.class)
public class ToBeanOption {

	@CopyProperty(property="beanInt")
	private int toBeanInt;
	@CopyProperty(property="beanString")
	private String toBeanString;
	@CopyProperty(property="baseBool")
	private boolean toBaseBool;
	@CopyProperty(property="baseByte")
	private byte toBaseByte;
	@CopyProperty(property="baseChar")
	private char toBaseChar;
	@CopyProperty(property="baseShort")
	private short toBaseShort;
	@CopyProperty(property="baseInt")
	private int toBaseInt;
	@CopyProperty(property="baseLong")
	private long toBaseLong;
	@CopyProperty(property="baseFloat")
	private float toBaseFloat;
	@CopyProperty(property="baseDouble")
	private double toBaseDouble;
	@CopyProperty(property="baseString")
	private String toBaseString;

}