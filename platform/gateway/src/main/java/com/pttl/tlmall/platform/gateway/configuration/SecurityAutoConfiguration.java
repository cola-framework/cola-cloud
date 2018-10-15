package com.pttl.tlmall.platform.gateway.configuration;

import com.pttl.tlmall.platform.gateway.security.cache.SecurityCacheManager;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jiachen.shi on 8/1/2017.
 */
@Configuration
public class SecurityAutoConfiguration {

    @Value("${spring.redis.expire:0}")
    private Long expiration;

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();

//        filterChainDefinitionMap.put("/health", "roles[aix]");//服务器健康状况页面
//        filterChainDefinitionMap.put("/info", "roles[aix]");//服务器信息页面
//        filterChainDefinitionMap.put("/env", "roles[aix]");//应用程序的环境变量
//        filterChainDefinitionMap.put("/metrics", "roles[aix]");
//        filterChainDefinitionMap.put("/configprops", "roles[aix]");

        //filterChainDefinitionMap.put("/**", "authc");


        shiroFilterFactoryBean.setUnauthorizedUrl("/error");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public CacheManager shiroCacheManager(RedisTemplate<?,?> redisTemplate){
        return new SecurityCacheManager(redisTemplate, expiration);
    }

    @Bean
    public SessionManager sessionManager(){
        return new ServletContainerSessionManager();
    }

    @Bean
    public SimpleCookie rememberMeCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    @Bean
    public RememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    @Bean
    public SecurityManager securityManager(CacheManager cacheManager, SessionManager sessionManager, RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(cacheManager);
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }
}
