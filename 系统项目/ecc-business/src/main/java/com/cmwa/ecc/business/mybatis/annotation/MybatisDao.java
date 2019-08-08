package com.cmwa.ecc.business.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * MyBatis Mapper Annotation
 * 
 * @see org.mybatis.spring.mapper.MapperScannerConfigurer#setAnnotationClass(Class)
 * @see conf-spring\common\spring-context-template.xml  </br>
 * 
 * <PRE>
 * &lt;bean class="org.mybatis.spring.mapper.MapperScannerConfigurer"&gt; </br>
 * 	&lt;property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" /&gt; </br>
 * 	&lt;!-- 扫描该目录下有@MybatisDao注解的dao接口 --&gt; </br>
 * 	&lt;property name="basePackage" value="com.cmwa.t.**.dao" /&gt; </br>
 * 	&lt;property name="annotationClass" value="com.cmwa.t.common.comp.mybatis.annotation.MybatisDao" /&gt; </br>
 * &lt;/bean&gt; </br>
 * </PRE>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MybatisDao {

}
