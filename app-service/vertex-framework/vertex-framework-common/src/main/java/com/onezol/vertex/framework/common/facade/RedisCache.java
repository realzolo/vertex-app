package com.onezol.vertex.framework.common.facade;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisCache {

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    <T> void setCacheObject(final String key, final T value);

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit);

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    boolean expire(final String key, final long timeout);

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    boolean expire(final String key, final long timeout, final TimeUnit unit);

    /**
     * 获取有效时间
     *
     * @param key Redis键
     * @return 有效时间
     */
    long getExpire(final String key);

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    Boolean hasKey(String key);

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    <T> T getCacheObject(final String key);

    /**
     * 删除单个对象
     *
     * @param key 键
     */
    boolean deleteObject(final String key);

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return 是否删除成功
     */
    boolean deleteObject(final Collection collection);

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    <T> long setCacheList(final String key, final List<T> dataList);

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    <T> List<T> getCacheList(final String key);

    /**
     * 获得缓存列表
     *
     * @param keys 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(final List<String> keys);

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
//    <T> BoundSetOperations<String, T> setCacheSet(final String key, final Set<T> dataSet);

    /**
     * 获得缓存的set
     *
     * @param key 键
     * @return 值
     */
    <T> Set<T> getCacheSet(final String key);

    /**
     * 缓存有序集合
     *
     * @param key    缓存键值
     * @param values 缓存的数据及其分值
     * @return 缓存数据的对象
     */
//    <T> BoundZSetOperations<String, T> setCacheZSet(final String key, final Map<T, Double> values);

    /**
     * 获得缓存的有序集合
     *
     * @param key   键
     * @param start ZRANGE 命令的开始索引
     * @param end   ZRANGE 命令的结束索引
     * @return 有序集合的成员和分值
     */
    <T> Set<T> getCacheZSet(final String key, long start, long end);


    /**
     * 删除有序集合中的元素
     *
     * @param key    键
     * @param values 值
     */
    void deleteZSet(final String key, final Object... values);

    /**
     * 缓存Map
     *
     * @param key     键
     * @param dataMap 值
     */
    <T> void setCacheMap(final String key, final Map<String, T> dataMap);

    /**
     * 获得缓存的Map
     *
     * @param key 键
     * @return 值
     */
    <T> Map<String, T> getCacheMap(final String key);

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    <T> void setCacheMapValue(final String key, final String hKey, final T value);

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    <T> T getCacheMapValue(final String key, final String hKey);

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys);

    /**
     * 删除Hash中的某条数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return 是否成功
     */
    boolean deleteCacheMapValue(final String key, final String hKey);

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    Collection<String> keys(final String pattern);


    /**
     * 获取列表(List)的长度
     *
     * @param key 键
     * @return 列表的长度
     */
    Long getListSize(final String key);

    /**
     * 获取集合(Set)的元素数量
     *
     * @param key 键
     * @return 集合的元素数量
     */
    Long getSetSize(final String key);

    /**
     * 获取有序集合(ZSet)的成员数量
     *
     * @param key 键
     * @return 有序集合的成员数量
     */
    Long getZSetSize(final String key);

    /**
     * 获取Hash中的项数
     *
     * @param key 键
     * @return Hash中的项数
     */
    Long getMapSize(final String key);
}
