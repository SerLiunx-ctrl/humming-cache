package com.serliunx.hummingcache.spring.config;

import com.serliunx.hummingcache.spring.support.HummingCacheProxyBeanPostProcessor;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 处理器及配置的导入
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/22
 */
public class HummingCacheConfigurationSelector implements ImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		return new String[] {
				HummingCacheConfiguration.class.getName(),
				HummingCacheProxyBeanPostProcessor.class.getName()
		};
	}
}
