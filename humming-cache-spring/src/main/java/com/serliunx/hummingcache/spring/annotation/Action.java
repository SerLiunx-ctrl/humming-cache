package com.serliunx.hummingcache.spring.annotation;

/**
 * 缓存注解行为定义
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/22
 */
public enum Action {

	/**
	 * 缓存加载
	 */
	CACHE_LOAD,
	/**
	 * 刷新缓存
	 */
	CACHE_REFRESH
}
