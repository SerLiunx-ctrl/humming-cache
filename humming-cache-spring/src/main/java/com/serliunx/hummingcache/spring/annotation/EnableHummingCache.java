package com.serliunx.hummingcache.spring.annotation;

import com.serliunx.hummingcache.core.loader.MapCacheLoader;
import com.serliunx.hummingcache.core.loader.WeakReferenceMapCacheLoader;
import com.serliunx.hummingcache.spring.config.HummingCacheConfigurationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否启用缓存
 * <li> 启用后将自动装配两个默认的缓存介质 {@link MapCacheLoader} 及
 * {@link WeakReferenceMapCacheLoader}
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HummingCacheConfigurationSelector.class)
public @interface EnableHummingCache {

}
