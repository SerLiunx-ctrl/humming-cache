package com.serliunx.hummingcache.spring.config;

import com.serliunx.hummingcache.core.loader.MapCacheLoader;
import com.serliunx.hummingcache.core.loader.WeakReferenceMapCacheLoader;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

/**
 * 缓存参数配置
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/22
 */
public class HummingCacheConfiguration implements ApplicationContextAware, EnvironmentAware, Ordered {

	private ApplicationContext applicationContext;
	private Environment environment;

	@Bean
	public MapCacheLoader mapCacheLoader() {
		return new MapCacheLoader();
	}

	@Bean
	public WeakReferenceMapCacheLoader weakReferenceMapCacheLoader() {
		return new WeakReferenceMapCacheLoader();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public int getOrder() {
		return 1;
	}
}
