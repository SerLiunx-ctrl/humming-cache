package com.serliunx.hummingcache.core.manager;

import com.serliunx.hummingcache.core.support.Trigger;
import com.serliunx.hummingcache.core.key.KeyGenerator;
import com.serliunx.hummingcache.core.loader.CacheLoader;
import com.serliunx.hummingcache.core.loader.MapCacheLoader;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 缓存调度及管理
 * <li> 其与 {@link CacheLoader} 的区别:
 * <li> 前者仅起到调度的作用, 后者才是缓存的存储介质{@link MapCacheLoader}
 * <p>
 * <li> 重点：缓存并不仅仅可以用在标准的Controller的端点中, 它可以用在任何地方。只需要保证一个Java进程（JVM）
 * 中只有一个缓存池(这里指{@link CacheLoader}), 即保证每个{@link CacheManager}所构造的{@link CacheLoader}
 * 是同一个即可。不然缓存并不会生效
 * </p>
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 * @see CacheLoader
 * @see java.lang.ref.WeakReference
 */
public interface CacheManager<T> {

	/**
	 * 将本次调度器设置为可复用的
	 *
	 * @return this
	 */
	CacheManager<T> reusable();

	/**
	 * 将本次调度器设置为一次性
	 * <li> 一定程度上有助于垃圾回收, 取决于gc配置. 不一定有明显的效果.
	 * <li> 一旦设置为一次性后, 获取次数仅限于1次，超过即会报错{@link NullPointerException}
	 *
	 * @return this
	 * @see CacheManager#get()
	 */
	CacheManager<T> once();

	/**
	 * 设置键的生成规则
	 *
	 * @param generator 键生成规则
	 * @return this
	 */
	CacheManager<T> setKeyGenerator(KeyGenerator generator);

	/**
	 * 设置缓存的键
	 * <li> 不可为空!
	 *
	 * @param key 缓存键, 唯一
	 * @return this
	 */
	CacheManager<T> key(String key);

	/**
	 * 设置缓存的键
	 * <li> 此方法会解析类型与{xxx}的参数
	 * <li> 例如user_cache:{user_id}, 会替换掉参数中的user_id
	 *
	 * @param rawKey 键
	 * @param args 参数
	 * @return this
	 * @see KeyGenerator
	 */
	CacheManager<T> key(String rawKey, Object...args);

	/**
	 * 当获取缓存、设置缓存时出现异常的处理策略
	 * <li> 如果缓存数据具有一定的独立性时建议设置处理策略
	 *
	 * @param consumer 处理策略
	 * @return this
	 */
	CacheManager<T> whenException(Consumer<Throwable> consumer);

	/**
	 * 当缓存过期时所执行的额外操作
	 * <li> 注: 额外操作, 可以为空
	 * <li> 部分缓存载入器支持事件响应, 其余则只会在触发缓存获取且过期时执行.
	 *
	 * @param runnable 操作逻辑
	 * @return this
	 */
	CacheManager<T> whenExpired(Runnable runnable);

	/**
	 * 是否需要强制刷新缓存
	 * <li> 一般由接口、方法参数来决定, 大部分情况下和{@link CacheManager#onlyAbsent()} 是互斥的
	 * <li> 具体互斥的原因取决于onlyAbsent的条件判断结果, 详见onlyAbsent的说明
	 *
	 * @return this
	 */
	CacheManager<T> forceRefresh(boolean refresh);

	/**
	 * 当且仅当缓存中不存在该缓存时才执行缓存查询、更新等操作。
	 * <li> 当前仅当onlyAbsent和{@link CacheLoader#hasKey(String)}同时为真时才生效。
	 * <li> 判断优先级最高 {@link CacheManager#get()}
	 *
	 * @return this
	 */
	CacheManager<T> onlyAbsent();

	/**
	 * 启用时, 在更新缓存时同一个键在同一时间段内只能被一个线程(约等于请求)更新
	 * <li> 时间段: 请求的进入缓存的get方法的开始到结束. {@link CacheManager#get()}
	 * <li> !!! 当触发器生效或者强制更新缓存, 键锁会失效。此时会出现缓存被多次复写的情况
	 * <li> 大部分情况下是允许出现缓存复写的。
	 * <li> 最佳实践: 加锁的缓存建议用在瞬时并发量很大且速度较慢的接口上, 可以最大程度减少数据库的访问频率
	 * <li> 如果接口瞬时并发量并不高, 建议使用 {@link CacheManager#onlyAbsent()}。
	 *
	 * @return this
	 */
	CacheManager<T> lock();

	/**
	 * 当缓存过期时是否需要往缓存存储介质中刷入新数据
	 * <li> 参数化便于实现缓存精确控制
	 *
	 * @param needsRefresh 判断逻辑
	 * @return this
	 */
	CacheManager<T> needsRefresh(Function<T, Boolean> needsRefresh);

	/**
	 * 缓存存活时间
	 *
	 * @param timeout 时间 (通常有默认值, 取决于具体的实现)
	 * @param timeUnit 时间单位 (通常有默认值, 取决于具体的实现)
	 * @return this;
	 */
	CacheManager<T> ttl(long timeout, TimeUnit timeUnit);

	/**
	 * 缓存失效时的填充策略, 一般从数据库中查询
	 * <li> 不可为空!
	 *
	 * @param supplier 填充策略
	 * @return this
	 */
	CacheManager<T> supplier(Supplier<T> supplier);

	/**
	 * 设置缓存更新触发器
	 * <li> 请与{@link CacheManager#forceRefresh(boolean)}及{@link CacheManager#needsRefresh(Function)}区分开
	 *
	 * @param trigger 触发器
	 * @return this
	 * @param <O> 缓存存储介质
	 */
	<O extends CacheLoader> CacheManager<T> trigger(Trigger<O, String> trigger);

	/**
	 * 获取填充策略
	 *
	 * @return 填充策略
	 */
	Supplier<T> getSupplier();

	/**
	 * 获取缓存数据
	 * <li> 如果缓存管理器为不可复用时, 此方法仅运行调用一次
	 * <li> 详见 {@link CacheManager#reusable()} 及 {@link CacheManager#once()}
	 *
	 * @return 缓存数据
	 */
	 T get();
}
