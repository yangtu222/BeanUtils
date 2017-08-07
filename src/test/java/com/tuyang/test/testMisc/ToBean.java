package com.tuyang.test.testMisc;

import java.util.List;

import com.tuyang.beanutils.annotation.CopyProperty;
import com.tuyang.beanutils.annotation.CopyCollection;
import com.tuyang.beanutils.annotation.BeanCopySource;

@BeanCopySource(source=FromBean.class)
public class ToBean {
	
	private Boolean beanBool;
	private Byte beanByte;
	private Character beanChar;
	private Short beanShort;
	private Integer beanInt;
	private long beanLong;
	private float beanFloat;
	private double beanDouble;
	private String beanString;
	
	@CopyProperty(convertor=DateConvertor.class)
	private String beanDate;
	
	private int[] beanIntArray;

	@CopyProperty
	private ToBean2 bean2;
	
	@CopyCollection(targetClass=ToBean3.class)
	private List<ToBean3> bean3List;
	
	@CopyProperty
	private ToBean4[] bean4Array;
	
	@CopyProperty(property="beanInt")
	private int beanId;
	
	@CopyProperty(property="bean2.beanString")
	private String bean2String;

	public int getBeanId() {
		return beanId;
	}
	public void setBeanId(int beanId) {
		this.beanId = beanId;
	}
	public String getBean2String() {
		return bean2String;
	}
	public void setBean2String(String bean2String) {
		this.bean2String = bean2String;
	}
	public Boolean getBeanBool() {
		return beanBool;
	}
	public void setBeanBool(Boolean beanBool) {
		this.beanBool = beanBool;
	}
	public Byte getBeanByte() {
		return beanByte;
	}
	public void setBeanByte(Byte beanByte) {
		this.beanByte = beanByte;
	}
	public Character getBeanChar() {
		return beanChar;
	}
	public void setBeanChar(Character beanChar) {
		this.beanChar = beanChar;
	}
	public Short getBeanShort() {
		return beanShort;
	}
	public void setBeanShort(Short beanShort) {
		this.beanShort = beanShort;
	}
	public Integer getBeanInt() {
		return beanInt;
	}
	public void setBeanInt(Integer beanInt) {
		this.beanInt = beanInt;
	}
	public long getBeanLong() {
		return beanLong;
	}
	public void setBeanLong(long beanLong) {
		this.beanLong = beanLong;
	}
	public float getBeanFloat() {
		return beanFloat;
	}
	public void setBeanFloat(float beanFloat) {
		this.beanFloat = beanFloat;
	}
	public double getBeanDouble() {
		return beanDouble;
	}
	public void setBeanDouble(double beanDouble) {
		this.beanDouble = beanDouble;
	}
	public String getBeanString() {
		return beanString;
	}
	public void setBeanString(String beanString) {
		this.beanString = beanString;
	}
	public String getBeanDate() {
		return beanDate;
	}
	public void setBeanDate(String beanDate) {
		this.beanDate = beanDate;
	}
	public int[] getBeanIntArray() {
		return beanIntArray;
	}
	public void setBeanIntArray(int[] beanIntArray) {
		this.beanIntArray = beanIntArray;
	}
	public ToBean2 getBean2() {
		return bean2;
	}
	public void setBean2(ToBean2 bean2) {
		this.bean2 = bean2;
	}
	public List<ToBean3> getBean3List() {
		return bean3List;
	}
	public void setBean3List(List<ToBean3> bean3List) {
		this.bean3List = bean3List;
	}
	public ToBean4[] getBean4Array() {
		return bean4Array;
	}
	public void setBean4Array(ToBean4[] bean4Array) {
		this.bean4Array = bean4Array;
	}
	
}