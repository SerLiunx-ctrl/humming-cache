package com.serliunx.hummingcache;

import com.serliunx.hummingcache.entity.Entity;
import com.serliunx.hummingcache.loader.CacheLoader;
import com.serliunx.hummingcache.loader.MapCacheLoader;
import com.serliunx.hummingcache.manager.CacheManager;
import com.serliunx.hummingcache.manager.WeakCacheManager;
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
				.ttl(1, TimeUnit.MINUTES)
				.whenExpired(() -> System.out.println("cache expired."))
				.supplier(() -> new Entity(16, "jack"))
				.reusable()
				.lock();
		System.out.println(manager.get());
		System.out.println(manager.get());

		TimeUnit.MILLISECONDS.sleep(2);

		System.out.println(manager.get());
	}
}
