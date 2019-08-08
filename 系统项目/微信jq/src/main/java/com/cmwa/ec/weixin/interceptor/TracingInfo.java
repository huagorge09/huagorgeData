package com.cmwa.ec.weixin.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * À¹½ØÆ÷×¢½â
 * 
 * @author jouislu
 * 
 */

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD)
public @interface TracingInfo {

    public boolean needCheckParams() default false;
    
    public String chekPrams() default "";
    
    public String redirectUrl() default "";
    
    public String charset() default "";
    
    public boolean needLogin() default false;
    
    public String authority() default "";
    
}
