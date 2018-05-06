package com.yibuwulianwang.cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.Cache;

public class CacheImpl implements CacheIntf {
	private static Map<String, Object> caches = new ConcurrentHashMap<String, Object>();
	
	private static long CREATETIME=0;
	
	public CacheIntf createCache() {
		CREATETIME=System.currentTimeMillis();
		return new CacheImpl();
	}

	public void putCache(String key, Object obj) {
		caches.put(key, obj);
	}

	public Object getCache(String key) {
		if(this.isContains(key)) {
			return caches.get(key);
		}
		return null;
	}

	public Map<String, Object> getAllCache() {
		return caches;
	}

	public boolean isContains(String key) {
		return caches.containsKey(key);
	}

	public void clearAllCache() {
		caches.clear();
	}

	public void clearCache(String key) {
		if (this.isContains(key)) {
			caches.remove(key);
		}
	}

	public boolean isCacheTimeOut(String key) {
		if (!caches.containsKey(key)) {
			return true;
		}
		if(System.currentTimeMillis()-CREATETIME>CacheClear.OUTTIME) {
			return true;
		}
		return false;
	}

	public Set<String> getAllCacheKeys() {
		return caches.keySet();
	}

	public void updataCache(String key, Object obj) {
		if(this.isContains(key)) {
			caches.replace(key, obj);		
		}else {
			caches.put(key, obj);
		}
	}

}
