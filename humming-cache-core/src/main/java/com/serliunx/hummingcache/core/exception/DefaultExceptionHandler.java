package com.serliunx.hummingcache.core.exception;

import java.util.function.Consumer;

/**
 * 默认的异常处理器
 * <li> 仅仅封装成运行时异常
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public class DefaultExceptionHandler implements Consumer<Throwable> {

	@Override
	public void accept(Throwable throwable) {
		throw new RuntimeException(throwable);
	}
}
