package com.kigoss.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by kigoss on 2017/7/4.
 */
public class TokenCache {

    private static Logger logger= LoggerFactory.getLogger(TokenCache.class);

    //LRU算法
    private static LoadingCache<String,String> loadingCache= CacheBuilder.newBuilder().
            initialCapacity(1000).
            maximumSize(10000).
            expireAfterWrite(12, TimeUnit.HOURS).
            build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void setKey(String key,String value){
        loadingCache.put(key,value);
    }

    public static String getKey(String key){
        String value=null;
        try {
            value=loadingCache.get(key);
            if("null".equals(value)){
                return null;
            }
            return value;
        } catch (Exception e) {
            logger.debug("localCache get error",e);
        }
        return null;
    }
}
