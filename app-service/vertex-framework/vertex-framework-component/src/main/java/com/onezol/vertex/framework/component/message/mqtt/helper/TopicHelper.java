package com.onezol.vertex.framework.component.message.mqtt.helper;

public class TopicHelper {

    /**
     * MQTT Topic匹配判断： 判断传入的topic是否匹配模板
     * 说明：
     * 1. 主题层级分隔符 “/”
     * 2. 单层通配符 “+”
     * 3. 多层通配符 “#”
     * /server/# 可以匹配 /server/imei/a0001、 /server/imei/a0002、 /server/imei
     * /sys/+/+/thing/event 可以匹配 /sys/product1/device1/thing/event 和 /sys/product1/device2/thing/event
     *
     * @param topic   主题
     * @param pattern 主题模板
     * @return 是否匹配
     */
    public static boolean match(String topic, String pattern) {
        if (topic == null || pattern == null) return false;
        if (topic.equals(pattern)) return true;

        String[] topicParts = topic.split("/");
        String[] patternParts = pattern.split("/");

        // 检查多层通配符#的位置（必须单独存在且在最后）
        for (int i = 0; i < patternParts.length - 1; i++) {
            if (patternParts[i].equals("#")) return false;
        }

        // 长度必须相同（除非模式以#结尾）
        if (!pattern.endsWith("#") && topicParts.length != patternParts.length) {
            return false;
        }

        for (int i = 0; i < patternParts.length; i++) {
            if (patternParts[i].equals("#")) return true;
            if (i >= topicParts.length) return false;
            if (!patternParts[i].equals("+") && !patternParts[i].equals(topicParts[i])) {
                return false;
            }
        }

        return topicParts.length == patternParts.length;
    }

}
