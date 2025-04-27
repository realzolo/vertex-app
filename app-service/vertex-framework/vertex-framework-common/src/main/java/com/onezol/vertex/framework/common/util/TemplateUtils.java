package com.onezol.vertex.framework.common.util;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * 模板工具类
 */
public final class TemplateUtils extends org.springframework.util.ResourceUtils {

    private TemplateUtils() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 处理模板字符串
     *
     * @param template       字符串模板
     * @param variableObject 变量对象
     */
    public static String processWithSpEL(String template, Object variableObject) {
        ExpressionParser parser = new SpelExpressionParser();
        TemplateParserContext context = new TemplateParserContext();
        Expression exp = parser.parseExpression(template, context);
        return exp.getValue(variableObject, String.class);
    }

}
