/**
 * Created by aman.gupta on 10/11/15.
 */
public class APIConfig {
    private String getTimeFromCheckpointBatchAPI;
    private String getShipmentStatusHistoriesBatchAPI;
    private String marvinWorkflowStartAPI;
    private String getLatestSSHPerShardAPI;

    public APIConfig() {
    }

    public String getGetTimeFromCheckpointBatchAPI() {
        return this.getTimeFromCheckpointBatchAPI;
    }

    public void setGetTimeFromCheckpointBatchAPI(String getTimeFromCheckpointBatchAPI) {
        this.getTimeFromCheckpointBatchAPI = getTimeFromCheckpointBatchAPI;
    }

    public String getGetShipmentStatusHistoriesBatchAPI() {
        return this.getShipmentStatusHistoriesBatchAPI;
    }

    public void setGetShipmentStatusHistoriesBatchAPI(String getShipmentStatusHistoriesBatchAPI) {
        this.getShipmentStatusHistoriesBatchAPI = getShipmentStatusHistoriesBatchAPI;
    }

    public String getMarvinWorkflowStartAPI() {
        return this.marvinWorkflowStartAPI;
    }

    public void setMarvinWorkflowStartAPI(String marvinWorkflowStartAPI) {
        this.marvinWorkflowStartAPI = marvinWorkflowStartAPI;
    }

    public String getGetLatestSSHPerShardAPI() {
        return this.getLatestSSHPerShardAPI;
    }

    public void setGetLatestSSHPerShardAPI(String getLatestSSHPerShardAPI) {
        this.getLatestSSHPerShardAPI = getLatestSSHPerShardAPI;
    }
}
