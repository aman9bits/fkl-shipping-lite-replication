/**
 * Created by aman.gupta on 24/11/15.
 */

import com.bendb.dropwizard.redis.JedisBundle;
import com.bendb.dropwizard.redis.JedisFactory;
import io.dropwizard.setup.Bootstrap;
import org.mockito.Mock;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.ws.rs.client.Client;

public class ReconciliationApplicationTest extends ReconciliationApplication {
    @Mock
    Jedis jedis = Mockito.mock(Jedis.class);
    @Mock
    ReconciliationResource resource = Mockito.mock(ReconciliationResource.class);
    @Mock
    Client client = Mockito.mock(Client.class);
    @Mock
    JedisFactory jedisFactory = Mockito.mock(JedisFactory.class);
    @Mock
    JedisPool jedisPool = Mockito.mock(JedisPool.class);
    @Mock
    JedisBundle<ReconciliationConfiguration> jedisBundle = Mockito.mock(JedisBundle.class);
    @Override
    public void initialize(Bootstrap<ReconciliationConfiguration> bootstrap) {
       /* jedisBundle = new JedisBundle<ReconciliationConfiguration>() {
            @Override
            public JedisFactory getJedisFactory(ReconciliationConfiguration configuration) {
                return configuration.getRedis();
            }
        };*/
        bootstrap.addBundle(jedisBundle);
    }
    public ReconciliationApplicationTest() {
    }
}