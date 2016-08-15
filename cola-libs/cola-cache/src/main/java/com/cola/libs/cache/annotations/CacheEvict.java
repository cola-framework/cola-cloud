package com.cola.libs.cache.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * cola
 * Created by jiachen.shi on 8/14/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface CacheEvict {

    String area() default "";

    String areaGenerator() default "";

    String key() default "";

    String keyGenerator() default "";

}
