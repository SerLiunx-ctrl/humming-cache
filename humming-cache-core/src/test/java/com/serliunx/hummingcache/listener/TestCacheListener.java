package com.serliunx.hummingcache.listener;

import com.serliunx.hummingcache.core.loader.CacheLoader;
import com.serliunx.hummingcache.core.loader.listner.Attachment;
import com.serliunx.hummingcache.core.loader.listner.CacheListener;
import com.serliunx.hummingcache.entity.Entity;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/27
 */
public class TestCacheListener implements CacheListener {

	@Override
	public Attachment expired(CacheLoader cacheLoader, String cacheKey) {
		System.out.println("缓存监听器被触发了..." + cacheLoader + ": " +  cacheKey);
		return new Attachment(300, TimeUnit.MILLISECONDS, new Entity(16, "jack"));
	}
}
