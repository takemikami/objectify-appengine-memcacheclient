package com.github.takemikami.objectify.appengine;

import com.google.appengine.api.memcache.MemcacheService.CasValues;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.cache.IdentifiableValue;
import com.googlecode.objectify.cache.MemcacheService;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AppEngineMemcacheClientService implements MemcacheService {

  com.google.appengine.api.memcache.MemcacheService appengineMemcache;

  public AppEngineMemcacheClientService() {
    this.appengineMemcache = MemcacheServiceFactory.getMemcacheService("objectify");
  }

  @Override
  public Object get(String key) {
    return this.appengineMemcache.get(key);
  }

  @Override
  public Map<String, IdentifiableValue> getIdentifiables(Collection<String> keys) {
    Map<String, com.google.appengine.api.memcache.MemcacheService.IdentifiableValue> ivs
        = this.appengineMemcache.getIdentifiables(keys);
    return keys.stream().collect(Collectors.toMap((k -> k), (k ->
        new AppEngineIdentifiableValue(new CasValues(
            ivs.computeIfAbsent(k, iv -> {
              this.appengineMemcache.put(iv, null);
              return this.appengineMemcache.getIdentifiable(iv);
            }), null))
    )));
  }

  @Override
  public Map<String, Object> getAll(Collection<String> keys) {
    return this.appengineMemcache.getAll(keys);
  }

  @Override
  public void put(String key, Object thing) {
    this.appengineMemcache.put(key, thing);
  }

  @Override
  public void putAll(Map<String, Object> values) {
    this.appengineMemcache.putAll(values);
  }

  @Override
  public Set<String> putIfUntouched(Map<String, CasPut> values) {
    return this.appengineMemcache.putIfUntouched(
        values.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e ->
            new CasValues(
                ((AppEngineIdentifiableValue) e.getValue().getIv()).getCasValues()
                    .getOldValue(),
                e.getValue().getNextToStore())
        ))
    );
  }

  @Override
  public void deleteAll(Collection<String> keys) {
    this.appengineMemcache.deleteAll(keys);
  }

  public class AppEngineIdentifiableValue implements IdentifiableValue {

    private CasValues casValues;

    public AppEngineIdentifiableValue(CasValues casValues) {
      this.casValues = casValues;
    }

    @Override
    public Object getValue() {
      return this.casValues.getOldValue().getValue();
    }

    @Override
    public IdentifiableValue withValue(final Object value) {
      return new AppEngineIdentifiableValue(
          new CasValues(casValues.getOldValue(), casValues.getNewValue()));
    }

    public CasValues getCasValues() {
      return casValues;
    }
  }

}
