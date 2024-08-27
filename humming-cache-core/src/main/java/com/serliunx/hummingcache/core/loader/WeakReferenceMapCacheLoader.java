package com.serliunx.hummingcache.core.loader;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 大部分逻辑同内置CHM缓存池
 * <li> 核心的要点是, 所有对象节点使用弱引用包装。这意味着每次触发GC, 缓存都会消失。
 * <li> 请结合业务场景使用
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.1
 * @since 2024/8/13
 */
public class WeakReferenceMapCacheLoader extends MapCacheLoader {

	private final ConcurrentHashMap<String, WeakReference<CacheNode>> WEAK_CACHE_MAP = new ConcurrentHashMap<>(256);

	@Override
	public void remove(String key) {
		WEAK_CACHE_MAP.remove(key);
	}

	@Override
	public void removeIfAlive(String key) {
		if (isAlive(key)) {
			WEAK_CACHE_MAP.remove(key);
		}
	}

	@Override
	public void removeIfExpired(String key) {
		if (!isAlive(key)) {
			WEAK_CACHE_MAP.remove(key);
		}
	}

	@Override
	protected void put(String key, CacheNode cacheNode) {
		WEAK_CACHE_MAP.put(key, new WeakReference<>(cacheNode));
	}

	@Override
	protected void delete(String key) {
		WEAK_CACHE_MAP.remove(key);
	}

	@Override
	protected CacheNode getNode(String key) {
		WeakReference<CacheNode> cacheNodeWeakReference = WEAK_CACHE_MAP.get(key);
		if (cacheNodeWeakReference == null) {
			return null;
		}
		return cacheNodeWeakReference.get();
	}

	@Override
	protected boolean containsKey(String key) {
		return WEAK_CACHE_MAP.containsKey(key);
	}
}
