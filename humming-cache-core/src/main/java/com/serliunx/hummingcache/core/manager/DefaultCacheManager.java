package com.serliunx.hummingcache.core.manager;

import com.serliunx.hummingcache.core.exception.CacheKeyIllegalException;
import com.serliunx.hummingcache.core.loader.CacheLoader;

import java.util.concurrent.locks.Lock;

/**
 * 默认的缓存管理/调度工具.
 * <li> 大部分情况下是够用的, 可以基于{@link CacheManager} 自行实现
 * <li> 请与javax中或Jakarta及Spring Cache中的CacheManager区分开.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public class DefaultCacheManager<T> extends AbstractCacheManager<T> {

	public DefaultCacheManager(CacheLoader cacheLoader) {
		super(cacheLoader);
	}

	@Override
	public T get() {
		if (key == null || key.isEmpty()) {
			throw new CacheKeyIllegalException();
		}
		T result = null;
		Lock lock = keyLock ? cacheLoader.lock(key) : null;
		try {
			if (onlyAbsent && cacheLoader.hasKey(key)) {
				return cacheLoader.get(key);
			}
			// 由内置缓存触发器和强制更新决定是否需要更新缓存
			if ((trigger != null
					&& trigger.apply(cacheLoader, key)) || forceRefresh) {
				runExpirationHandler();
				result = supplier.get();
				cacheLoader.put(key, result, timeout, timeUnit);
			} else {
				result = cacheLoader.get(key);
			}
			if (result != null) {
				return result;
			}
			// 正常触发缓存更新
			try {
				if (lock != null) {
					lock.lock();
				}
				if (cacheLoader.hasKey(key)) {
					return cacheLoader.get(key);
				}
				runExpirationHandler();
				result = supplier.get();
				// 如果此时数据还为空, 跳过缓存更新校验及逻辑
				if (result == null) {
					return null;
				}
				if (needsRefresh == null
						|| needsRefresh.apply(result)) {
					cacheLoader.put(key, result, timeout, timeUnit);
				}
			} finally {
				if (lock != null) {
					lock.unlock();
				}
			}
		} catch (Throwable throwable) {
			runExceptionHandler(throwable);
		} finally {
			// 尽可能协助垃圾回收, 作用不大. 甚至可能是心理作用
			postProcess();
		}
		return result;
	}
}
