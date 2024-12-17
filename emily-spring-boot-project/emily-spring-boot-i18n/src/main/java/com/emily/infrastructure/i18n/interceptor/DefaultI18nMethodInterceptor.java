package com.emily.infrastructure.i18n.interceptor;

import com.emily.infrastructure.aop.constant.AopOrderInfo;
import com.emily.infrastructure.common.constant.HeaderInfo;
import com.emily.infrastructure.i18n.annotation.I18nOperation;
import com.emily.infrastructure.language.convert.I18nUtils;
import com.emily.infrastructure.language.convert.LanguageType;
import com.otter.infrastructure.servlet.RequestUtils;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Objects;

/**
 * 多语言拦截器
 *
 * @author :  Emily
 * @since :  2024/10/31 上午10:21
 */
public class DefaultI18nMethodInterceptor implements I18nCustomizer {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //执行结果
        Object response = invocation.proceed();
        if (Objects.isNull(response)) {
            return null;
        }
        I18nOperation annotation = invocation.getMethod().getAnnotation(I18nOperation.class);
        //语言类型
        LanguageType languageType = LanguageType.getByCode(RequestUtils.getHeader(HeaderInfo.LANGUAGE));
        // 如果是字符串直接转换
        if (response instanceof String value) {
            return I18nUtils.doGetProperty(value, languageType);
        }
        //将结果翻译为指定语言类型
        return I18nUtils.translate(response, languageType, annotation.removePackClass());

    }

    @Override
    public int getOrder() {
        return AopOrderInfo.I18N + 1;
    }
}
