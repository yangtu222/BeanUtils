package com.tuyang.test.testArray;

import com.tuyang.beanutils.annotation.BeanProperty;
import com.tuyang.beanutils.annotation.BeanPropertySource;

@BeanPropertySource(source=FromBean.class)
public class ToBean {

	private boolean beanBool;
	private int beanInt;
	private String beanString;
	
	private int[] beanIntArray;
	private Long[] beanLongArray;
	private Float[] beanFloatArray;
	private double[] beanDoubleArray;
	
	@BeanProperty(property="bean2s")
	private ToBean2[] toBeans;
	
	public boolean isBeanBool() {
		return beanBool;
	}
	public void setBeanBool(boolean beanBool) {
		this.beanBool = beanBool;
	}
	public int getBeanInt() {
		return beanInt;
	}
	public void setBeanInt(int beanInt) {
		this.beanInt = beanInt;
	}
	public String getBeanString() {
		return beanString;
	}
	public void setBeanString(String beanString) {
		this.beanString = beanString;
	}
	public int[] getBeanIntArray() {
		return beanIntArray;
	}
	public void setBeanIntArray(int[] beanIntArray) {
		this.beanIntArray = beanIntArray;
	}
	public Long[] getBeanLongArray() {
		return beanLongArray;
	}
	public void setBeanLongArray(Long[] beanLongArray) {
		this.beanLongArray = beanLongArray;
	}
	public Float[] getBeanFloatArray() {
		return beanFloatArray;
	}
	public void setBeanFloatArray(Float[] beanFloatArray) {
		this.beanFloatArray = beanFloatArray;
	}
	public double[] getBeanDoubleArray() {
		return beanDoubleArray;
	}
	public void setBeanDoubleArray(double[] beanDoubleArray) {
		this.beanDoubleArray = beanDoubleArray;
	}
	public ToBean2[] getToBeans() {
		return toBeans;
	}
	public void setToBeans(ToBean2[] toBeans) {
		this.toBeans = toBeans;
	}


}