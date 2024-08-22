package com.serliunx.hummingcache.support;

import com.serliunx.hummingcache.loader.CacheLoader;

import java.util.concurrent.TimeUnit;

/**
 * 缓存触发器
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public interface Trigger<O, T> {

	/**
	 * 校验触发器
	 *
	 * @param o 源1
	 * @param t 源2
	 * @return 结果
	 */
	Boolean apply(O o, T t);

	/**
	 * 缓存键触发器, 用于判断缓存中是否存在指定的键
	 * <li> 用于缓存调度
	 *
	 * @param k 键
	 * @return 缓存键触发器
	 */
	static Trigger<CacheLoader, String> hasKey(String k) {
		return (cacheLoader, key) -> cacheLoader.isAlive(k);
	}

	/**
	 * 缓存存活时间阈值触发器
	 * <li> 当缓存尚且存活且剩余的存活时间小于指定的时间时触发
	 *
	 * @param time 时间
	 * @param timeUnit 时间单位
	 * @return 缓存存活时间阈值触发器
	 */
	static Trigger<CacheLoader, String> threshold(long time, TimeUnit timeUnit) {
		return (cacheLoader, key) -> {
			if (time <= 0 || timeUnit == null) {
				return false;
			}
			return cacheLoader.ttl(key) <= timeUnit.toMillis(time);
		};
	}
}
