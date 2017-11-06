package com.tuyang.test.testEnum;

import com.tuyang.beanutils.annotation.CopyProperty;

import java.util.List;

import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyCollection;

@BeanCopySource(source=FromBean.class)
public class ToBean {

	private MyEnum myEnum1;
	
	@CopyProperty
	private String myEnum2;
	
	@CopyProperty
	private MyEnum enumString;
	
	@CopyProperty
	private MyEnum[] myEnums1;
	
	@CopyProperty
	private String[] myEnums2;

	@CopyProperty
	private MyEnum[] enumStrings;
	
	@CopyCollection(targetClass = MyEnum.class)
	private List<MyEnum> myEnums3;
	
	@CopyCollection(targetClass= MyEnum.class)
	private List<MyEnum> eStrings;

	public MyEnum getMyEnum1() {
		return myEnum1;
	}

	public void setMyEnum1(MyEnum myEnum1) {
		this.myEnum1 = myEnum1;
	}

	public String getMyEnum2() {
		return myEnum2;
	}

	public void setMyEnum2(String myEnum2) {
		this.myEnum2 = myEnum2;
	}

	public MyEnum getEnumString() {
		return enumString;
	}

	public void setEnumString(MyEnum enumString) {
		this.enumString = enumString;
	}

	public MyEnum[] getMyEnums1() {
		return myEnums1;
	}

	public void setMyEnums1(MyEnum[] myEnums1) {
		this.myEnums1 = myEnums1;
	}

	public String[] getMyEnums2() {
		return myEnums2;
	}

	public void setMyEnums2(String[] myEnums2) {
		this.myEnums2 = myEnums2;
	}

	public MyEnum[] getEnumStrings() {
		return enumStrings;
	}

	public void setEnumStrings(MyEnum[] enumStrings) {
		this.enumStrings = enumStrings;
	}

	public List<MyEnum> getMyEnums3() {
		return myEnums3;
	}

	public void setMyEnums3(List<MyEnum> myEnums3) {
		this.myEnums3 = myEnums3;
	}

	public List<MyEnum> geteStrings() {
		return eStrings;
	}

	public void seteStrings(List<MyEnum> eStrings) {
		this.eStrings = eStrings;
	}

}