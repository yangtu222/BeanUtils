package com.tuyang.test.testEnum2;

import java.util.List;

public class FromBean {
	
	private MyEnum myEnum1;
	private MyEnum myEnum2;
	
	private String enumString;
	
	private MyEnum[] myEnums1;
	private MyEnum[] myEnums2;

	private String[] enumStrings;
	
	private List<MyEnum> myEnums3;
	
	private List<String> eStrings;
	
	private Inside inside;

	public Inside getInside() {
		return inside;
	}

	public void setInside(Inside inside) {
		this.inside = inside;
	}

	public MyEnum getMyEnum1() {
		return myEnum1;
	}

	public void setMyEnum1(MyEnum myEnum1) {
		this.myEnum1 = myEnum1;
	}

	public MyEnum getMyEnum2() {
		return myEnum2;
	}

	public void setMyEnum2(MyEnum myEnum2) {
		this.myEnum2 = myEnum2;
	}

	public String getEnumString() {
		return enumString;
	}

	public void setEnumString(String enumString) {
		this.enumString = enumString;
	}

	public MyEnum[] getMyEnums1() {
		return myEnums1;
	}

	public void setMyEnums1(MyEnum[] myEnums1) {
		this.myEnums1 = myEnums1;
	}

	public MyEnum[] getMyEnums2() {
		return myEnums2;
	}

	public void setMyEnums2(MyEnum[] myEnums2) {
		this.myEnums2 = myEnums2;
	}

	public String[] getEnumStrings() {
		return enumStrings;
	}

	public void setEnumStrings(String[] enumStrings) {
		this.enumStrings = enumStrings;
	}

	public List<MyEnum> getMyEnums3() {
		return myEnums3;
	}

	public void setMyEnums3(List<MyEnum> myEnums3) {
		this.myEnums3 = myEnums3;
	}

	public List<String> geteStrings() {
		return eStrings;
	}

	public void seteStrings(List<String> eStrings) {
		this.eStrings = eStrings;
	}



}
