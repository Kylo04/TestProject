package qa.automation.redis;

import com.google.gson.Gson;
import redis.clients.jedis.*;
import qa.automation.config.Config;

import java.util.Map;

public class RedisClient {

    private static final JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
            .timeoutMillis(5000)
            .password(Config.redisPassword() != null && !Config.redisPassword().isBlank() ? Config.redisPassword() : null)
             .ssl(true)
            .build();

    private static final JedisPooled pool = new JedisPooled(
            new HostAndPort(Config.redisHost(), Config.redisPort()),
            clientConfig
    );
  private static final Gson gson = new Gson();

  public static void set(String key, String json, int ttlSec) { pool.setex(key, ttlSec, json); }
  public static void set(String key, String json) { set(key, json, Config.redisTtl()); }
  public static String get(String key) { return pool.get(key); }
  public static void del(String key) { pool.del(key); }

  public static <T> void setObj(String key, T obj, int ttl) { set(key, gson.toJson(obj), ttl); }

    public static <T> T getHashObj(String key, Class<T> clazz) {
        Map<String, String> hash = pool.hgetAll(key);

        if (hash == null || hash.isEmpty()) return null;

        return gson.fromJson(gson.toJsonTree(hash), clazz);
    }
  public static <T> T getObj(String key, Class<T> clazz) {
    String v = get(key);
    return v == null ? null : gson.fromJson(v, clazz);
    }
}