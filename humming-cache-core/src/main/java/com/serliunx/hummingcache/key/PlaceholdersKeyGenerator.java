package com.serliunx.hummingcache.key;

/**
 * 占位符式键生成器
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public final class PlaceholdersKeyGenerator implements KeyGenerator {

	@Override
	public String generate(String rawKey, Object... objects) {
		return build(rawKey, objects);
	}

	private static String build(String key, Object...args){
		return keyBuild(key, args);
	}

	private static String keyBuild(String rawKey, Object...args){
		if (args == null) {
			return rawKey;
		}
		for (Object arg : args) {
			String placeholder = getFirstPlaceHolder(rawKey);
			rawKey = rawKey.replace(placeholder, arg.toString());
		}
		return rawKey;
	}

	private static String getFirstPlaceHolder(String key){
		int left = key.indexOf("{");
		int right = key.indexOf("}");
		if(left == -1 || right == -1){
			return key;
		}
		return key.substring(left, right + 1);
	}
}
