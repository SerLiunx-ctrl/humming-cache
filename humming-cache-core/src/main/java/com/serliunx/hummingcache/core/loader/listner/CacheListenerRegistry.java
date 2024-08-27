package com.serliunx.hummingcache.core.loader.listner;

import com.serliunx.hummingcache.core.loader.CacheLoader;
import com.serliunx.hummingcache.core.loader.CacheListenerRegistryDelegate;

/**
 * 缓存监听器的注册行为.
 * <li> 默认由{@link CacheLoader} 实现
 * <li> 目前暂无直接实现, 使用{@link CacheListenerRegistryDelegate#fromLoader(CacheLoader)}将现有的{@link CacheLoader}包装成可监听的.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/27
 */
public interface CacheListenerRegistry {

	/**
	 * 注册监听器
	 * <li> 具体行为由实现决定, 可能是无序, 也可能是有序的.
	 *
	 * @param cacheListener 监听器
	 */
	void registerCacheListener(CacheListener cacheListener);

	/**
	 * 移除监听器
	 *
	 * @param cacheListener 监听器
	 */
	void unregisterCacheListener(CacheListener cacheListener);
}
