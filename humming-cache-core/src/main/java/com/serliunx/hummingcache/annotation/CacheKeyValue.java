package com.serliunx.hummingcache.annotation;

import com.serliunx.hummingcache.key.ObjectFieldsKeyGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标注对象中哪些属性用来生成缓存键
 * <li> 用于{@link ObjectFieldsKeyGenerator}
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheKeyValue {

	/**
	 * 标记键中哪个占位符需要被目标属性替换
	 * <li> 为空时, 替换与属性名一致的占位符
	 *
	 * @return 占位符名称
	 */
	String value() default "";
}
