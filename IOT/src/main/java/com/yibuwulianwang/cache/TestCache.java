package com.yibuwulianwang.cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.junit.Test;

public class TestCache {
	Logger logger = Logger.getLogger("cacheLog");

	/**
	 * 测试缓存和缓存失效
	 */
	@Test
	public void testCacheManager() {
		ICacheManager cacheManagerImpl = new CacheManagerImpl();
		cacheManagerImpl.putCache("test", "is test", 0);
		cacheManagerImpl.putCache("myTest", "is myTest", 15 * 1000L);
		CacheListener cacheListener = new CacheListener(cacheManagerImpl);
		cacheListener.startListen();
		logger.info("test:" + cacheManagerImpl.getCacheByKey("test").getDatas());
		logger.info("myTest:" + cacheManagerImpl.getCacheByKey("myTest").getDatas());
		try {
			TimeUnit.SECONDS.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.info("test:" + cacheManagerImpl.getCacheByKey("test"));
		logger.info("myTest:" + cacheManagerImpl.getCacheByKey("myTest"));
	}

	/**
	 * 测试线程安全
	 */
	@Test
	public void testThredSafe() {
		final String key = "thread";
		final CacheManagerImpl cacheManagerImpl = new CacheManagerImpl();
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 100; i++) {
			exec.execute(new Runnable() {
				public void run() {
					if (!cacheManagerImpl.isContains(key)) {
						cacheManagerImpl.putCache(key, 1, 0);
					} else {
						// 因为+1和赋值操作不是原子性的，所以把它用synchronize块包起来
						synchronized (cacheManagerImpl) {
							int value = (Integer) cacheManagerImpl.getCacheDataByKey(key) + 1;
							cacheManagerImpl.putCache(key, value, 0);
						}
					}
				}
			});
		}
		exec.shutdown();
		try {
			exec.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		logger.info(cacheManagerImpl.getCacheDataByKey(key).toString());
	}
}
