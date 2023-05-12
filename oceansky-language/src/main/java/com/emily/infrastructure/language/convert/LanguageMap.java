package com.emily.infrastructure.language.convert;

import com.emily.infrastructure.language.i18n.I18nChineseHelper;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @Description :  异常多语言缓存
 * @Author :  Emily
 * @CreateDate :  Created in 2022/8/9 7:38 下午
 */
public class LanguageMap {
    /**
     * 简体-繁体
     */
    private static Map<String, String> zhMap = Maps.newHashMap();
    /**
     * 简体-英文
     */
    private static Map<String, String> enMap = Maps.newHashMap();

    /**
     * 简体-繁体 绑定
     *
     * @param simple      简体
     * @param traditional 繁体
     */
    public static void bindZh(String simple, String traditional) {
        zhMap.put(simple, traditional);
    }

    /**
     * 简体-繁体 绑定
     */
    public static void bindZh(Map<String, String> zhCache) {
        zhMap.putAll(zhCache);
    }

    /**
     * 简体-英文 绑定
     *
     * @param simple 简体
     * @param en     英文
     */
    public static void bindEn(String simple, String en) {
        enMap.put(simple, en);
    }

    /**
     * 简体-英文 绑定
     */
    public static void bindEn(Map<String, String> enCache) {
        enMap.putAll(enCache);
    }

    /**
     * 获取简体中文对应的语言
     */
    public static String acquire(String simple) {
        String language = LanguageType.ZH_CN.getCode();
        //if (RequestUtils.isServlet()) {
        // language = RequestUtils.getRequest().getHeader(HeaderInfo.LANGUAGE);
        // }
        return acquire(simple, language);
    }

    /**
     * 获取简体中文对应的语言
     */
    public static String acquire(String simple, String language) {
        LanguageType languageType = LanguageType.getByCode(language);
        return acquire(simple, languageType);
    }

    /**
     * 获取简体中文对应的语言
     */
    public static String acquire(String simple, LanguageType languageType) {
        if (Objects.isNull(languageType) || StringUtils.isEmpty(simple)) {
            return simple;
        }
        if (StringUtils.length(simple) > 1000) {
            return simple;
        }
        if (languageType.equals(LanguageType.ZH_TW)) {
            return zhMap.containsKey(simple) ? zhMap.get(simple) : I18nChineseHelper.convertToTraditionalChinese(simple);
        }
        if (languageType.equals(LanguageType.EN_US)) {
            return enMap.containsKey(simple) ? enMap.get(simple) : simple;
        }
        return simple;
    }

}