package com.serliunx.hummingcache.core.loader.listner;

import java.util.concurrent.TimeUnit;

/**
 * 缓存监听器处理结果.
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/27
 */
public class Attachment {

	private long ttl;
	private TimeUnit timeUnit;
	private Object obj;

	public Attachment() {}

	public Attachment(long ttl, TimeUnit timeUnit, Object obj) {
		this.ttl = ttl;
		this.timeUnit = timeUnit;
		this.obj = obj;
	}

	public long getTtl() {
		return ttl;
	}

	public void setTtl(long ttl) {
		this.ttl = ttl;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
}
