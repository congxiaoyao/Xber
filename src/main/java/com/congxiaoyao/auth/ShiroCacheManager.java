package com.congxiaoyao.auth;

import net.sf.ehcache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;

/**
 * Created by Jayce on 2016/11/10.
 */
public class ShiroCacheManager extends EhCacheManager {
    public ShiroCacheManager() {
        setCacheManager(CacheManager.create());
    }
}
