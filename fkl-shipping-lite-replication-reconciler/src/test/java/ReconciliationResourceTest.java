/**
 * Created by aman.gupta on 24/11/15.
 */

import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class ReconciliationResourceTest {
    /*
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("conf-test.yml");
    Client client;
    ReconciliationApplicationTest application;
    @ClassRule
    public static final DropwizardAppRule<ReconciliationConfiguration> RULE = new DropwizardAppRule(ReconciliationApplicationTest.class, CONFIG_PATH, new ConfigOverride[0]);
    ReconciliationResource resource;

    public ReconciliationResourceTest() {
    }

    @Before
    public void setup() {
        this.client = ClientBuilder.newClient();
        this.application = RULE.getApplication();
        this.resource = this.application.resource;
    }

    @Test
    public void checkpointShouldBeInserted() {
        Mockito.when(this.application.jedis.zadd(Mockito.anyString(), Mockito.anyDouble(), Mockito.anyString())).thenReturn(1L);
        String request = ReconciliationResourceTestData.createInsertCheckpointPayload();
        Response response = this.client.target("http://localhost:8096/insert").request().post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals("payload must be same", 200L, (long)response.getStatus());
        Mockito.reset(this.application.jedis);
    }

    @Test
    public void checkpointShouldNotBeInsertedWhenWrongPayload() {
        Mockito.when(this.application.jedis.zadd(Mockito.anyString(), Mockito.anyDouble(), Mockito.anyString())).thenReturn(1L);
        String request = ReconciliationResourceTestData.createFaultyInsertCheckpointPayload();
        Response response = this.client.target("http://localhost:8096/insert").request().post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals("payload must be same", 400L, (long)response.getStatus());
        Mockito.reset(this.application.jedis);
    }

    @Test
    public void checkpointShouldNotBeInsertedWhenExceptionRaised() {
        Mockito.when(application.jedisPool.getResource()).thenReturn(application.jedis);
        Mockito.when(application.jedisBundle.getPool()).thenReturn(application.jedisPool);
        Mockito.when(application.jedis.zadd(Mockito.anyString(), Mockito.anyDouble(), Mockito.anyString())).thenThrow(new JedisConnectionException(""));
        Mockito.verify(application.jedisPool).getResource();
        String request = ReconciliationResourceTestData.createInsertCheckpointPayload();
        Response response = this.client.target("http://localhost:8096/insert").request().post(Entity.entity(request, MediaType.APPLICATION_JSON_TYPE));
        Assert.assertEquals("payload must be same", 502L, (long)response.getStatus());
        Mockito.reset(application.jedis);
    }

    @Test
    public void findCheckpointTest() {
        String[] arr = ReconciliationResourceTestData.getFindCheckpointTestArray();
        String output = ReconciliationResource.findCheckpoint(0, arr.length - 1, arr.length, arr);
        Assert.assertEquals("6", output);
    }

    @Test
    public void syncTimeTest() {
        String response = ReconciliationResource.findSyncedTime(ReconciliationResourceTestData.getLatestCheckpoints(), ReconciliationResourceTestData.getLatestSSHPerShardResult(), ReconciliationResourceTestData.getCheckpointTimeMapping());
        Assert.assertEquals(String.valueOf(8.73281901E8D), response);
    }

    @Test
    public void findMissingIdsTest() {
        byte low = 0;
        String[] arr = ReconciliationResourceTestData.getFindMissingIdsArray();
        int high = arr.length - 1;
        HashMap missingIds = new HashMap();
        FindMissingIdThread.binarySearch(low, high, arr, missingIds);
        Map expectedList = ReconciliationResourceTestData.getFindMissingIdsResult();
        Assert.assertTrue("Computed Missing Ids are not as expected \n Expected: " + expectedList + "\n Found: " + missingIds, expectedList.equals(missingIds));
    }


*/
}
