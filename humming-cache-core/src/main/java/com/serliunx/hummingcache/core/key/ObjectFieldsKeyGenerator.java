package com.serliunx.hummingcache.core.key;

/**
 * 依赖外部对象生成的键
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public class ObjectFieldsKeyGenerator implements KeyGenerator {

	private static final PlaceholdersKeyGenerator PLACEHOLDERS_KEY_GENERATOR = new PlaceholdersKeyGenerator();

	@Override
	public String generate(String rawKey, Object... objects) {
		return rawKey;
	}
}
