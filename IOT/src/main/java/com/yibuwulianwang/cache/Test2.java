package com.yibuwulianwang.cache;

public class Test2 {
	public static void main(String[] args) {
		CacheIntf cache = new CacheImpl().createCache();
		cache.putCache("put1", "put1");
		cache.putCache("put2", "put2");
		cache.putCache("put3", "put3");
		cache.putCache("put4", "put4");
		
		System.out.println(cache.getCache("put4"));
		cache.clearCache("put4");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cache.updataCache("put4", "new put4");
		System.out.println(cache.getCache("put4"));
		System.out.println(cache.getCache("put3"));
		cache.clearAllCache();
		System.out.println(cache.getCache("put3"));
	}
}
