package com.serliunx.hummingcache.exception;

/**
 * 异常: 缓存键为空
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public class CacheKeyIllegalException extends IllegalArgumentException {

	public CacheKeyIllegalException() {
		super("缓存键不能为空!");
	}
}
