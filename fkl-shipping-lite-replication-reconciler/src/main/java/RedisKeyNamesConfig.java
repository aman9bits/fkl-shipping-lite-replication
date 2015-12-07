
/**
 * Created by aman.gupta on 04/09/15.
 */
public class RedisKeyNamesConfig {
    private String shardDataKeyName;
    private String latestCheckpointsPerShardKeyName;
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

    public String getLatestCheckpointsPerShardKeyName() {
        return this.latestCheckpointsPerShardKeyName;
    }

    public void setLatestCheckpointsPerShardKeyName(String latestCheckpointsPerShardKeyName) {
        this.latestCheckpointsPerShardKeyName = latestCheckpointsPerShardKeyName;
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
