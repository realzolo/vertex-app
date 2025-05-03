package com.onezol.vertx.framework.support.support;

import com.p6spy.engine.spy.appender.StdoutLogger;

/**
 * P6spy 日志颜色配置
 */
public class P6spyColoredStdoutLogger extends StdoutLogger {

    // ANSI 颜色代码
    private static final String ANSI_RESET = "\u001B[0m";
    // ANSI 颜色代码 (亮白色)
    private static final String ANSI_BRIGHT_WHITE = "\u001B[97m";

    @Override
    public void logText(String text) {
        super.logText(ANSI_BRIGHT_WHITE + text + ANSI_RESET);
    }

}
