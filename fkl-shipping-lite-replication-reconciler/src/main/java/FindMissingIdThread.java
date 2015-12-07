import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import javax.ws.rs.core.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by aman.gupta on 21/09/15.
 */

public class FindMissingIdThread extends Thread {
    @Context
    Jedis jedis;
    String shardId;
    String checkpoint;
    String redisServerIPAddress;
    String missingIdKeyNamePrefix;
    String checkpointListPrefix;
    long latestSSH;
    public static final Logger LOGGER = LoggerFactory.getLogger(FindMissingIdThread.class);

    public FindMissingIdThread(String shardId, String checkpoint, String redisServerIPAddress, String missingIdKeyNamePrefix, String checkpointListPrefix, long latestSSH) {
        this.shardId = shardId;
        this.checkpoint = checkpoint;
        this.redisServerIPAddress = redisServerIPAddress;
        this.missingIdKeyNamePrefix = missingIdKeyNamePrefix;
        this.checkpointListPrefix = checkpointListPrefix;
        this.latestSSH = latestSSH;
    }

    public void run() {
        LOGGER.info("FindMissingId thread started for shard " + this.shardId);
        LOGGER.info("Getting checkpoint list for shard " + this.shardId);
        Set list = this.jedis.zrangeByScore(this.checkpointListPrefix + this.shardId, (double) Long.parseLong(this.checkpoint), 9.223372036854776E18D);
        String[] arr = (String[])list.toArray(new String[0]);
        int length = list.size();
        byte low = 0;
        int high = length - 1;
        HashMap missingList = new HashMap();
        binarySearch(low, high, arr, missingList);

        for(long missingId = Long.parseLong(arr[length]) + 1L; missingId <= this.latestSSH; ++missingId) {
            missingList.put(String.valueOf(missingId), Double.valueOf((double)missingId));
        }

        this.jedis.zadd(this.missingIdKeyNamePrefix + this.shardId, missingList);
        LOGGER.info("Missing Ids added to the corresponding list for shard " + this.shardId);
    }

    public static void binarySearch(int low, int high, String[] arr, Map arr2) {
        if(high - low != 1) {
            if(high - low != Integer.parseInt(arr[high]) - Integer.parseInt(arr[low])) {
                int mid = (low + high) / 2;
                binarySearch(low, mid, arr, arr2);
                binarySearch(mid, high, arr, arr2);
            }

        } else {
            for(long mid = (long)(Integer.parseInt(arr[low]) + 1); mid < (long)Integer.parseInt(arr[high]); ++mid) {
                arr2.put(String.valueOf(mid), Double.valueOf((double)mid));
            }

        }
    }
}
