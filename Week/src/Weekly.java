import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

class MultiLevelCache {

    // L1 Cache (fast memory) - LRU
    private LinkedHashMap<String, VideoData> L1;

    // L2 Cache (SSD simulated)
    private LinkedHashMap<String, VideoData> L2;

    // L3 Database
    private HashMap<String, VideoData> L3 = new HashMap<>();

    private int L1_SIZE = 10000;
    private int L2_SIZE = 100000;

    int l1Hits = 0;
    int l2Hits = 0;
    int l3Hits = 0;

    public MultiLevelCache() {

        L1 = new LinkedHashMap<String, VideoData>(L1_SIZE, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L1_SIZE;
            }
        };

        L2 = new LinkedHashMap<String, VideoData>(L2_SIZE, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                return size() > L2_SIZE;
            }
        };

        // Simulated database videos
        L3.put("video_123", new VideoData("video_123", "Movie A"));
        L3.put("video_456", new VideoData("video_456", "Movie B"));
        L3.put("video_999", new VideoData("video_999", "Movie C"));
    }

    public VideoData getVideo(String videoId) {

        long start = System.currentTimeMillis();

        // L1 Cache
        if (L1.containsKey(videoId)) {
            l1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 Cache
        if (L2.containsKey(videoId)) {
            l2Hits++;
            System.out.println("L2 Cache HIT (5ms)");

            VideoData data = L2.get(videoId);

            // Promote to L1
            L1.put(videoId, data);
            System.out.println("Promoted to L1");

            return data;
        }

        System.out.println("L2 Cache MISS");

        // L3 Database
        if (L3.containsKey(videoId)) {

            l3Hits++;
            System.out.println("L3 Database HIT (150ms)");

            VideoData data = L3.get(videoId);

            // Add to L2
            L2.put(videoId, data);
            System.out.println("Added to L2");

            return data;
        }

        System.out.println("Video not found");
        return null;
    }

    public void getStatistics() {

        int total = l1Hits + l2Hits + l3Hits;

        if (total == 0) {
            System.out.println("No requests yet.");
            return;
        }

        double l1Rate = (l1Hits * 100.0) / total;
        double l2Rate = (l2Hits * 100.0) / total;
        double l3Rate = (l3Hits * 100.0) / total;

        System.out.println("\nCache Statistics:");
        System.out.printf("L1 Hit Rate: %.2f%%\n", l1Rate);
        System.out.printf("L2 Hit Rate: %.2f%%\n", l2Rate);
        System.out.printf("L3 Hit Rate: %.2f%%\n", l3Rate);

        System.out.printf("Overall Hit Rate: %.2f%%\n", (l1Rate + l2Rate));
    }
}

public class weekly {

    public static void main(String[] args) {

        MultiLevelCache cache = new MultiLevelCache();

        System.out.println("getVideo(video_123)");
        cache.getVideo("video_123");

        System.out.println("\ngetVideo(video_123) second request");
        cache.getVideo("video_123");

        System.out.println("\ngetVideo(video_999)");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}