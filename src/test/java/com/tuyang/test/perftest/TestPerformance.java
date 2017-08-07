package com.tuyang.test.perftest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Test;
import org.slf4j.impl.StaticLoggerBinder;
import org.springframework.cglib.beans.BeanCopier;

import com.tuyang.beanutils.BeanCopyUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class TestPerformance {
	
	static {
		StaticLoggerBinder binder = StaticLoggerBinder.getSingleton();
		LoggerContext context = (LoggerContext) binder.getLoggerFactory();
		context.getLogger("ROOT").setLevel(Level.INFO);
	}
	
	private FromBean getFromBean() {
		FromBean fromBean = new FromBean();
		fromBean.setBeanBool(true);
		fromBean.setBeanByte((byte)5);
		fromBean.setBeanChar((char)40);
		fromBean.setBeanShort((short)50);
		fromBean.setBeanInt(100);
		fromBean.setBeanFloat(100.50f);
		fromBean.setBeanLong(234323243243243234L);
		fromBean.setBeanDouble(2342332423.23432432523523);
		fromBean.setBeanString("Test test Test test.");
		return fromBean;
	}

	@Test
	public void testBanchmark() {
		
		FromBean fromBean = getFromBean();
		
		BeanCopyInterface BeanCopyUtilsCopyBean1 = new BeanCopyInterface() {
			@Override
			public String getMethodName() {
				return "com.tuyang.beanutils.BeanCopyUtils.copyBean 1";
			}
			@Override
			public ToBean callCopyBean(FromBean fromBean) throws Exception {
				ToBean toBean = new ToBean();
				return (ToBean) BeanCopyUtils.copyBean(fromBean, toBean);
			}
		};
		
		BeanCopyInterface BeanCopyUtilsCopyBean2 = new BeanCopyInterface() {
			com.tuyang.beanutils.BeanCopier copier = BeanCopyUtils.getBeanCopier(FromBean.class, ToBean.class);
			@Override
			public String getMethodName() {
				return "com.tuyang.beanutils.BeanCopyUtils.copyBean 2";
			}
			@Override
			public ToBean callCopyBean(FromBean fromBean) throws Exception {
				ToBean toBean = new ToBean();
				return (ToBean)copier.copyBean(fromBean, toBean);
			}
		};

		BeanCopyInterface BeanUtilcopyProperties = new BeanCopyInterface() {

			@Override
			public String getMethodName() {
				return "org.apache.commons.beanutils.BeanUtil.copyProperties";
			}
			@Override
			public ToBean callCopyBean(FromBean fromBean) throws Exception {
				ToBean toBean = new ToBean();
				BeanUtils.copyProperties(toBean, fromBean);
				return toBean;
			}
		};

		BeanCopyInterface PropertyUtilscopyProperties = new BeanCopyInterface() {
			@Override
			public String getMethodName() {
				return "org.apache.commons.beanutils.PropertyUtils.copyProperties";
			}
			@Override
			public ToBean callCopyBean(FromBean fromBean) throws Exception {
				ToBean toBean = new ToBean();
				PropertyUtils.copyProperties(toBean, fromBean);
				return toBean;
			}
		};

		BeanCopyInterface springcopyProperties = new BeanCopyInterface() {
			@Override
			public String getMethodName() {
				return "org.springframework.beans.BeanUtils.copyProperties";
			}
			@Override
			public ToBean callCopyBean(FromBean fromBean) throws Exception {
				ToBean toBean = new ToBean();
				org.springframework.beans.BeanUtils.copyProperties(fromBean,
						toBean);
				return toBean;
			}
		};

		BeanCopyInterface BeanCopiercreate1 = new BeanCopyInterface() {
			
			
			@Override
			public String getMethodName() {
				return "org.springframework.cglib.beans.BeanCopier.create 1";
			}
			@Override
			public ToBean callCopyBean(FromBean fromBean) throws Exception {
				ToBean toBean = new ToBean();
				BeanCopier bc = BeanCopier.create(FromBean.class, ToBean.class, false);

				bc.copy(fromBean, toBean, null);
				return toBean;
			}
		};
		
		BeanCopyInterface BeanCopiercreate2 = new BeanCopyInterface() {
			BeanCopier bc = BeanCopier.create(FromBean.class, ToBean.class, false);

			@Override
			public String getMethodName() {
				return "org.springframework.cglib.beans.BeanCopier.create 2";
			}
			@Override
			public ToBean callCopyBean(FromBean fromBean) throws Exception {
				ToBean toBean = new ToBean();
				bc.copy(fromBean, toBean, null);
				return toBean;
			}
		};
		
		BeanCopyInterface NativeCopy = new BeanCopyInterface() {
			
			@Override
			public String getMethodName() {
				return "NativeCopy";
			}
			@Override
			public ToBean callCopyBean(FromBean fromBean) throws Exception {
				ToBean toBean = new ToBean();
				toBean.setBeanByte(fromBean.getBeanByte());
				toBean.setBeanChar(fromBean.getBeanChar());
				toBean.setBeanDouble(fromBean.getBeanDouble());
				toBean.setBeanFloat(fromBean.getBeanFloat());
				toBean.setBeanInt(fromBean.getBeanInt());
				toBean.setBeanLong(fromBean.getBeanLong());
				toBean.setBeanShort(fromBean.getBeanShort());
				toBean.setBeanString(fromBean.getBeanString());
				return toBean;
			}
		};

		LoopTest loopTest = new LoopTest(1);
		
		loopTest.run(BeanCopyUtilsCopyBean1, fromBean);
		loopTest.run(BeanCopyUtilsCopyBean2, fromBean);
		loopTest.run(BeanCopiercreate1, fromBean);
		loopTest.run(BeanCopiercreate2, fromBean);
		loopTest.run(springcopyProperties, fromBean);
		loopTest.run(BeanUtilcopyProperties, fromBean);
		loopTest.run(PropertyUtilscopyProperties, fromBean);
		
		loopTest = new LoopTest(100);

		loopTest.run(BeanCopyUtilsCopyBean1, fromBean);
		loopTest.run(BeanCopyUtilsCopyBean2, fromBean);
		loopTest.run(BeanCopiercreate1, fromBean);
		loopTest.run(BeanCopiercreate2, fromBean);
		loopTest.run(springcopyProperties, fromBean);
		loopTest.run(BeanUtilcopyProperties, fromBean);
		loopTest.run(PropertyUtilscopyProperties, fromBean);
		
		for( int i =0; i< 3; i++ ) {
			
			loopTest = new LoopTest(100000);
			
			loopTest.run(BeanCopyUtilsCopyBean1, fromBean);
			loopTest.run(BeanCopyUtilsCopyBean2, fromBean);
			loopTest.run(BeanCopiercreate1, fromBean);
			loopTest.run(BeanCopiercreate2, fromBean);
			loopTest.run(springcopyProperties, fromBean);
			loopTest.run(BeanUtilcopyProperties, fromBean);
			loopTest.run(PropertyUtilscopyProperties, fromBean);
		}
		
		for( int i =0; i< 5; i++ ) {
			loopTest = new LoopTest(10000000);
		
			loopTest.run(BeanCopyUtilsCopyBean1, fromBean);
			loopTest.run(BeanCopyUtilsCopyBean2, fromBean);
			loopTest.run(BeanCopiercreate1, fromBean);
			loopTest.run(BeanCopiercreate2, fromBean);
			loopTest.run(NativeCopy, fromBean);
		
		}

		System.out.println("===================finish===============");
		
	}
}

