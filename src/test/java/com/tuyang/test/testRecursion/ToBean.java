package com.tuyang.test.testRecursion;

import com.tuyang.beanutils.annotation.CopyProperty;
import com.tuyang.beanutils.annotation.BeanCopySource;

@BeanCopySource(source=FromBean.class)
public class ToBean {

	private Integer beanInt;
	private long beanLong;
	private String beanString;
	
	@CopyProperty(property="bean2.beanInt")
	private int bean2Int;
	@CopyProperty(property="bean2.beanLong")
	private Long bean2Long;
	@CopyProperty(property="bean2.beanString")
	private String bean2Str;

	@CopyProperty(property="bean2.bean3.bean4.bean5.bean6.bean7.bean8.beanFloat")
	private float bean8Float;
	
	@CopyProperty(property="bean2.bean3.bean4.bean5.bean6.bean7.bean8.beanString")
	private String bean8String;

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

	public String getBeanString() {
		return beanString;
	}

	public void setBeanString(String beanString) {
		this.beanString = beanString;
	}

	public int getBean2Int() {
		return bean2Int;
	}

	public void setBean2Int(int bean2Int) {
		this.bean2Int = bean2Int;
	}

	public Long getBean2Long() {
		return bean2Long;
	}

	public void setBean2Long(Long bean2Long) {
		this.bean2Long = bean2Long;
	}

	public String getBean2Str() {
		return bean2Str;
	}

	public void setBean2Str(String bean2Str) {
		this.bean2Str = bean2Str;
	}

	public float getBean8Float() {
		return bean8Float;
	}

	public void setBean8Float(float bean8Float) {
		this.bean8Float = bean8Float;
	}

	public String getBean8String() {
		return bean8String;
	}

	public void setBean8String(String bean8String) {
		this.bean8String = bean8String;
	}
	


}