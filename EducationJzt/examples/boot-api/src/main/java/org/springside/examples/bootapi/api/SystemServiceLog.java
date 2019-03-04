package org.springside.examples.bootapi.api;

import java.lang.annotation.*;

/**
 * @Description: 定义注解，拦截service
 * @Author: vesus
 * @CreateDate: 2018/5/20 上午10:54
 * @Version: 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface SystemServiceLog {
    //定义成员
    String decription() default "" ;
}
