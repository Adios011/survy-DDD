package org.survy.domain.applicationservice.ports.output.cache;

import java.util.concurrent.TimeUnit;

public interface CacheService<T> {

    void save(String key, T object);
    void save(String key, T object , long timeout , TimeUnit timeUnit);
    T get(String key);
    void delete(String key);
}
