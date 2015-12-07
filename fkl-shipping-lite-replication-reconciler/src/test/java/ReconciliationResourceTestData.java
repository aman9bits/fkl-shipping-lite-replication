/**
 * Created by aman.gupta on 24/11/15.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReconciliationResourceTestData {
    public ReconciliationResourceTestData() {
    }

    public static String createInsertCheckpointPayload() {
        return "{\n    \"checkpointID\":1999,\n    \"shardId\":\"shard1\",\n    \"createdAt\":123123123\n}";
    }

    public static String createFaultyInsertCheckpointPayload() {
        return "{\n    \"checkpointaID\":1999,\n    \"shardId\":\"shard1\",\n    \"createdAt\":123123123\n}";
    }

    public static String[] getFindCheckpointTestArray() {
        String[] arr = new String[]{"1", "2", "3", "4", "5", "6", "8", "9", "11", "12"};
        return arr;
    }

    public static List<Map<String, String>> getLatestSSHPerShardResult() {
        ArrayList output = new ArrayList();

        for(int i = 0; i < 6; ++i) {
            HashMap map = new HashMap();
            map.put("shardId", "shard" + i);
            map.put("sshId", "123" + i);
            map.put("currentTimeStamp", "87328190" + i);
            output.add(map);
        }

        return output;
    }

    public static Map getLatestCheckpoints() {
        HashMap response = new HashMap();
        response.put("shard0", "1231");
        response.put("shard1", "1231");
        response.put("shard2", "1232");
        response.put("shard3", "1233");
        response.put("shard4", "1232");
        response.put("shard5", "1233");
        return response;
    }

    public static Map getCheckpointTimeMapping() {
        HashMap response = new HashMap();
        response.put("shard0", Double.valueOf(8.73281903E8D));
        response.put("shard1", Double.valueOf(8.732818E8D));
        response.put("shard2", Double.valueOf(8.73281802E8D));
        response.put("shard3", Double.valueOf(8.73281803E8D));
        response.put("shard4", Double.valueOf(8.73281911E8D));
        response.put("shard5", Double.valueOf(8.7328193E8D));
        return response;
    }

    public static String[] getFindMissingIdsArray() {
        String[] list = new String[]{"12", "15", "16", "18", "19", "21"};
        return list;
    }

    public static Map<String, Double> getFindMissingIdsResult() {
        HashMap list = new HashMap();
        list.put("13", Double.valueOf(13.0D));
        list.put("14", Double.valueOf(14.0D));
        list.put("17", Double.valueOf(17.0D));
        list.put("20", Double.valueOf(20.0D));
        return list;
    }
}
