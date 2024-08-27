package com.serliunx.hummingcache.core.loader.listner;

import com.serliunx.hummingcache.core.loader.CacheLoader;
import com.serliunx.hummingcache.core.manager.CacheManager;

import java.util.function.Supplier;

/**
 * 缓存监听器.
 * <li> 用于监听指定缓存键或者键模板.
 * <li> 一般用于全局的缓存控制, 单独监听缓存的过期事件建议通过 {@link CacheManager#whenExpired}
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/27
 */
@FunctionalInterface
public interface CacheListener {

	/**
	 * 缓存过期时执行的操作
	 *
	 * @param cacheLoader 存储介质
	 * @param cacheKey 过期缓存所对应的键
	 * @return 返回空时不做任何处理, 不为空时会将返回值设置为新的缓存，
	 * 		   且不会执行{@link CacheManager#supplier(Supplier)}中所指定的内容。
	 * 		   因为此时对于{@link CacheManager}来说，缓存是没有过期的。
	 */
	Attachment expired(CacheLoader cacheLoader, String cacheKey);
}
