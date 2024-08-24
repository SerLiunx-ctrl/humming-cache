package com.serliunx.hummingcache.boot.core;

import com.serliunx.hummingcache.core.loader.MapCacheLoader;
import com.serliunx.hummingcache.core.loader.WeakReferenceMapCacheLoader;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/24
 */
@AutoConfiguration
public class HummingCacheAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(MapCacheLoader.class)
	public MapCacheLoader mapCacheLoader() {
		return new MapCacheLoader();
	}

	@Bean
	@ConditionalOnMissingBean(WeakReferenceMapCacheLoader.class)
	public WeakReferenceMapCacheLoader weakReferenceMapCacheLoader() {
		return new WeakReferenceMapCacheLoader();
	}
}
