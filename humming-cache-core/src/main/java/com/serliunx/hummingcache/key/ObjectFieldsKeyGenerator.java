package com.serliunx.hummingcache.key;

import com.serliunx.hummingcache.annotation.CacheKeyValue;

/**
 * 依赖外部对象生成的键 {@link ObjectFieldsKeyGenerator}
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public class ObjectFieldsKeyGenerator implements KeyGenerator {

	private static final PlaceholdersKeyGenerator PLACEHOLDERS_KEY_GENERATOR = new PlaceholdersKeyGenerator();
	private static final Class<CacheKeyValue> ANNOTATION_CLASS = CacheKeyValue.class;

	@Override
	public String generate(String rawKey, Object... objects) {
		return rawKey;
	}
}
