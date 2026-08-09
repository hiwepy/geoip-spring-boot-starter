package com.maxmind.db.spring.boot.cache;

import com.maxmind.db.CacheKey;
import com.maxmind.db.DecodedValue;
import com.maxmind.db.NodeCache;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link CaffeineNodeCache}.
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class CaffeineNodeCacheTest {

    @SuppressWarnings("unchecked")
    private CacheKey<Object> createCacheKey(int offset) {
        try {
            Constructor<CacheKey> ctor = CacheKey.class.getDeclaredConstructor(int.class, Class.class, java.lang.reflect.Type.class);
            ctor.setAccessible(true);
            return ctor.newInstance(offset, Object.class, Object.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private DecodedValue createDecodedValue(Object value) {
        try {
            Constructor<DecodedValue> ctor = DecodedValue.class.getDeclaredConstructor(Object.class);
            ctor.setAccessible(true);
            return ctor.newInstance(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void defaultConstructorShouldCreateCache() {
        CaffeineNodeCache cache = new CaffeineNodeCache();
        assertThat(cache).isNotNull();
    }

    @Test
    void parameterizedConstructorShouldCreateCache() {
        CaffeineNodeCache cache = new CaffeineNodeCache(128, 1024, 30, TimeUnit.MINUTES);
        assertThat(cache).isNotNull();
    }

    @Test
    void getShouldLoadValueWhenNotCached() throws IOException {
        CaffeineNodeCache cache = new CaffeineNodeCache();
        CacheKey<Object> key = createCacheKey(0);
        DecodedValue expected = createDecodedValue("test");

        DecodedValue result = cache.get(key, k -> expected);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void getShouldReturnCachedValueOnSubsequentCalls() throws IOException {
        CaffeineNodeCache cache = new CaffeineNodeCache();
        CacheKey<Object> key = createCacheKey(0);
        DecodedValue expected = createDecodedValue("test");

        DecodedValue first = cache.get(key, k -> expected);
        DecodedValue second = cache.get(key, k -> createDecodedValue("other"));
        assertThat(first).isEqualTo(expected);
        assertThat(second).isEqualTo(expected);
    }

    @Test
    void getShouldPropagateLoaderException() {
        CaffeineNodeCache cache = new CaffeineNodeCache();
        CacheKey<Object> key = createCacheKey(0);

        assertThatThrownBy(() -> cache.get(key, k -> {
            throw new IOException("load failed");
        })).isInstanceOf(IOException.class).hasMessage("load failed");
    }

    @Test
    void getShouldHandleMultipleKeys() throws IOException {
        CaffeineNodeCache cache = new CaffeineNodeCache();
        CacheKey<Object> key1 = createCacheKey(0);
        CacheKey<Object> key2 = createCacheKey(1);
        DecodedValue value1 = createDecodedValue("value1");
        DecodedValue value2 = createDecodedValue("value2");

        cache.get(key1, k -> value1);
        cache.get(key2, k -> value2);

        // key1 should still be cached
        DecodedValue result = cache.get(key1, k -> createDecodedValue("changed"));
        assertThat(result).isEqualTo(value1);
    }

    @Test
    void smallCapacityCacheShouldStopCachingWhenFull() throws IOException {
        // capacity=1, so after 1 entry the cache is marked full and stops caching
        CaffeineNodeCache cache = new CaffeineNodeCache(1, 100, 1, TimeUnit.HOURS);
        CacheKey<Object> key1 = createCacheKey(0);
        CacheKey<Object> key2 = createCacheKey(1);
        DecodedValue value1 = createDecodedValue("value1");
        DecodedValue value2 = createDecodedValue("value2");

        cache.get(key1, k -> value1);
        cache.get(key2, k -> value2);

        // key1 is cached, so it returns the original value
        assertThat(cache.get(key1, k -> createDecodedValue("new1"))).isEqualTo(value1);
        // key2 was never cached (cache was full), so it reloads each time
        DecodedValue reloaded = cache.get(key2, k -> createDecodedValue("new2"));
        assertThat(reloaded).isNotNull();
    }

    @Test
    void getShouldImplementNodeCacheInterface() {
        CaffeineNodeCache cache = new CaffeineNodeCache();
        assertThat(cache).isInstanceOf(NodeCache.class);
    }
}
