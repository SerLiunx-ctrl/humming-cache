package com.serliunx.hummingcache.core.loader;

import com.serliunx.hummingcache.core.loader.listner.Attachment;
import com.serliunx.hummingcache.core.loader.listner.CacheListener;
import com.serliunx.hummingcache.core.loader.listner.CacheListenerRegistry;
import com.serliunx.hummingcache.core.support.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 监听器注册委托, 用于封装{@link CacheLoader}
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/27
 */
public final class CacheListenerRegistryDelegate extends AbstractCacheLoader implements CacheListenerRegistry {

	private CacheLoader cacheLoader;

	private final List<CacheListener> cacheListeners = new ArrayList<>(64);

	/**
	 * 保留的空构造器
	 */
	public CacheListenerRegistryDelegate() {}

	public CacheListenerRegistryDelegate(CacheLoader cacheLoader) {
		Assert.notNull(cacheLoader, "cacheLoader");
		this.cacheLoader = cacheLoader;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		if (hasCacheListeners() && !isAlive(key)) {
			T result = null;
			long ttl = 0;
			TimeUnit timeUnit = null;
			for (CacheListener cacheListener : cacheListeners) {
				Attachment attachment = cacheListener.expired(cacheLoader, key);
				if (attachment == null) {
					continue;
				}
				result = (T)attachment.getObj();
				ttl = attachment.getTtl();
				timeUnit = attachment.getTimeUnit();
			}
			if (result != null) {
				// 设置缓存
				put(key, result, ttl, timeUnit);
				return result;
			}
		}
		return cacheLoader.get(key);
	}

	@Override
	public <T> boolean put(String key, T val, long timeout, TimeUnit timeUnit) {
		return cacheLoader.put(key, val, timeout, timeUnit);
	}

	@Override
	public void remove(String key) {
		cacheLoader.remove(key);
	}

	@Override
	public long ttl(String key) {
		return cacheLoader.ttl(key);
	}

	@Override
	public void addTtl(String key, long time, TimeUnit timeUnit) {
		cacheLoader.addTtl(key, time, timeUnit);
	}

	@Override
	public boolean hasKey(String key) {
		return cacheLoader.hasKey(key);
	}

	@Override
	public synchronized void registerCacheListener(CacheListener cacheListener) {
		cacheListeners.add(cacheListener);
	}

	@Override
	public synchronized void unregisterCacheListener(CacheListener cacheListener) {
		cacheListeners.remove(cacheListener);
	}

	public void setCacheLoader(CacheLoader cacheLoader) {
		Assert.notNull(cacheLoader, "cacheLoader");
		this.cacheLoader = cacheLoader;
	}

	public CacheLoader getCacheLoader() {
		return this.cacheLoader;
	}

	private boolean hasCacheListeners() {
		return !cacheListeners.isEmpty();
	}

	/**
	 * 快速构建
	 */
	public static CacheListenerRegistryDelegate fromLoader(CacheLoader original) {
		return new CacheListenerRegistryDelegate(original);
	}
}
