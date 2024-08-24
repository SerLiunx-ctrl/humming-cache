import com.serliunx.hummingcache.core.loader.CacheLoader;
import com.serliunx.hummingcache.spring.annotation.EnableHummingCache;
import com.serliunx.hummingcache.spring.annotation.HummingCache;
import org.junit.Test;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * spring综合测试
 *
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/22
 */
public class SpringTests {

	@Test
	public void testSpring() {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(GlobalConfiguration.class);

		CacheService cacheService = (CacheService)applicationContext.getBean("cacheService");
		System.out.println(cacheService.getClass());

		cacheService.getCache();
		cacheService.getCache2();
	}

	@Configuration
	@EnableHummingCache
	public static class GlobalConfiguration {

		@Bean
		public CacheService cacheService() {
			return new CacheService();
		}
	}

	@Component
	public static class CacheService implements InitializingBean {

		@Autowired(required = false)
		private List<CacheLoader> cacheLoaders = new ArrayList<>();

		@Override
		public void afterPropertiesSet() throws Exception {
			cacheLoaders.forEach(System.out::println);
		}

		@HummingCache
		public void getCache() {

		}

		@HummingCache
		public void getCache2() {

		}
	}
}
