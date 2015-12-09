
/**
 * Created by aman.gupta on 04/09/15.
 */
public class RedisKeyNamesConfig {
    private String shardDataKeyName;
    private String latestCheckpointsKeyName;
    private String finalCheckpointTimeKeyName;
    private String missingIdKeyNamePrefix;
    private String checkpointListPrefix;
    private String checkpointTimeMappingPrefix;

    public RedisKeyNamesConfig() {
    }

    public String getCheckpointListPrefix() {
        return this.checkpointListPrefix;
    }

    public void setCheckpointListPrefix(String checkpointListPrefix) {
        this.checkpointListPrefix = checkpointListPrefix;
    }

    public String getCheckpointTimeMappingPrefix() {
        return this.checkpointTimeMappingPrefix;
    }

    public void setCheckpointTimeMappingPrefix(String checkpointTimeMappingPrefix) {
        this.checkpointTimeMappingPrefix = checkpointTimeMappingPrefix;
    }

    public String getShardDataKeyName() {
        return this.shardDataKeyName;
    }

    public void setShardDataKeyName(String shardDataKeyName) {
        this.shardDataKeyName = shardDataKeyName;
    }

    public String getLatestCheckpointsKeyName() {
        return this.latestCheckpointsKeyName;
    }

    public void setLatestCheckpointsKeyName(String latestCheckpointsKeyName) {
        this.latestCheckpointsKeyName = latestCheckpointsKeyName;
    }

    public String getFinalCheckpointTimeKeyName() {
        return this.finalCheckpointTimeKeyName;
    }

    public void setFinalCheckpointTimeKeyName(String finalCheckpointTimeKeyName) {
        this.finalCheckpointTimeKeyName = finalCheckpointTimeKeyName;
    }

    public String getMissingIdKeyNamePrefix() {
        return this.missingIdKeyNamePrefix;
    }

    public void setMissingIdKeyNamePrefix(String missingIdKeyNamePrefix) {
        this.missingIdKeyNamePrefix = missingIdKeyNamePrefix;
    }
}
