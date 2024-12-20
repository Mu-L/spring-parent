package com.emily.infrastructure.sensitize.annotation;

import com.emily.infrastructure.sensitize.DesensitizeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义jackson注解，标注在属性上，字段必须是字符串类型
 * 1. 可以标记在String上、Map字段类型；
 * 2. 如果标记在Map字段上优先级低于{@link DesensitizeMapProperty}
 *
 * @author Emily
 * @since :  Created in 2022/7/19 5:22 下午
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DesensitizeProperty {
    /**
     * 脱敏类型，见枚举类型{@link DesensitizeType}
     *
     * @return 脱敏类型
     */
    DesensitizeType value() default DesensitizeType.DEFAULT;
}
