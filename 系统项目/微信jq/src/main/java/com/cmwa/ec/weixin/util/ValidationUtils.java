package com.cmwa.ec.weixin.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.HibernateValidator;

/**
 * hibernate validation工具类
 * @author caolp
 *
 */
public class ValidationUtils {
	
	/**
     * 使用hibernate的注解来进行验证
     * 
     */
    private static Validator validator = Validation.byProvider(HibernateValidator.class)
    		.configure().failFast(true).buildValidatorFactory().getValidator();

    /**
     * 校验
     * @param obj 传入的校验对象
     * @return  校验结果message
     */
    public static <T> String validate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        // 抛出检验异常
        if (constraintViolations.size() > 0) {
            return String.format("参数校验失败:%s", constraintViolations.iterator().next().getMessage());
        } 
        return null;
    }
}
