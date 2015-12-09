/**
 * Created by aman.gupta on 24/11/15.
 */


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

    public ReconciliationApplicationTest() {
    }
}