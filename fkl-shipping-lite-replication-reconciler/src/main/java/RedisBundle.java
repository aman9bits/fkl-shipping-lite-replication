import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by aman.gupta on 04/12/15.
 */
public abstract class RedisBundle<T extends Configuration> implements ConfiguredBundle<T> {
    private static final Logger logger = LoggerFactory.getLogger(RedisBundle.class);
    private JedisPool pool;
    public abstract RedisConfiguration getRedisConfiguration(T configuration);

    @Override
    public void run(T configuration, Environment environment) throws Exception {
        RedisConfiguration redisConfiguration = getRedisConfiguration(configuration);
        pool = buildPool(redisConfiguration);
        environment.healthChecks().register("redis", new RedisHealthCheck(pool));
        environment.jersey().register(new RedisPoolBinder(pool));
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {}

    public Jedis getJedisClient(){
        return this.pool.getResource();
    }
    public JedisPool getPool(){return pool;}
    private JedisPool buildPool(RedisConfiguration redisConfiguration){
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxIdle(redisConfiguration.getMaxIdle());
        config.setMaxTotal(redisConfiguration.getMaxTotal());
        config.setMinIdle(redisConfiguration.getMinIdle());
        String host = redisConfiguration.getHost();
        int port = redisConfiguration.getPort();
        JedisPool pool = new JedisPool(config,host,port);
        return pool;
    }
}

