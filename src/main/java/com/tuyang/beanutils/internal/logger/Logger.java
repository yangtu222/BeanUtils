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

package com.tuyang.beanutils.internal.logger;

import java.lang.reflect.Method;

import com.tuyang.beanutils.config.BeanCopyConfig;

public class Logger {

	public static int LogLevelAll   = 0;
	public static int LogLevelTrace = 0;
	public static int LogLevelDebug = 1;
	public static int LogLevelInfo  = 2;
	public static int LogLevelWarn  = 3;
	public static int LogLevelError = 4;
	public static int LogLevelFatal = 5;
	public static int LogLevelNone  = 6;
	
	public static Logger getLogger(Class<?> clazz) {
		try {
			Class<?> loggerClass = Logger.class.getClassLoader().loadClass("org.apache.log4j.Logger");
			Method method = loggerClass.getMethod("getLogger", Class.class);
			Object logger = method.invoke(null, clazz);
			return new Logger(logger);
		}catch (Exception e) {
			
		}
		return new Logger(null);
	}
	
	Object logger = null;
	
	public Logger( Object logger ) {
		this.logger = logger;
	}
	
	public void trace(Object message) {
		
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("trace", Object.class);
				method.invoke(logger, message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() < LogLevelTrace )
				return;
			
			System.out.println(message.toString());
		}
	}
	
	public void trace(Object message, Throwable t) {
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("trace", Object.class, Throwable.class);
				method.invoke(logger, message, t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() < LogLevelTrace )
				return;
			
			System.out.println(message.toString());
			t.printStackTrace();
		}
	}

	public void debug(Object message) {
		
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("debug", Object.class);
				method.invoke(logger, message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() < LogLevelDebug )
				return;
			
			System.out.println(message.toString());
		}
	}

	/**
	 * Delegates to {@link org.slf4j.Logger#debug(String,Throwable)} method in
	 * SLF4J.
	 */
	public void debug(Object message, Throwable t) {
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("debug", Object.class, Throwable.class);
				method.invoke(logger, message, t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() < LogLevelDebug )
				return;
			
			System.out.println(message.toString());
			t.printStackTrace();
		}
	}

	/**
	 * Delegates to {@link org.slf4j.Logger#info(String)} method in SLF4J.
	 */
	public void info(Object message) {
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("info", Object.class );
				method.invoke(logger, message );
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() < LogLevelInfo )
				return;
			
			System.out.println(message.toString());
		}
	}

	/**
	 * Delegates to {@link org.slf4j.Logger#info(String,Throwable)} method in
	 * SLF4J.
	 */
	public void info(Object message, Throwable t) {
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("info", Object.class, Throwable.class);
				method.invoke(logger, message, t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() < LogLevelInfo )
				return;
			
			System.out.println(message.toString());
			t.printStackTrace();
		}
	}

	/**
	 * Delegates to {@link org.slf4j.Logger#warn(String)} method in SLF4J.
	 */
	public void warn(Object message) {
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("warn", Object.class);
				method.invoke(logger, message );
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() < LogLevelWarn )
				return;
			
			System.out.println(message.toString());
		}
	}

	/**
	 * Delegates to {@link org.slf4j.Logger#warn(String,Throwable)} method in
	 * SLF4J.
	 */
	public void warn(Object message, Throwable t) {
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("warn", Object.class, Throwable.class);
				method.invoke(logger, message, t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() < LogLevelWarn )
				return;
			
			System.out.println(message.toString());
			t.printStackTrace();
		}
	}

	/**
	 * Delegates to {@link org.slf4j.Logger#error(String)} method in SLF4J.
	 */
	public void error(Object message) {
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("error", Object.class );
				method.invoke(logger, message );
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() < LogLevelError )
				return;
			
			System.out.println(message.toString());
		}
	}

	/**
	 * Delegates to {@link org.slf4j.Logger#error(String,Throwable)} method in
	 * SLF4J.
	 */
	public void error(Object message, Throwable t) {
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("error", Object.class, Throwable.class);
				method.invoke(logger, message, t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() < LogLevelError )
				return;
			
			System.out.println(message.toString());
			t.printStackTrace();
		}
	}

	/**
	 * Delegates to {@link org.slf4j.Logger#error(String)} method in SLF4J.
	 */
	public void fatal(Object message) {
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("fatal", Object.class, Throwable.class);
				method.invoke(logger, message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() > LogLevelDebug )
				return;
			
			System.out.println(message.toString());
		}
	}

	/**
	 * Delegates to {@link org.slf4j.Logger#error(String,Throwable)} method in
	 * SLF4J. In addition, the call is marked with a marker named "FATAL".
	 */
	public void fatal(Object message, Throwable t) {
		if( logger != null ) {
			try {
				Method method = logger.getClass().getMethod("fatal", Object.class, Throwable.class);
				method.invoke(logger, message, t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if( BeanCopyConfig.instance().getLogLevel() > LogLevelDebug )
				return;
			
			System.out.println(message.toString());
			t.printStackTrace();
		}
	}
}
