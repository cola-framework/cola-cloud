package com.cola.libs.session.configuration;

import com.cola.libs.sessioin.interceptor.SessionContextInterceptor;
import com.cola.libs.session.error.controller.ApplicationErrorController;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.List;

/**
 * Created by jiachen.shi on 7/24/2017.
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore({ErrorMvcAutoConfiguration.class})
public class WebApplicationAutoConfiguration extends WebMvcConfigurerAdapter{

    private final ServerProperties serverProperties;
    private final List<ErrorViewResolver> errorViewResolvers;

    @Value("${website.language.default:}")
    private String defaultLanguage;

    public WebApplicationAutoConfiguration(ServerProperties serverProperties, ObjectProvider<List<ErrorViewResolver>> errorViewResolversProvider) {
        this.serverProperties = serverProperties;
        this.errorViewResolvers = (List)errorViewResolversProvider.getIfAvailable();
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        if(!StringUtils.isEmpty(defaultLanguage)){
            sessionLocaleResolver.setDefaultLocale(StringUtils.parseLocaleString(defaultLanguage));
        }
        return sessionLocaleResolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(new SessionContextInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Bean
    public ApplicationErrorController applicationErrorController(ErrorAttributes errorAttributes) {
        return new ApplicationErrorController(errorAttributes, this.serverProperties.getError(), this.errorViewResolvers);
    }

}
