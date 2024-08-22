import com.serliunx.hummingcache.spring.annotation.EnableHummingCache;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author <a href="mailto:serliunx@yeah.net">SerLiunx</a>
 * @version 1.0.0
 * @since 2024/8/22
 */
public class SpringTests {

	@Test
	public void testSpring() {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(GlobalConfiguration.class);

		System.out.println(Arrays.toString(applicationContext.getBeanDefinitionNames()));
	}

	@Configuration
	@EnableHummingCache
	public static class GlobalConfiguration {

	}
}
