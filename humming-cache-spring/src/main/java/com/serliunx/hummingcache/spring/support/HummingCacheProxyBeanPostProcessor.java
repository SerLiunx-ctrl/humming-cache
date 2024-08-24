package com.serliunx.hummingcache.spring.support;

import com.serliunx.hummingcache.spring.annotation.HummingCache;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 缓存注解代理对象处理器
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/23
 */
public class HummingCacheProxyBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> targetClass = bean.getClass();
		boolean hasAnnotation = Arrays.stream(targetClass.getMethods())
				.anyMatch(m -> m.isAnnotationPresent(HummingCache.class));

		if (!hasAnnotation) {
			return bean;
		}

		ProxyFactory proxyFactory = new ProxyFactory(bean);

		// 判断是否需要使用 CGLIB 代理
		boolean requiresCglibProxy = false;

		// 获取目标类实现的所有接口的方法
		Set<Method> interfaceMethods = new HashSet<>();
		for (Class<?> face : targetClass.getInterfaces()) {
			interfaceMethods.addAll(Arrays.asList(face.getMethods()));
		}

		// 检查 @HummingCache 注解的方法是否在接口中
		for (Method method : targetClass.getDeclaredMethods()) {
			if (method.isAnnotationPresent(HummingCache.class)) {
				// 如果注解的方法没有在接口中定义，则需要使用 CGLIB 代理
				if (!interfaceMethods.contains(method)) {
					requiresCglibProxy = true;
					break;
				}
			}
		}

		if (requiresCglibProxy || targetClass.getInterfaces().length == 0) {
			// 如果没有接口或者需要 CGLIB 代理
			proxyFactory.setProxyTargetClass(true); // 使用 CGLIB 代理
		}

		// 添加 AOP 逻辑
		proxyFactory.addAdvice((org.aopalliance.intercept.MethodInterceptor) invocation -> {
			Method method = invocation.getMethod();
			if (method.isAnnotationPresent(HummingCache.class)) {
				// 处理缓存逻辑
				System.out.println("Caching logic applied before method: " + method.getName());
			}
			return invocation.proceed();
		});

		return proxyFactory.getProxy();
	}
}
