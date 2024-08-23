package com.serliunx.hummingcache;

import com.serliunx.hummingcache.entity.Entity;
import com.serliunx.hummingcache.core.loader.CacheLoader;
import com.serliunx.hummingcache.core.loader.MapCacheLoader;
import com.serliunx.hummingcache.core.manager.CacheManager;
import com.serliunx.hummingcache.core.manager.WeakCacheManager;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public class CacheTest {

	@Test
	public void defaultTest() throws Exception {
		CacheLoader cacheLoader = new MapCacheLoader();
		CacheManager<Entity> manager = new WeakCacheManager<Entity>(cacheLoader)
				.key("cache_test:{num}", 1)
				.ttl(6, TimeUnit.MILLISECONDS)
				.whenExpired(() -> System.out.println("cache expired."))
				.supplier(() -> new Entity(16, "jack"))
				.reusable()
				.lock();
		System.out.println(manager.get());
		System.out.println(manager.get());

		TimeUnit.MILLISECONDS.sleep(4);

		System.out.println(manager.get());
	}
}
