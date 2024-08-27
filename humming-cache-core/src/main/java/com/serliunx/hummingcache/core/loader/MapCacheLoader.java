package com.serliunx.hummingcache.core.loader;

import com.serliunx.hummingcache.core.exception.CacheKeyIllegalException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 内置CHM缓存池
 * <li> 适用于单机、数据量小的缓存， 如反射数据的缓存。
 * <li> 由于使用的JVM中堆内存空间, 大量数据且存活时间过长易造成内存溢出.
 * <li> 仅基于内存实现, 无任何持久化机制.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public class MapCacheLoader extends AbstractCacheLoader implements CacheLoader {

	/**
	 * 使用CHM实现的缓存池
	 */
	private final ConcurrentHashMap<String, CacheNode> CACHE_MAP = new ConcurrentHashMap<>(256);

	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		if (key == null || key.isEmpty()) {
			throw new CacheKeyIllegalException();
		}
		long now = now();
		CacheNode node = getNode(key);
		if (node == null) {
			return null;
		}
		if (now > node.getTime()) {
			delete(key);
			return null;
		}
		return (T)node.getObj();
	}

	@Override
	public <T> boolean put(String key, T val, long timeout, TimeUnit timeUnit) {
		if (key == null || key.isEmpty()) {
			throw new CacheKeyIllegalException();
		}
		if (timeUnit == null || timeUnit == TimeUnit.NANOSECONDS) {
			throw new IllegalArgumentException("存活时间单位不能为空, 且最小单位仅支持毫秒!");
		}
		if (timeout <= 0) {
			throw new IllegalArgumentException("存活时间必须大于0!");
		}
		boolean exists = getNode(key) != null;
		long time = now() + timeUnit.toMillis(timeout);
		CacheNode cacheNode = new CacheNode(val, time);
		put(key, cacheNode);
		return exists;
	}

	@Override
	public void remove(String key) {
		delete(key);
	}

	@Override
	public long ttl(String key) {
		CacheNode node = getNode(key);
		if (node == null) {
			return -1;
		}
		long ttl = node.getTime() - now();
		return ttl <= -1 ? -1 : ttl;
	}

	@Override
	public void addTtl(String key, long time, TimeUnit timeUnit) {
		final CacheNode node;
		if ((node = getNode(key)) == null) {
			return;
		}
		long timeToExpired = node.getTime();
		long millis = timeUnit.toMillis(time);
		node.time = timeToExpired + millis;
	}

	@Override
	public boolean hasKey(String key) {
		return containsKey(key);
	}

	/**
	 * 获取缓存
	 */
	protected CacheNode getNode(String key) {
		return CACHE_MAP.get(key);
	}

	protected void put(String key ,CacheNode cacheNode) {
		CACHE_MAP.put(key, cacheNode);
	}

	protected void delete(String key) {
		CACHE_MAP.remove(key);
	}

	protected boolean containsKey(String key) {
		return CACHE_MAP.containsKey(key);
	}

	/**
	 * 获取当前时间戳
	 */
	protected long now() {
		return System.currentTimeMillis();
	}

	protected static class CacheNode {

		/**
		 * 缓存对象
		 */
		private final Object obj;
		/**
		 * 存活时间(毫秒)
		 */
		private volatile long time;

		public CacheNode(Object obj, long time) {
			this.obj = obj;
			this.time = time;
		}

		public Object getObj() {
			return obj;
		}

		public long getTime() {
			return time;
		}
	}
}
