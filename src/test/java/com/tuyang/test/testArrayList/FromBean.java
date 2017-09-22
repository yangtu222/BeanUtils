package com.tuyang.test.testArrayList;

import java.util.List;

public class FromBean {
	
	private boolean beanBool;
	private int beanInt;
	private String beanString;
	
	private int[] beanIntArray;
	private Long[] beanLongArray;
	private List<Float> beanFloatArray;
	private List<Double> beanDoubleArray;
	
	private FromBean2[] bean2s;

	private List<FromBean2> bean2sList;

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

	public List<Float> getBeanFloatArray() {
		return beanFloatArray;
	}

	public void setBeanFloatArray(List<Float> beanFloatArray) {
		this.beanFloatArray = beanFloatArray;
	}

	public List<Double> getBeanDoubleArray() {
		return beanDoubleArray;
	}

	public void setBeanDoubleArray(List<Double> beanDoubleArray) {
		this.beanDoubleArray = beanDoubleArray;
	}

	public FromBean2[] getBean2s() {
		return bean2s;
	}

	public void setBean2s(FromBean2[] bean2s) {
		this.bean2s = bean2s;
	}

	public List<FromBean2> getBean2sList() {
		return bean2sList;
	}

	public void setBean2sList(List<FromBean2> bean2sList) {
		this.bean2sList = bean2sList;
	}

}