/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.maxmind.db.spring.boot.cache;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.maxmind.db.CacheKey;
import com.maxmind.db.DecodedValue;
import com.maxmind.db.NodeCache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Caffeine-based NodeCache implementation for MaxMind database.
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
public class CaffeineNodeCache implements NodeCache {

    private static final Logger log = LoggerFactory.getLogger(CaffeineNodeCache.class);

    private static final int DEFAULT_CAPACITY = 4096;
    private static final int DEFAULT_MAXIMUM_SIZE = DEFAULT_CAPACITY * 8;
    private static final int DEFAULT_EXPIRE_DURATION = 1;
    private static final TimeUnit DEFAULT_EXPIRE_UNIT = TimeUnit.HOURS;
    
    private final int capacity;
    private final Cache<Object, Object> cache;
    private boolean cacheFull = false;

    public CaffeineNodeCache() {
        this(DEFAULT_CAPACITY, DEFAULT_MAXIMUM_SIZE, DEFAULT_EXPIRE_DURATION, DEFAULT_EXPIRE_UNIT);
    }
    
    public CaffeineNodeCache(int capacity, int maximumSize, long duration, TimeUnit unit) {
        this.capacity = capacity;
        this.cache = Caffeine.newBuilder()
        		.initialCapacity(capacity)
        		.maximumSize(maximumSize)
                .expireAfterWrite(duration, unit)
                .expireAfterAccess(duration, unit)
        		.evictionListener(new RemovalListener<Object, Object>() {

					@Override
					public void onRemoval(Object key, Object value, RemovalCause cause) {
						log.debug("Remove Cache : {}", key);
					}
        			
				})
        		.build();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public DecodedValue get(CacheKey key, Loader loader) throws IOException {
        Object value = cache.getIfPresent(key);
        if (value == null) {
            value = loader.load(key);
            if (!cacheFull) {
                if (cache.estimatedSize() < capacity) {
                    cache.put(key, value);
                } else {
                    cacheFull = true;
                }
            }
        }
        return (DecodedValue) value;
    }

}
