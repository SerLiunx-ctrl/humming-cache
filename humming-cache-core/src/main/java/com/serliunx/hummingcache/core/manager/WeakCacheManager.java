package com.serliunx.hummingcache.core.manager;

import com.serliunx.hummingcache.core.support.Trigger;
import com.serliunx.hummingcache.core.key.KeyGenerator;
import com.serliunx.hummingcache.core.loader.CacheLoader;
import com.serliunx.hummingcache.core.loader.MapCacheLoader;
import com.serliunx.hummingcache.core.loader.WeakReferenceMapCacheLoader;
import com.serliunx.hummingcache.core.support.Assert;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 弱引用包装的缓存管理调度中心.
 * <li> 详细信息见 {@link CacheManager}
 * <li> 最佳实践:
 * <p>
 * 适用于访问频率相对较高、能够在短时间内对GC造成较大影响的接口。这里所说的对GC造成影响的原因是
 * 绝大多数情况下我们对调度器都是一次性使用，每次接口访问（不一定时每次接口访问，取决于你使用的场景）都会
 * 创建一个缓存调度器, 这意味着瞬时可能创建大批量的调度器对象。
 * <p> 此时可以得出一个结论: 所造成的GC压力来源于缓存调度器，并不是所缓存的对象（也不绝对）
 * <p> 当然，以上的结论并不适用于所有场景。如果你使用的基于堆内存的缓存池{@link CacheLoader} 又不太一样了,
 * 详见 {@link MapCacheLoader} 及
 * {@link WeakReferenceMapCacheLoader}
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.1.0
 * @since 2024/8/13
 * @see WeakReference
 */
public class WeakCacheManager<T> implements CacheManager<T> {

	private final WeakReference<CacheManager<T>> weakCacheManager;

	/**
	 * 标记本次缓存管理器是否已失效
	 * <li> 可能出现在创建时刚好触发了垃圾回收。
	 * <p> 对于需不需要使用volatile的分析:
	 * <p>
	 * 具体问题具体分析, 取决于构造的管理器是否为复用的且同时被多个线程同时使用。
	 * </p>
	 */
	private volatile boolean failure = false;
	/**
	 * GC发生时的策略, 直接执行填充策略.
	 */
	private Supplier<T> internalSupplier;

	public WeakCacheManager(CacheLoader cacheLoader) {
		Assert.notNull(cacheLoader, "CacheLoader must not be null");
		this.weakCacheManager = new WeakReference<>(new DefaultCacheManager<>(cacheLoader));
	}

	@Override
	public CacheManager<T> reusable() {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.reusable();
		}
		return this;
	}

	@Override
	public CacheManager<T> once() {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.once();
		}
		return this;
	}

	@Override
	public CacheManager<T> setKeyGenerator(KeyGenerator generator) {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.setKeyGenerator(generator);
		}
		return this;
	}

	@Override
	public CacheManager<T> key(String key) {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.key(key);
		}
		return this;
	}

	@Override
	public CacheManager<T> key(String rawKey, Object... args) {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.key(rawKey, args);
		}
		return this;
	}

	@Override
	public CacheManager<T> whenException(Consumer<Throwable> consumer) {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.whenException(consumer);
		}
		return this;
	}

	@Override
	public CacheManager<T> whenExpired(Runnable runnable) {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.whenExpired(runnable);
		}
		return this;
	}

	@Override
	public CacheManager<T> forceRefresh(boolean refresh) {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.forceRefresh(refresh);
		}
		return this;
	}

	@Override
	public CacheManager<T> onlyAbsent() {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.onlyAbsent();
		}
		return this;
	}

	@Override
	public CacheManager<T> lock() {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.lock();
		}
		return this;
	}

	@Override
	public CacheManager<T> needsRefresh(Function<T, Boolean> needsRefresh) {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.needsRefresh(needsRefresh);
		}
		return this;
	}

	@Override
	public CacheManager<T> ttl(long timeout, TimeUnit timeUnit) {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.ttl(timeout, timeUnit);
		}
		return this;
	}

	@Override
	public CacheManager<T> supplier(Supplier<T> supplier) {
		CacheManager<T> internalManager = getInternalManager();
		// 保留一份，在内置管理器失效时使用。
		internalSupplier = supplier;
		if (internalManager != null) {
			internalManager.supplier(supplier);
		}
		return this;
	}

	@Override
	public <O extends CacheLoader> CacheManager<T> trigger(Trigger<O, String> trigger) {
		CacheManager<T> internalManager = getInternalManager();
		if (internalManager != null) {
			internalManager.trigger(trigger);
		}
		return this;
	}

	@Override
	public Supplier<T> getSupplier() {
		return internalSupplier;
	}

	@Override
	public T get() {
		CacheManager<T> internalManager = getInternalManager();
		/*
		 * 如果此时状态为失败, 意味着内置的管理器已经被GC回收掉了
		 * 此时正常走缓存失效的逻辑, 且不执行任何其他额外逻辑
		 */
		if (failure || internalManager == null) {
			return internalSupplier.get();
		}
		return internalManager.get();
	}

	private CacheManager<T> getInternalManager() {
		CacheManager<T> manager = weakCacheManager.get();
		if (manager == null) {
			failure = true;
		}
		return manager;
	}
}
