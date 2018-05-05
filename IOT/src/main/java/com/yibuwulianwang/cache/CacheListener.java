package com.yibuwulianwang.cache;

import java.util.logging.Logger;

public class CacheListener {
	Logger logger = Logger.getLogger("cacheLog");
	
	private ICacheManager cacheManagerImpl;

	public CacheListener(ICacheManager cacheManagerImpl) {
		this.cacheManagerImpl = cacheManagerImpl;
	}
	
	static boolean isCacheRun = true;

	public static boolean isCacheRun() {
		return isCacheRun;
	}

	public static void setCacheRun(boolean isCacheRun) {
		CacheListener.isCacheRun = isCacheRun;
	}

	public void startListen() {
		new Thread() {
			public void run() {
				while (isCacheRun) {
					for (String key : cacheManagerImpl.getAllKeys()) {
						if (cacheManagerImpl.isTimeOut(key)) {
							cacheManagerImpl.clearByKey(key);
							logger.info(key + "缓存被清除");
						}
					}
				}
			}
		}.start();
	}
}
