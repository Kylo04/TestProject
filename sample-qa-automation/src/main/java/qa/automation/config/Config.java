package qa.automation.config;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {
    private static final Properties p = new Properties();

    static {
        try {
            p.load(new FileInputStream("src/main/resources/config.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Failed loading config.properties", e);
        }
    }

    private static String env(String key, String def) {
        return System.getenv().getOrDefault(key, System.getProperty(key, def));
    }

    public static String baseUrl() { return env("BASE_URL", p.getProperty("baseUrl")); }
    public static String getPassword(String userName){return p.getProperty(userName);}
    public static boolean headless() { return Boolean.parseBoolean(env("HEADLESS", p.getProperty("headless"))); }
    public static boolean trace() { return Boolean.parseBoolean(env("TRACE", p.getProperty("trace"))); }
    public static String screenshotDir() { return p.getProperty("screenshotDir"); }
    public static String reportsDir() { return p.getProperty("reportsDir"); }
    public static String redisHost() { return env("REDIS_HOST", p.getProperty("redis.host")); }
    public static int redisPort() { return Integer.parseInt(env("REDIS_PORT", p.getProperty("redis.port"))); }
    public static String redisPassword() { return env("REDIS_PASSWORD", p.getProperty("redis.password")); }
    public static int redisTtl() { return Integer.parseInt(p.getProperty("redis.defaultTtlSeconds", "1800")); }
    public static String tablesConn() { return env("AZURE_TABLES_CONN", p.getProperty("azure.tables.connectionString")); }
    public static String table() { return p.getProperty("azure.tables.table"); }
    public static String apiConfigBaseUrl() { return env("API_CONFIG_BASE", p.getProperty("apiConfig.baseUrl")); }
    public static String apiDeleteUrl() { return env("API_DELETE_BASE", p.getProperty("apiDelete.baseUrl")); }
    public static String apiBearer() { return env("API_BEARER", p.getProperty("api.auth.bearerToken")); }
    public static int parallelThreads() { return Integer.parseInt(p.getProperty("parallel.threads", "4")); }
    public static String oracleDbUrl() { return env("ORACLE_DB_URL", p.getProperty("oracle.db.url")); }
    public static String oracleDbUser() { return env("ORACLE_DB_USER", p.getProperty("oracle.db.user")); }
    public static String oracleDbPassword() { return env("ORACLE_DB_PASSWORD", p.getProperty("oracle.db.password")); }
}