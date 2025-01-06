package com.emily.infrastructure.aop.advisor;

import jakarta.annotation.Nonnull;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 切面增强类，增强类=切点+切面（拦截器advice）
 *
 * @author Emily
 * @since 2022/01/12
 */
public class AnnotationPointcutAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private final Advice advice;

    private final Pointcut pointcut;

    public AnnotationPointcutAdvisor(MethodInterceptor interceptor, Pointcut pointcut) {
        this.advice = interceptor;
        this.pointcut = pointcut;
    }

    @Nonnull
    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Nonnull
    @Override
    public Advice getAdvice() {
        return this.advice;
    }


    @Override
    public void setBeanFactory(@Nonnull BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware adviceAware) {
            adviceAware.setBeanFactory(beanFactory);
        }
    }

}
