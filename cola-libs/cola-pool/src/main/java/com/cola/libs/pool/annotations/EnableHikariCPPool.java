package com.cola.libs.pool.annotations;

import com.cola.libs.pool.configuration.HikariCPConfiguration;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * cola
 * Created by jiachen.shi on 8/8/2016.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(HikariCPConfiguration.class)
@Documented
public @interface EnableHikariCPPool {
}
