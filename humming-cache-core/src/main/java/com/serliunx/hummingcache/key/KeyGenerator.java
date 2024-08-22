package com.serliunx.hummingcache.key;

/**
 * 缓存键生成器
 * <li> 根据规则生成缓存对应的键
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
@FunctionalInterface
public interface KeyGenerator {

	/**
	 * 生成键, 逻辑取决于具体的实现
	 * <li> {@link ObjectFieldsKeyGenerator}
	 * <li> {@link PlaceholdersKeyGenerator}
	 *
	 * @param rawKey 代替换的源键
	 * @param objects 变量值
	 * @return 键
	 */
	String generate(String rawKey, Object...objects);
}
