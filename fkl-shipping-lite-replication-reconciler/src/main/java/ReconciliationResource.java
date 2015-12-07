
/**
 * Created by aman.gupta on 04/09/15.
 */

import com.google.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.*;
import java.util.Map.Entry;


@Path("/")
public class ReconciliationResource {
    ReconciliationConfiguration configuration;
    public static final Logger LOGGER = LoggerFactory.getLogger(ReconciliationResource.class);
    String redisServerIPAddress;
    String shardDataKeyName;
    String latestCheckpointsKeyName;
    String finalCheckpointTimeKeyName;
    String missingIdKeyNamePrefix;
    String checkpointListPrefix;
    String checkpointTimeMappingPrefix;

    @Inject
    public ReconciliationResource(ReconciliationConfiguration configuration) {
        this.configuration = configuration;
        this.redisServerIPAddress = configuration.getRedisServerIPAddress();
        this.shardDataKeyName = configuration.getRedisKeyNamesConfig().getShardDataKeyName();
        this.latestCheckpointsKeyName = configuration.getRedisKeyNamesConfig().getLatestCheckpointsPerShardKeyName();
        this.finalCheckpointTimeKeyName = configuration.getRedisKeyNamesConfig().getFinalCheckpointTimeKeyName();
        this.missingIdKeyNamePrefix = configuration.getRedisKeyNamesConfig().getMissingIdKeyNamePrefix();
        this.checkpointListPrefix = configuration.getRedisKeyNamesConfig().getCheckpointListPrefix();
        this.checkpointTimeMappingPrefix = configuration.getRedisKeyNamesConfig().getCheckpointTimeMappingPrefix();
    }

    @Inject
    ReconcilerDataStore dataStore;
    @POST
    @Consumes("application/json")
    @Path("/insert")
    public Response insertCheckpoint(String request) {
        /*
        LOGGER.info("Insert Checkpoint Service started for payload: ", request);
        try {
            JSONObject requestJson = new JSONObject(request);
            long checkpoint = requestJson.getLong("checkpointID");
            String shardId = requestJson.getString("shardId");
            long timestamp = requestJson.getLong("createdAt");
            jedis.zadd(checkpointListPrefix + shardId, (double)checkpoint, String.valueOf(checkpoint));
            LOGGER.info("Checkpoint added for checkpoint " + checkpoint + " and shardId " + shardId);
            jedis.zadd(this.checkpointTimeMappingPrefix + shardId, (double)timestamp, String.valueOf(checkpoint));
            LOGGER.info("Checkpoint-Time Mapping added for checkpoint " + checkpoint + " and shardId " + shardId);
            return Response.status(Status.OK).build();
        } catch (JSONException e) {
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (JedisConnectionException e) {
            return Response.status(Status.BAD_GATEWAY).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOGGER.error("error", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }*/
        LOGGER.info("Insert Checkpoint Service started for payload: ", request);
        try {
            JSONObject requestJson = new JSONObject(request);
            long checkpoint = requestJson.getLong("checkpointID");
            String shardId = requestJson.getString("shardId");
            long timestamp = requestJson.getLong("createdAt");
            dataStore.insertCheckpoint(checkpoint, shardId);
            LOGGER.info("Checkpoint added for checkpoint " + checkpoint + " and shardId " + shardId);
            dataStore.insertChekpointTimeMapping(checkpoint,timestamp,shardId);
            LOGGER.info("Checkpoint-Time Mapping added for checkpoint " + checkpoint + " and shardId " + shardId);
            return Response.status(Status.OK).build();
        } catch (JSONException e) {
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOGGER.error("Insert failed", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/findMissingEntries")
    public void findMissingEntries() {
        /*
        LOGGER.info("FindMissingEntries service started");
        ArrayList thread = new ArrayList();
        LOGGER.info("Getting shard data.. ");
        Map shardData = jedis.hgetAll(shardDataKeyName);
        LOGGER.info("Getting latest checkpointed values for each shard...");
        Map latest_checkpoints = jedis.hgetAll(this.latestCheckpointsKeyName);
        Iterator i = latest_checkpoints.entrySet().iterator();

        while(i.hasNext()) {
            Entry th = (Entry)i.next();
            String shardId = (String)th.getKey();
            String checkpoint = (String)th.getValue();
            long latestSSH = Long.parseLong((String)shardData.get(shardId));
            thread.add(new FindMissingIdThread(shardId, checkpoint, redisServerIPAddress, missingIdKeyNamePrefix, checkpointListPrefix, latestSSH));
        }

        i = thread.iterator();

        while(i.hasNext()) {
            FindMissingIdThread th1 = (FindMissingIdThread)i.next();
            th1.start();
            LOGGER.info("Find missing id Thread started for shard " + th1.shardId);
        }
*/
        Map<String,Long> shardData = dataStore.getShardData();
        Map<String,Long> latestCheckpoints = dataStore.getLatestCheckpoints();
        Map<String,ArrayList<Long>> missingIds = dataStore.findMissingIds(shardData,latestCheckpoints);
        dataStore.storeMissingIds(missingIds);
    }

    @POST
    @Path("/reconcile")
    public void reconcile() {
        LOGGER.info("Reconcile service started.");
        LOGGER.info("Getting shard data..");
        Set shardIds = jedis.hkeys(this.shardDataKeyName);
        Iterator i = shardIds.iterator();
        ArrayList thread = new ArrayList();
        String marvinAPI = this.configuration.getApiConfig().getMarvinWorkflowStartAPI();
        String getSSHBatchAPI = this.configuration.getApiConfig().getGetShipmentStatusHistoriesBatchAPI();

        while(i.hasNext()) {
            String i2 = (String)i.next();
            thread.add(new ReconciliationThread(i2, this.redisServerIPAddress, this.missingIdKeyNamePrefix, getSSHBatchAPI, marvinAPI));
        }

        for (Object aThread : thread) {
            ReconciliationThread th = (ReconciliationThread) aThread;
            th.start();
            LOGGER.info("Reconciliation Thread started for shard " + th.shardId);
        }

    }

    @POST
    @Path("/syncTime")
    public void syncTime() {
        LOGGER.info("syncTime service started");
        String latestSSHAPI = this.configuration.getApiConfig().getGetLatestSSHPerShardAPI();
        LOGGER.info("Making GET call to url: " + latestSSHAPI);
        Client client = ClientBuilder.newClient();

        try {
            Response response = client.target(latestSSHAPI).request().get();
            List latestSSHPerShardList = response.readEntity(List.class);
            LOGGER.info("Call successful with response " + response.getStatus() + " and response as " + latestSSHPerShardList);
            LOGGER.info("Getting latest checkpointed values for each shard..");
            Map checkpointsMap = jedis.hgetAll(this.latestCheckpointsKeyName);
            Transaction transaction = jedis.multi();
            ArrayList shardIds = new ArrayList();
            HashMap checkpointTimeMapping = new HashMap();

            for (Object o : checkpointsMap.entrySet()) {
                Entry final_timestamp = (Entry) o;
                String shardId = (String) final_timestamp.getKey();
                transaction.zscore(this.checkpointTimeMappingPrefix + final_timestamp.getKey(), (String) final_timestamp.getValue());
                shardIds.add(shardId);
            }

            List trans = transaction.exec();

            for(int i = 0; i < shardIds.size(); ++i) {
                checkpointTimeMapping.put(shardIds.get(i), trans.get(i));
            }

            String final_time = findSyncedTime(checkpointsMap, latestSSHPerShardList, checkpointTimeMapping);
            jedis.set(this.finalCheckpointTimeKeyName, final_time);
            LOGGER.info("New final checkpointed time saved as " + final_time);
        } catch (Exception exception) {
            LOGGER.error("Sync Failed", exception);
        }

    }

    public static String findSyncedTime(Map<String, String> checkpointsMap, List<Map<String, String>> latestSSHPerShardList, Map<String, Double> checkpointTimeMapping) {
        double final_timestamp = 1.7976931348623157E308D;

        for (Object aLatestSSHPerShardList : latestSSHPerShardList) {
            Map shardDataItem = (Map) aLatestSSHPerShardList;
            String shardId = (String) shardDataItem.get("shardId");
            long redisCheckpoint = Long.parseLong(checkpointsMap.get(shardId));
            if (Long.parseLong((String) shardDataItem.get("sshId")) == redisCheckpoint) {
                final_timestamp = Double.min(final_timestamp, Double.parseDouble((String) shardDataItem.get("currentTimeStamp")));
            } else {
                double time = checkpointTimeMapping.get(shardId);
                final_timestamp = Double.min(final_timestamp, time);
            }
        }

        return String.valueOf(final_timestamp);
    }

    @POST
    @Path("/purge")
    public void purgeCheckpointList() {
        LOGGER.info("Purge service started.");
        LOGGER.info("Starting purge operation for checkpoints lists..");
        LOGGER.info("Getting latest checkpointed values for each shard..");
        Map latest_checkpoints = jedis.hgetAll(this.latestCheckpointsKeyName);

        for (Object o : latest_checkpoints.entrySet()) {
            Entry entry = (Entry) o;
            String shardId = (String) entry.getKey();
            long entry1 = Long.parseLong((String) entry.getValue());
            jedis.zremrangeByScore(this.checkpointListPrefix + shardId, -9.223372036854776E18D, (double) (entry1 - 1L));
            LOGGER.info("Checkpoints List purged till checkpoint id: " + String.valueOf(entry1 - 1L) + " for shard " + shardId);
        }

        LOGGER.info("Purge for checkpoint lists completed.");
        LOGGER.info("Starting purge operation for mapping lists..");
        long finalCheckpointTime1 = Long.parseLong(jedis.get(this.finalCheckpointTimeKeyName));
        Iterator shardId2 = latest_checkpoints.entrySet().iterator();

        String shardId1;
        Entry entry2;
        while(shardId2.hasNext()) {
            entry2 = (Entry)shardId2.next();
            shardId1 = (String)entry2.getKey();
            jedis.zremrangeByScore(this.checkpointTimeMappingPrefix + shardId1, -9.223372036854776E18D, (double)(finalCheckpointTime1 - 1L));
            LOGGER.info("Mapping purged till timestamp: " + String.valueOf(finalCheckpointTime1 - 1L) + " for shard " + shardId1);
        }

        LOGGER.info("Purge for mapping lists completed.");
        LOGGER.info("Starting purge operation for missing IDs lists..");
        shardId2 = latest_checkpoints.entrySet().iterator();

        while(shardId2.hasNext()) {
            entry2 = (Entry)shardId2.next();
            shardId1 = (String)entry2.getKey();
            long checkpoint = Long.parseLong((String)entry2.getValue());
            jedis.zremrangeByScore(this.missingIdKeyNamePrefix + shardId1, -9.223372036854776E18D, (double)checkpoint);
            LOGGER.info("Missing ids purged till checkpoint: " + String.valueOf(checkpoint) + " for shard " + shardId1);
        }

        LOGGER.info("Purge for missing ids finished.");
    }

    @POST
    @Path("/updateCheckpoint")
    public void updateCheckpoint() {
        LOGGER.info("updateCheckpoint service started.");
        LOGGER.info("Getting latest checkpoints for each shard..");
        Map latest_checkpoints = jedis.hgetAll(this.latestCheckpointsKeyName);
        ArrayList checkpointTime = new ArrayList();

        for (Object o : latest_checkpoints.entrySet()) {
            Entry entry = (Entry) o;
            String iterator = (String) entry.getKey();
            long currentTime = Long.parseLong((String) entry.getValue());
            LOGGER.info("Getting checkpoint list for shard " + iterator);
            Set list = jedis.zrangeByScore(this.checkpointListPrefix + iterator, (double) currentTime, 9.223372036854776E18D);
            String[] arr = (String[]) list.toArray(new String[list.size()]);
            int length = list.size();
            byte low = 0;
            int high = length - 1;
            LOGGER.info("Finding the new checkpoint for shard " + iterator);
            String newCheckpoint = findCheckpoint(low, high, length, arr);
            LOGGER.info("New checkpoint for shard " + iterator + " is " + newCheckpoint);
            latest_checkpoints.put(iterator, newCheckpoint);
            LOGGER.info("Getting timestamp values corresponding to new checkpoints..");
            checkpointTime.add(jedis.zscore(this.checkpointTimeMappingPrefix + iterator, newCheckpoint));
        }

        jedis.hmset(this.latestCheckpointsKeyName, latest_checkpoints);
        LOGGER.info("New checkpoint values updated in redis DB.");
        double finalCheckpointTime1 = 0.0D;

        for (Object aCheckpointTime : checkpointTime) {
            double currentTime1 = (Double) aCheckpointTime;
            if (finalCheckpointTime1 == 0.0D || finalCheckpointTime1 < currentTime1) {
                finalCheckpointTime1 = currentTime1;
            }
        }

        String currentTime2 = jedis.get(this.finalCheckpointTimeKeyName);
        if((double)Long.parseLong(currentTime2) < finalCheckpointTime1) {
            jedis.set(this.finalCheckpointTimeKeyName, String.valueOf(finalCheckpointTime1));
            LOGGER.info("Final checkpoint time saved as: " + finalCheckpointTime1);
        } else {
            LOGGER.info("Final checkpoint time unchanged.");
        }

    }

    public static String findCheckpoint(int low, int high, int length, String[] arr) {
        System.out.print("" + low + high + length + arr);
        if(high == low) {
            return arr[low];
        } else if((long)(high - low) == Long.parseLong(arr[high]) - Long.parseLong(arr[low])) {
            return arr[high];
        } else {
            int mid = (low + high) / 2;
            return mid == low?arr[low]:((long)(mid - low) == Long.parseLong(arr[mid]) - Long.parseLong(arr[low])?findCheckpoint(mid, high, (length + 1) / 2, arr):findCheckpoint(low, mid, (length + 1) / 2, arr));
        }
    }

    @GET
    @Path("/test")
    public void test() {
        HashMap map = new HashMap();
        map.put("key1", "val1");
        map.put("key2", "");
        map.put("key3", null);
        System.out.println(map.toString());
        JSONObject json = new JSONObject(map);
        json.append("key4", null);
        System.out.println(json.toString());
        JSONObject json2 = new JSONObject(map);
        System.out.println(json2);
    }
}
