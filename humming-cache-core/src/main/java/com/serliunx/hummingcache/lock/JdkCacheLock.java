package com.serliunx.hummingcache.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于JDK中Lock的缓存锁
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public class JdkCacheLock implements CacheLock {

	private final Lock internalLock = new ReentrantLock();
	private final String key;

	public JdkCacheLock(String key) {
		this.key = key;
	}

	@Override
	public void lock() {
		internalLock.lock();
	}

	@Override
	public void unlock() {
		internalLock.unlock();
	}

	@Override
	public String getKey() {
		return key;
	}
}
