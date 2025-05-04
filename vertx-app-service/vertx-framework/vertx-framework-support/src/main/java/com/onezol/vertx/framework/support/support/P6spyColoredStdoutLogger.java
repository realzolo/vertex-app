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
    // ANSI 颜色代码 (白色)
    private static final String ANSI_WHITE = "\u001B[37m";
    // ANSI 颜色代码 (亮绿色)
    private static final String ANSI_BRIGHT_GREEN = "\u001B[92m";
    // ANSI 颜色代码 (黄色)
    private static final String ANSI_YELLOW = "\u001B[33m";
    // ANSI 颜色代码 (亮黄色)
    private static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";

    @Override
    public void logText(String text) {
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (i == 0) {
                super.logText(ANSI_BRIGHT_YELLOW + lines[i] + ANSI_RESET);
            } else {
                super.logText(ANSI_BRIGHT_WHITE + lines[i] + ANSI_RESET);
            }
        }
    }

}
