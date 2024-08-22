package com.serliunx.hummingcache.loader;

import com.serliunx.hummingcache.lock.Lockable;

import java.util.concurrent.TimeUnit;

/**
 * 缓存载入、存储介质
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/13
 */
public interface CacheLoader extends Lockable {

	/**
	 * 获取指定键下的缓存
	 * <li> key 不能为空
	 * <li> 过期时返回null
	 *
	 * @param key 键
	 * @return 值
	 * @param <T> 类型, 取决于接受返回值的引用类型.
	 */
	<T> T get(String key);

	/**
	 * 设置缓存
	 * <li> 会覆盖相同键的缓存
	 *
	 * @param key 键
	 * @param val 值
	 * @param timeout 存活时间
	 * @param timeUnit 存活时间单位
	 * @param <T> 类型
	 * @return 返回真代表之前有缓存未过期，旧的被覆盖了. 返回假时表示没有任何的覆盖行为, 即之前没有相同键的缓存
	 */
	<T> boolean put(String key, T val, long timeout, TimeUnit timeUnit);

	/**
	 * 强制移除指定键的缓存值
	 * <li> 忽略缓存的存活状态
	 *
	 * @param key 键
	 */
	void remove(String key);

	/**
	 * 如果缓存还存活则移除
	 *
	 * @param key 键
	 */
	void removeIfAlive(String key);

	/**
	 * 移除已经过期的缓存
	 * <li> 效果取决于具体的实现
	 *
	 * @param key 键
	 */
	void removeIfExpired(String key);

	/**
	 * 获取指定键的缓存的存活时间
	 *
	 * @param key 键
	 * @return 时间(毫秒), 指定键的缓存不存在或者已过期则返回-1
	 */
	long ttl(String key);

	/**
	 * 增加指定缓存的存活时间
	 *
	 * @param key 缓存键
	 * @param time 时长
	 * @param timeUnit 时间单位
	 */
	void addTtl(String key, long time, TimeUnit timeUnit);

	/**
	 * 查看一个键的缓存是否存活
	 *
	 * @param key 缓存键
	 * @return 存活返回真, 否则返回假。大多数实现会在调用该方法时触发过期
	 * <li> 关于过期机制见具体的实现
	 */
	boolean isAlive(String key);

	/**
	 * 查看缓存中是否包含指定键
	 * <li> 键存在不等于缓存还没过期, 取决于具体的实现.
	 *
	 * @param key 键
	 * @return 存在指定的键返回真, 否则返回假
	 */
	boolean hasKey(String key);
}
