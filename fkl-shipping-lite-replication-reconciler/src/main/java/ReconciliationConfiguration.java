import com.bendb.dropwizard.redis.JedisFactory;
import io.dropwizard.Configuration;

/**
 * Created by aman.gupta on 04/09/15.
 */

public class ReconciliationConfiguration extends Configuration {
    private String redisServerIPAddress;
    private APIConfig apiConfig;
    private RedisKeyNamesConfig redisKeyNamesConfig;
    private RedisConfiguration redisConfiguration;

    public ReconciliationConfiguration() {
    }

    public RedisConfiguration getRedisConfiguration() {
        return redisConfiguration;
    }

    public void setRedisConfiguration(RedisConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
    }

    public APIConfig getApiConfig() {
        return this.apiConfig;
    }

    public void setApiConfig(APIConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public RedisKeyNamesConfig getRedisKeyNamesConfig() {
        return this.redisKeyNamesConfig;
    }

    public void setRedisKeyNamesConfig(RedisKeyNamesConfig redisKeyNamesConfig) {
        this.redisKeyNamesConfig = redisKeyNamesConfig;
    }

    public String getRedisServerIPAddress() {
        return this.redisServerIPAddress;
    }

    public void setRedisServerIPAddress(String redisServerIPAddress) {
        this.redisServerIPAddress = redisServerIPAddress;
    }
}
