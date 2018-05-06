package com.yibuwulianwang.cache;

import java.util.logging.Logger;

public class Test {
	static Logger logger = Logger.getLogger("Test");

	static ICacheManager cacheManagerImpl;
	
	static {
		cacheManagerImpl = new CacheManagerImpl();
	}

	public static void main(String[] args) {
		EntityCache entityCache = new EntityCache("isCache",0,System.currentTimeMillis());
		cacheManagerImpl.putCache("test", "is test", 0);
		cacheManagerImpl.putCache("entity",entityCache);
		cacheManagerImpl.putCache("myTest", "is myTest", 15 * 1000L);
		CacheListener cacheListener = new CacheListener(cacheManagerImpl);
		cacheListener.startListen();
		logger.info("test:" + cacheManagerImpl.getCacheByKey("test").getDatas());
		logger.info("myTest:" + cacheManagerImpl.getCacheByKey("myTest").getDatas());
		logger.info("entity:" + cacheManagerImpl.getCacheDataByKey("entity"));
	}

}
