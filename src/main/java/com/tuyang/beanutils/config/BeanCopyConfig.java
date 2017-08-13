 /*
  BeanUtils Version 1.0.0

  Created by yangtu222 on 2017.08.05

  Distributed under the permissive zlib License
  Get the latest version from here:

  https://github.com/yangtu222/BeanUtils

  This software is provided 'as-is', without any express or implied
  warranty.  In no event will the authors be held liable for any damages
  arising from the use of this software.

  Permission is granted to anyone to use this software for any purpose,
  including commercial applications, and to alter it and redistribute it
  freely, subject to the following restrictions:

  1. The origin of this software must not be misrepresented; you must not
  claim that you wrote the original software. If you use this software
  in a product, an acknowledgment in the product documentation would be
  appreciated but is not required.

  2. Altered source versions must be plainly marked as such, and must not be
  misrepresented as being the original software.

  3. This notice may not be removed or altered from any source distribution.
*/

package com.tuyang.beanutils.config;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

import com.tuyang.beanutils.internal.cache.BeanCopyCache;
import com.tuyang.beanutils.internal.factory.BeanCopierFactory;
import com.tuyang.beanutils.internal.javassist.JavassistBeanCopyFactory;
import com.tuyang.beanutils.internal.logger.Logger;

public class BeanCopyConfig {

	private static BeanCopyConfig INSTANCE = new BeanCopyConfig();
	
	private int logLevel = Logger.LogLevelWarn;
	
	private Class<?> setClass = HashSet.class;
	private Class<?> listClass = ArrayList.class;
	private Class<?> queueClass = ArrayDeque.class;
	private Class<?> dequeClass = ArrayDeque.class;
	
	private Class<? extends BeanCopierFactory> beanCopyFactory = JavassistBeanCopyFactory.class;
//	private Class<? extends BeanCopierFactory> beanCopyFactory = ReflactBeanCopyFactory.class;
	
	
	public static BeanCopyConfig instance() {
		return INSTANCE;
	}

	public static void setBeanCopyConfig(BeanCopyConfig beanCopyConfig) {
		if( beanCopyConfig == null )
			beanCopyConfig = new BeanCopyConfig();
		BeanCopyConfig.INSTANCE = beanCopyConfig;
		BeanCopyCache.setBeanCopyConfig(beanCopyConfig);
	}

	public int getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	public Class<?> getSetClass() {
		return setClass;
	}

	public void setSetClass(Class<?> setClass) {
		this.setClass = setClass;
	}

	public Class<?> getListClass() {
		return listClass;
	}

	public void setListClass(Class<?> listClass) {
		this.listClass = listClass;
	}

	public Class<? extends BeanCopierFactory> getBeanCopyFactory() {
		return beanCopyFactory;
	}

	public void setBeanCopyFactory(Class<? extends BeanCopierFactory> beanCopyFactory) {
		this.beanCopyFactory = beanCopyFactory;
	}

	public Class<?> getQueueClass() {
		return queueClass;
	}

	public void setQueueClass(Class<?> queueClass) {
		this.queueClass = queueClass;
	}

	public Class<?> getDequeClass() {
		return dequeClass;
	}

	public void setDequeClass(Class<?> dequeClass) {
		this.dequeClass = dequeClass;
	}

}
