package com.tuyang.test.testMisc;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.tuyang.beanutils.BeanCopyConvertor;

public class DateConvertor extends BeanCopyConvertor<Date, String> {

	@Override
	public String convertTo(Date object) {
		if( object == null )
			object = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(object);
	}

}
