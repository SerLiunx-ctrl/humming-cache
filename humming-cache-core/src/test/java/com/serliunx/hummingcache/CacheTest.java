package com.serliunx.hummingcache;

import com.serliunx.hummingcache.core.loader.CacheListenerRegistryDelegate;
import com.serliunx.hummingcache.entity.Entity;
import com.serliunx.hummingcache.core.loader.MapCacheLoader;
import com.serliunx.hummingcache.core.manager.CacheManager;
import com.serliunx.hummingcache.core.manager.WeakCacheManager;
import com.serliunx.hummingcache.listener.TestCacheListener;
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
		CacheListenerRegistryDelegate delegate = CacheListenerRegistryDelegate.fromLoader(new MapCacheLoader());
		delegate.registerCacheListener(new TestCacheListener());

		CacheManager<Entity> manager = new WeakCacheManager<Entity>(delegate)
				.key("cache_test:{num}", 1)
				.ttl(500, TimeUnit.MILLISECONDS)
				.supplier(() -> {
					System.out.println("supplier invoked!");
					return new Entity(32, "jack");
				})
				.reusable()
				.lock();
		System.out.println(manager.get());
		System.out.println(manager.get());

		System.out.println(delegate.ttl("cache_test:1"));

		TimeUnit.MILLISECONDS.sleep(1000);

		System.out.println(manager.get());
		System.out.println(delegate.ttl("cache_test:1"));
	}
}
