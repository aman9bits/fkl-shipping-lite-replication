
/**
 * Created by aman.gupta on 04/09/15.
 */

import com.google.common.collect.Lists;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ReconciliationThread extends Thread {
    String shardId;
    ArrayList<Long> missingIds;
    String getSSHBatchAPI;
    String marvinAPI;
    private static final int MAX_BATCH_SIZE = 100;
    public static final Logger LOGGER = LoggerFactory.getLogger(ReconciliationThread.class);

    public ReconciliationThread(String shardId, ArrayList<Long> missingIds, String getSSHBatchAPI, String marvinAPI) {
        this.shardId = shardId;
        this.missingIds = missingIds;
        this.getSSHBatchAPI = getSSHBatchAPI;
        this.marvinAPI = marvinAPI;

    }

    public void run(){
        LOGGER.info("Reconciliation thread started for shard " + this.shardId);
        Iterator iterator = Lists.partition(missingIds, MAX_BATCH_SIZE).iterator();
        while(iterator.hasNext()) {
            List partition = (List)iterator.next();
            String request = partition.toString();
            Client client = ClientBuilder.newClient();
            LOGGER.info("Making a rest API call to url:" + this.getSSHBatchAPI + " with request payload as " + request);
            try {
                Response response = client.target(this.getSSHBatchAPI).request().post(Entity.entity(partition, "application/json"));
                String responseString = response.readEntity(String.class);
                LOGGER.info("Data received successfully for checkpoints " + request + " as " + responseString);
                //Confirm this conversion later
                List sshList = Arrays.asList(new JSONObject(responseString.substring(1, responseString.length() - 1).split(", ")));
                Iterator it = sshList.iterator();
                while(it.hasNext()) {
                    JSONObject payload = (JSONObject)iterator.next();
                    try {
                        Response res = client.target(this.marvinAPI).request()
                                .header("Accept-Language", "en-US,en;q=0.5")
                                .header("Content-Type", "application/json")
                                .header("MARVIN_CLIENT_ID", "shipping-lite-secondary-datastore")
                                .header("MARVIN_REF_ID", payload.getString("parent_sr_id"))
                                .header("MARVIN_WORKFLOW_IDEMPOTENCY", "true")
                                .header("MARVIN_IDEMPOTENCY_DURATION", "60")
                                .post(Entity.entity(payload.toString(), "application/json"));
                        if(res.getStatus() >= 200 && res.getStatus() < 300) {
                            LOGGER.info("Marvin workflow call succeeded for sshId :" + payload.getString("id"));
                        } else {
                            LOGGER.info("Marvin workflow call failed for sshId :" + payload.getString("id"));
                        }
                    } catch (Exception exception) {
                        LOGGER.error("Error while calling marvin workflow for sshId: "+payload.getString("id"), exception);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Unable to connect", e);
            }
        }
    }
    /*@Context
    Jedis jedis;
    String shardId;
    String redisServerIPAddress;
    String missingIdKeyNamePrefix;
    String getShipmentStatusHistoriesBatchAPI;
    String marvinWorkflowStartAPI;
    private static final int MAX_BATCH_SIZE = 100;
    public static final Logger LOGGER = LoggerFactory.getLogger(ReconciliationThread.class);

    public ReconciliationThread(String shardId, String redisServerIPAddress, String missingIdKeyNamePrefix, String getShipmentStatusHistoriesBatchAPI, String marvinWorkflowStartAPI) {
        this.shardId = shardId;
        this.redisServerIPAddress = redisServerIPAddress;
        this.missingIdKeyNamePrefix = missingIdKeyNamePrefix;
        this.getShipmentStatusHistoriesBatchAPI = getShipmentStatusHistoriesBatchAPI;
        this.marvinWorkflowStartAPI = marvinWorkflowStartAPI;
    }

    public void run() {
        LOGGER.info("Reconciliation thread started for shard " + this.shardId);
        Set setOfIds = jedis.zrange(this.missingIdKeyNamePrefix + this.shardId, Long.MIN_VALUE, Long.MAX_VALUE);
        LOGGER.info("List of missing ids received for shard " + this.shardId);
        ArrayList listOfIds = new ArrayList();
        listOfIds.addAll(setOfIds);
        LOGGER.info("Creating batches of size 100 from missing id list.");
        Iterator iterator = Lists.partition(listOfIds, MAX_BATCH_SIZE).iterator();

        while(iterator.hasNext()) {
            List partition = (List)iterator.next();
            String request = partition.toString();
            Client client = ClientBuilder.newClient();
            LOGGER.info("Making a rest API call to url:" + this.getShipmentStatusHistoriesBatchAPI + " with request payload as " + request);

            try {
                Response response = client.target(this.getShipmentStatusHistoriesBatchAPI).request().post(Entity.entity(partition, "application/json"));
                LOGGER.info("Data received successfully for checkpoints " + request + " as " + response.readEntity(String.class));
                String responseString = response.readEntity(String.class);
                List sshList = Arrays.asList(new JSONObject[]{new JSONObject(responseString.substring(1, responseString.length() - 1).split(", "))});
                Iterator it = sshList.iterator();
                while(it.hasNext()) {
                    JSONObject payload = (JSONObject)iterator.next();

                    try {
                        Response res = client.target(this.marvinWorkflowStartAPI).request()
                                .header("Accept-Language", "en-US,en;q=0.5")
                                .header("Content-Type", "application/json")
                                .header("MARVIN_CLIENT_ID", "shipping-lite-secondary-datastore")
                                .header("MARVIN_REF_ID", payload.getString("parent_sr_id"))
                                .header("MARVIN_WORKFLOW_IDEMPOTENCY", "true")
                                .header("MARVIN_IDEMPOTENCY_DURATION", "60")
                                .post(Entity.entity(payload.toString(), "application/json"));
                        if(res.getStatus() >= 200 && res.getStatus() < 300) {
                            LOGGER.info("Marvin workflow call succeeded for sshId :" + payload.getString("id"));
                        } else {
                            LOGGER.info("Marvin workflow call failed for sshId :" + payload.getString("id"));
                        }
                    } catch (Exception exception) {
                        LOGGER.error("Error while calling marvin workflow for sshId: "+payload.getString("id"), exception);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Unable to connect", e);
            }
        }

    }
    */
}
