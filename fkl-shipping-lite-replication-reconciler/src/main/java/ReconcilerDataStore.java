import java.util.ArrayList;
import java.util.Map;

/**
 * Created by aman.gupta on 04/12/15.
 */
public interface ReconcilerDataStore {
    public void insertCheckpoint(long checkpoint, String shardId);
    public void insertChekpointTimeMapping(long checkpoint, long timestamp, String shardId);
    public Map<String,Long> getShardData();
    public Map<String,Long> getLatestCheckpoints();
    public Map<String,ArrayList<Long>> findMissingIds(Map<String,Long> shardData, Map<String,Long> latestCheckpoints);
    public void storeMissingIds(Map<String, ArrayList<Long>> missingIds);
}
