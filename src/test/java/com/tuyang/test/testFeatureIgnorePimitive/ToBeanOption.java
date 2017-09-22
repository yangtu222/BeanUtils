package com.tuyang.test.testFeatureIgnorePimitive;

import com.tuyang.beanutils.annotation.BeanCopySource;
import com.tuyang.beanutils.annotation.CopyFeature;

@BeanCopySource(source=FromBean.class, features= {CopyFeature.IGNORE_PRIMITIVE_NULL_SOURCE_VALUE})
public class ToBeanOption {

}