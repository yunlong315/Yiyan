package com.example.administration.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 数据缓存至Redis
 */
public interface CacheService {

    /**
     * 数据缓存至Redis
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    <K, V> void add(K key, V value);

    /**
     * 数据缓存至Redis并设置过期时间
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    <K, V> void add(K key, V value, long timeout, TimeUnit unit);

    /**
     * 写入哈希集合，并设置过期时间
     *
     * @param key    缓存键
     * @param subKey 子键
     * @param value  写入的值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    <K, SK, V> void addHashCache(K key, SK subKey, V value, long timeout, TimeUnit unit);

    /**
     * 写入哈希集合
     *
     * @param key    缓存键
     * @param subKey 子键
     * @param value  写入的值
     */
    <K, SK, V> void addHashCache(K key, SK subKey, V value);

    /**
     * 获取哈希集合中的值
     *
     * @param key    缓存键
     * @param subKey 子键
     * @return 对应的缓存值
     */
    <K, SK> Object getHashCache(K key, SK subKey);

    /**
     * 从Redis中获取缓存数据并转成对象
     *
     * @param key   缓存键
     * @param clazz 对象类型
     * @return 缓存值对象
     */
    <K, V> V getObject(K key, Class<V> clazz);

    /**
     * 从Redis中获取缓存数据并转成List
     *
     * @param key   缓存键
     * @param clazz 对象类型
     * @return 缓存值List
     */
    <K, V> List<V> getList(K key, Class<V> clazz);

    /**
     * 获取缓存数据
     *
     * @param key 缓存键
     * @return 缓存值
     */
    <K> String get(K key);

    /**
     * 删除缓存数据
     *
     * @param key 缓存键
     */
    void delete(String key);

    /**
     * 批量删除缓存数据
     *
     * @param keys 缓存键集合
     */
    void delete(Collection<String> keys);

    /**
     * 序列化缓存键
     *
     * @param key 缓存键
     * @return 序列化后的字节数组
     */
    byte[] dump(String key);

    /**
     * 判断缓存键是否存在
     *
     * @param key 缓存键
     * @return 如果缓存键存在，则返回true，否则返回false
     */
    Boolean hasKey(String key);

    /**
     * 设置缓存键的过期时间
     *
     * @param key     缓存键
     * @param timeout 过期时间
     * @param unit    时间单位
     * @return 如果设置成功，则返回true，否则返回false
     */
    Boolean expire(String key, long timeout, TimeUnit unit);

    /**
     * 设置缓存键的过期时间
     *
     * @param key  缓存键
     * @param date 过期时间
     * @return 如果设置成功，则返回true，否则返回false
     */
    Boolean expireAt(String key, Date date);

    /**
     * 移除缓存键的过期时间，使其持久保持
     *
     * @param key 缓存键
     * @return 如果移除成功，则返回true，否则返回false
     */
    Boolean persist(String key);

    /**
     * 获取缓存键的剩余过期时间
     *
     * @param key  缓存键
     * @param unit 时间单位
     * @return 缓存键的剩余过期时间
     */
    Long getExpire(String key, TimeUnit unit);

    /**
     * 获取缓存键的剩余过期时间
     *
     * @param key 缓存键
     * @return 缓存键的剩余过期时间
     */
    Long getExpire(String key);
}