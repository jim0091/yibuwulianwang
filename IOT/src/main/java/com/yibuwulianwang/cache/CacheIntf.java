package com.yibuwulianwang.cache;

import java.util.Map;
import java.util.Set;

public interface CacheIntf {
	
	CacheIntf createCache();
	
	/***
	 * 添加缓存
	 * @param key
	 * @param obj
	 */
	void putCache(String key,Object obj);
	/***
	 * 获取缓存
	 * @param key
	 * @return
	 */
	Object getCache(String key);
	
	/***
	 * 获取全部缓存
	 * @return
	 */
	Map<String, Object> getAllCache();
	/***
	 * 判断是否在缓存中
	 * @param key
	 * @return
	 */
	boolean isContains(String key);
	
	/***
	 * 清除所有缓存
	 */
	void clearAllCache();
	
	/**
     * 清除对应缓存
     * @param key
     */
    void clearCache(String key);
    
    /**
     * 缓存是否超时失效
     * @param key
     * @return
     */
    boolean isCacheTimeOut(String key);

    /**
     * 获取所有key
     * @return
     */
    Set<String> getAllCacheKeys();
    
    /***
     * 
     * 刷新缓存
     * @param key
     * @param obj
     */
    void updataCache(String key,Object obj);
}
