
package com.tuyang.test.testMultiThread;

import com.tuyang.beanutils.BeanCopyConvertor;

public class StringToIntegerConverter implements BeanCopyConvertor<String,Integer> {

    @Override
    public Integer convertTo(String object) {
        return Integer.parseInt(object);
    }
}
