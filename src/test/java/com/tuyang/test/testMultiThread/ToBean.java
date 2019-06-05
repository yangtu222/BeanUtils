package com.tuyang.test.testMultiThread;

import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyProperty;

@BeanCopySource(source=FromBean.class)
public class ToBean {

	@CopyProperty(property="id", convertor=StringToIntegerConverter.class)
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}