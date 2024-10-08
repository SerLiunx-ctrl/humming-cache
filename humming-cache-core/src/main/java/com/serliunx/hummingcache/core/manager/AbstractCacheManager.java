package com.serliunx.hummingcache.core.manager;

import com.serliunx.hummingcache.core.exception.DefaultExceptionHandler;
import com.serliunx.hummingcache.core.support.Trigger;
import com.serliunx.hummingcache.core.key.KeyGenerator;
import com.serliunx.hummingcache.core.key.PlaceholdersKeyGenerator;
import com.serliunx.hummingcache.core.loader.CacheLoader;
import com.serliunx.hummingcache.core.support.Assert;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 缓存调度及管理抽象实现, 定义公共逻辑
 * <li> 详细信息见 {@link CacheManager}
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.1.0
 * @since 2024/8/13
 */
public abstract class AbstractCacheManager<T> implements CacheManager<T> {

	protected static final KeyGenerator PKG = new PlaceholdersKeyGenerator();
	protected static final Consumer<Throwable> DEFAULT_EXCEPTION_HANDLER = new DefaultExceptionHandler();

	protected long timeout = 3;
	protected boolean reuse = false;
	protected boolean forceRefresh = false;
	protected boolean onlyAbsent = false;
	protected boolean keyLock = false;
	protected TimeUnit timeUnit = TimeUnit.MINUTES;
	protected String key;
	protected Supplier<T> supplier;
	protected Function<T, Boolean> needsRefresh;
	protected Consumer<Throwable> exceptionHandler;
	protected Trigger<CacheLoader, String> trigger;
	protected Runnable expirationHandler;

	protected final CacheLoader cacheLoader;

	public AbstractCacheManager(CacheLoader cacheLoader) {
		Assert.notNull(cacheLoader, "CacheLoader can not be null");
		this.cacheLoader = cacheLoader;
	}

	@Override
	public CacheManager<T> reusable() {
		this.reuse = true;
		return this;
	}

	@Override
	public CacheManager<T> once() {
		this.reuse = false;
		return this;
	}

	@Override
	public CacheManager<T> setKeyGenerator(KeyGenerator generator) {
		// 该实现的键生成器是静态成员, 无需设置.
		return this;
	}

	@Override
	public CacheManager<T> key(String key) {
		this.key = key;
		return this;
	}

	@Override
	public CacheManager<T> key(String rawKey, Object... args) {
		this.key = PKG.generate(rawKey, args);
		return this;
	}

	@Override
	public CacheManager<T> whenException(Consumer<Throwable> consumer) {
		this.exceptionHandler = consumer;
		return this;
	}

	@Override
	public CacheManager<T> whenExpired(Runnable runnable) {
		this.expirationHandler = runnable;
		return this;
	}

	@Override
	public CacheManager<T> forceRefresh(boolean refresh) {
		this.forceRefresh = refresh;
		return this;
	}

	@Override
	public CacheManager<T> onlyAbsent() {
		onlyAbsent = true;
		return this;
	}

	@Override
	public CacheManager<T> lock() {
		keyLock = true;
		return this;
	}

	@Override
	public CacheManager<T> needsRefresh(Function<T, Boolean> needsRefresh) {
		this.needsRefresh = needsRefresh;
		return this;
	}

	@Override
	public CacheManager<T> ttl(long timeout, TimeUnit timeUnit) {
		this.timeout = timeout;
		this.timeUnit = timeUnit;
		return this;
	}

	@Override
	public CacheManager<T> supplier(Supplier<T> supplier) {
		this.supplier = supplier;
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <O extends CacheLoader> CacheManager<T> trigger(Trigger<O, String> trigger) {
		this.trigger = (Trigger<CacheLoader, String>) trigger;
		return this;
	}

	@Override
	public Supplier<T> getSupplier() {
		return supplier;
	}

	/**
	 * 获取缓存键锁
	 */
	protected Lock getLock() {
		return cacheLoader.lock(key);
	}

	/**
	 * 检查是否需要锁定
	 * <li> 需要锁定时会尝试加锁
	 */
	protected void tryLock() {
		Lock lock = getLock();
		if (lock != null) {
			lock.lock();
		}
	}

	/**
	 * 检查是否需要释放锁
	 * <li> 需要锁定时会释放锁
	 */
	protected void tryUnlock() {
		Lock lock = getLock();
		if (lock != null) {
			lock.unlock();
		}
	}

	/**
	 * 异常处理
	 */
	protected void runExceptionHandler(Throwable throwable) {
		if (exceptionHandler != null) {
			exceptionHandler.accept(throwable);
		} else {
			DEFAULT_EXCEPTION_HANDLER.accept(throwable);
		}
	}

	/**
	 * 缓存过期逻辑
	 */
	protected void runExpirationHandler() {
		if (expirationHandler != null) {
			expirationHandler.run();
		}
	}

	/**
	 * 善后
	 */
	protected void postProcess() {
		if (reuse) {
			return;
		}
		timeUnit = null;
		key = null;
		supplier = null;
		trigger = null;
		needsRefresh = null;
		expirationHandler = null;
		exceptionHandler = null;
	}
}
