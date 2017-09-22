package com.tuyang.test.testArrayList;

import com.tuyang.beanutils.annotation.CopyProperty;

import java.util.List;

import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyCollection;

@BeanCopySource(source=FromBean.class)
public class ToBean {

	private boolean beanBool;
	private int beanInt;
	private String beanString;
	
	@CopyCollection(targetClass=Integer.class)
	private List<Integer> beanIntArray;
	@CopyCollection(targetClass=Long.class)
	private List<Long> beanLongArray;
	@CopyProperty
	private Float[] beanFloatArray;
	@CopyProperty
	private double[] beanDoubleArray;
	
	@CopyCollection(property="bean2s", targetClass=ToBean2.class)
	private List<ToBean2> toBeans;
	
	@CopyProperty(property="bean2sList")
	private ToBean2[] bean2sList;

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

	public List<Integer> getBeanIntArray() {
		return beanIntArray;
	}

	public void setBeanIntArray(List<Integer> beanIntArray) {
		this.beanIntArray = beanIntArray;
	}

	public List<Long> getBeanLongArray() {
		return beanLongArray;
	}

	public void setBeanLongArray(List<Long> beanLongArray) {
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

	public List<ToBean2> getToBeans() {
		return toBeans;
	}

	public void setToBeans(List<ToBean2> toBeans) {
		this.toBeans = toBeans;
	}

	public ToBean2[] getBean2sList() {
		return bean2sList;
	}

	public void setBean2sList(ToBean2[] bean2sList) {
		this.bean2sList = bean2sList;
	}
}