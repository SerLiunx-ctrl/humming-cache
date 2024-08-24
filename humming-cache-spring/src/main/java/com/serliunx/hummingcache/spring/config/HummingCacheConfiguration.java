package com.serliunx.hummingcache.spring.config;

import com.serliunx.hummingcache.core.loader.MapCacheLoader;
import com.serliunx.hummingcache.core.loader.WeakReferenceMapCacheLoader;
import org.springframework.context.annotation.Bean;

/**
 * 缓存参数配置
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/22
 */
public class HummingCacheConfiguration {

	@Bean
	public MapCacheLoader mapCacheLoader() {
		return new MapCacheLoader();
	}

	@Bean
	public WeakReferenceMapCacheLoader weakReferenceMapCacheLoader() {
		return new WeakReferenceMapCacheLoader();
	}
}
