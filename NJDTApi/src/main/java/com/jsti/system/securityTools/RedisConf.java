package com.jsti.system.securityTools;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.lang.reflect.Method;
/**
 * 文件名：RedisConf
 * 版本信息：日期：2017/4/25 .
 * @EnableCaching：启用缓存功能 开启缓存功能，配置类中需要加上这个注解，
 * 有了这个注解之后，spring才知道你需要使用缓存的功能，其他和缓存相关的注解才会有效，
 * spring中主要是通过aop实现的，通过aop来拦截需要使用缓存的方法，实现缓存的功能。
 * @Cacheable：赋予缓存功能作用.
 * @Cacheable可以标记在一个方法上，也可以标记在一个类上。
 * 当标记在一个方法上时表示该方法是支持缓存的，当标记在一个类上时则表示该类所有的方法都是支持缓存的。
 * 对于一个支持缓存的方法，Spring会在其被调用后将其返回值缓存起来，
 * 以保证下次利用同样的参数来执行该方法时可以直接从缓存中获取结果，而不需要再次执行该方法。
 * Spring在缓存方法的返回值时是以键值对进行缓存的，
 * 值就是方法的返回结果，
 * 至于键的话，Spring又支持两种策略，默认策略和自定义策略。
 * 需要注意的是当一个支持缓存的方法在对象内部被调用时是不会触发缓存功能的。
 * @Cacheable可以指定三个属性，value、key和condition。
 */
@Configuration
@EnableCaching
public class RedisConf extends CachingConfigurerSupport{

    @Bean
    public KeyGenerator keyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();

                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };

    }

    @Bean
    public CacheManager cacheManager(
            @SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }

    @Bean
    @SuppressWarnings("rawtypes")
    public RedisSerializer fastJson2JsonRedisSerializer() {
        return new FastJson2JsonRedisSerializer<Object>(Object.class);
    }

    @Bean
    @SuppressWarnings("rawtypes")
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory, RedisSerializer fastJson2JsonRedisSerializer) {
        StringRedisTemplate template = new StringRedisTemplate(factory);

        template.setValueSerializer(fastJson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

}