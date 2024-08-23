package com.serliunx.hummingcache.core.lock;

import java.util.concurrent.locks.Lock;

/**
 * 可锁定的.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public interface Lockable {

	/**
	 * 获取锁
	 *
	 * @param key 键
	 * @return 缓存锁
	 */
	Lock lock(String key);
}
