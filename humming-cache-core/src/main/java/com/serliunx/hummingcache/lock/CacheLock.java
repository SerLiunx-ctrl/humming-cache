package com.serliunx.hummingcache.lock;

import java.util.concurrent.locks.Lock;

/**
 * 缓存锁
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public interface CacheLock {

	/**
	 * 加锁
	 */
	void lock();

	/**
	 * 解锁
	 */
	void unlock();

	/**
	 * 获取与当前这把锁相关联的缓存键
	 */
	String getKey();
}
