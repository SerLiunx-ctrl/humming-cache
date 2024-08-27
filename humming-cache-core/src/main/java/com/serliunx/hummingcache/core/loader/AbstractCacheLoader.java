package com.serliunx.hummingcache.core.loader;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 存储介质的抽象实现.
 * <li> 实现通用的逻辑.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/27
 */
public abstract class AbstractCacheLoader implements CacheLoader {

	protected final ConcurrentHashMap<String, Lock> LOCK_MAP = new ConcurrentHashMap<>(256);

	@Override
	public void removeIfAlive(String key) {
		if (isAlive(key)) {
			remove(key);
		}
	}

	@Override
	public void removeIfExpired(String key) {
		if (!isAlive(key)) {
			remove(key);
		}
	}

	@Override
	public boolean isAlive(String key) {
		return ttl(key) > 0;
	}

	@Override
	public Lock lock(String key) {
		LOCK_MAP.putIfAbsent(key, new ReentrantLock());
		return LOCK_MAP.get(key);
	}
}
