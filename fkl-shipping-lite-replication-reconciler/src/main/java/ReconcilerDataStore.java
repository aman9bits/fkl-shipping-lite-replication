import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by aman.gupta on 04/12/15.
 */
public interface ReconcilerDataStore {
    public void insertCheckpoint(long checkpoint, String shardId);
    public void insertCheckpointTimeMapping(long checkpoint, double timestamp, String shardId);
    public Map<String,Long> getShardData();
    public Map<String,Long> getLatestCheckpoints();
    //public Map<String,ArrayList<Long>> getMissingIds(Map<String, Long> shardData);

    Map<String,Double> getTimeMappingFromCheckpoint(Map<String,Long> checkpointsMap);

    void setFinalCheckpointedTime(Double final_time);

    void purgeCheckpointsList(String shardId, Long checkpoint);
    
    Long[] getCheckpointsList(String shardId, Long checkpoint);

    Double getCheckpointedTime();

    void addMissingIds(String shardId, List<Long> missingList);

    ArrayList<Long> getMissingIds(String shardId, Long checkpoint);

    void purgeMappingsList(String shardId, Long checkpoint);

    void purgeMissingIdsList(String shardId, Long checkpoint);
}
